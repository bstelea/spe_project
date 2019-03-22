package web.globalbeershop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import web.globalbeershop.GlobalbeershopApplication;
import web.globalbeershop.data.Beer;
import web.globalbeershop.data.Order;
import web.globalbeershop.data.OrderItem;
import web.globalbeershop.data.User;
import web.globalbeershop.exception.NoBeersInCartException;
import web.globalbeershop.exception.NotEnoughBeersInStockException;
import web.globalbeershop.repository.OrderItemRepository;
import web.globalbeershop.repository.OrderRepository;
import web.globalbeershop.service.BeerService;
import web.globalbeershop.service.NotificationService;
import web.globalbeershop.service.ShoppingCartService;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.Transaction.Status;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.CreditCard;
import com.braintreegateway.Customer;
import com.braintreegateway.ValidationError;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.globalbeershop.service.UserService;

import javax.validation.Valid;

@Controller
public class CheckoutController {


    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartService shoppingCartService;
    private final BeerService beerService;
    private final UserService userService;
    private final NotificationService notificationService;

    private final BraintreeGateway gateway = GlobalbeershopApplication.gateway;

    private Status[] TRANSACTION_SUCCESS_STATUSES = new Status[] {
            Transaction.Status.AUTHORIZED,
            Transaction.Status.AUTHORIZING,
            Transaction.Status.SETTLED,
            Transaction.Status.SETTLEMENT_CONFIRMED,
            Transaction.Status.SETTLEMENT_PENDING,
            Transaction.Status.SETTLING,
            Transaction.Status.SUBMITTED_FOR_SETTLEMENT
    };

    @Autowired
    public CheckoutController(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ShoppingCartService shoppingCartService, BeerService beerService, UserService userService, NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.shoppingCartService = shoppingCartService;
        this.beerService = beerService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    private void addCheckoutAttributes(RedirectAttributes attributes){
        attributes.addFlashAttribute("beers", shoppingCartService.getBeersInCart());
        attributes.addFlashAttribute("total", shoppingCartService.getTotal().toString());
        attributes.addFlashAttribute("clientToken", gateway.clientToken().generate());
    }

    @GetMapping("/checkout")
    public String getCheckoutPage(Model model, Authentication auth){
        Order order = new Order();

        if(auth!=null) {
            User user = userService.findByEmail(auth.getName());
            if (user != null) {
                order.setUser(user);
                order.setName(user.getFirstName());
                order.setLastName(user.getLastName());
                order.setEmail(user.getEmail());
            }
        }
        model.addAttribute("order", order);
        model.addAttribute("beers", shoppingCartService.getBeersInCart());
        model.addAttribute("total", shoppingCartService.getTotal().toString());
        model.addAttribute("clientToken", gateway.clientToken().generate());
        return "checkout";
    }

    @PostMapping("/checkout")
    public String checkoutOrder(Model model, @RequestParam("amount") String amount, @RequestParam("payment_method_nonce") String nonce, @Valid Order order, BindingResult bindingResult, RedirectAttributes attributes, Authentication auth){
        User user = null;
        if(auth!=null){
            user = userService.findByEmail(auth.getName());
            model.addAttribute("user", user);
        }
        //If delivery details not valid
        if (bindingResult.hasErrors()) {
            model.addAttribute("beers", shoppingCartService.getBeersInCart());
            model.addAttribute("total", shoppingCartService.getTotal().toString());
            model.addAttribute("clientToken", gateway.clientToken().generate());
            return "checkout";
        }
        //Check stock again, returning to checkout page if cart is empty or quantities requested are no longer available
        try {
            shoppingCartService.validateCart();
        } catch (NotEnoughBeersInStockException e) {
            attributes.addFlashAttribute("order", order);
            attributes.addFlashAttribute("outOfStockMessage", e.getMessage());
            addCheckoutAttributes(attributes);
            return "redirect:/checkout";
        } catch (NoBeersInCartException b) {
            attributes.addFlashAttribute("order", order);
            attributes.addFlashAttribute("emptyCartMessage", b.getMessage());
            addCheckoutAttributes(attributes);
            return "redirect:/checkout";
        }

        //attempt to parse param for total for transaction
        BigDecimal decimalAmount;
        try {
            decimalAmount = new BigDecimal(amount);
        } catch (NumberFormatException e) {
            attributes.addFlashAttribute("order", order);
            attributes.addFlashAttribute("paymentErrorMessage","Error: 81503: Total is an invalid format.");
            addCheckoutAttributes(attributes);
            return "redirect:/checkout";
        }

        //build transaction request
        TransactionRequest request = new TransactionRequest()
                .amount(decimalAmount)
                .paymentMethodNonce(nonce)
                .options()
                .submitForSettlement(true)
                .done();

        //attempt transaction request
        Result<Transaction> result = gateway.transaction().sale(request);
        Transaction transaction;

        if (result.isSuccess())transaction = result.getTarget();
        else if(result!=null) transaction = result.getTransaction();
        else {
            String errorString = "";
            for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
                errorString += "Error: " + error.getCode() + ": " + error.getMessage() + "\n";
            }
            attributes.addFlashAttribute("order", order);
            attributes.addFlashAttribute("paymentErrorMessage",errorString);
            addCheckoutAttributes(attributes);
            return "redirect:/checkout";
        }


        //attempt to contact service to check transaction
        try {
            transaction = gateway.transaction().find(transaction.getId());
        } catch (Exception e) {
            attributes.addFlashAttribute("order", order);
            attributes.addFlashAttribute("paymentErrorMessage",e.toString());
            addCheckoutAttributes(attributes);
            return "redirect:/checkout";
        }

        //if transaction was succesful
        if(Arrays.asList(TRANSACTION_SUCCESS_STATUSES).contains(transaction.getStatus())){

            //if currently logged in, link order to user account
            if(user!=null) order.setUser(user);

            //log order
            order.setPaymentRef(transaction.getId());
            orderRepository.save(order);

            //Send notification
            try {
                notificationService.sendNotification(order.getEmail());
            } catch (MailException e){
                //catch error
                System.out.println("Email didn't send. Error: " + e.getMessage());
            }

            //save order items
            for(Map.Entry<Beer, Integer> cartItem  : shoppingCartService.getBeersInCart().entrySet()) orderItemRepository.save(new OrderItem(order, cartItem.getKey(), cartItem.getValue()));

            //clear cart and reduce beer stock
            shoppingCartService.finish();

        }

        //if transaction was unsuccessful
        else{
            attributes.addFlashAttribute("paymentErrorMessage","Braintree Transaction Unsuccessful: "+transaction.getStatus().toString());
            attributes.addFlashAttribute("order", order);
            attributes.addFlashAttribute("beers", shoppingCartService.getBeersInCart());
            attributes.addFlashAttribute("total", shoppingCartService.getTotal().toString());
            attributes.addFlashAttribute("clientToken", gateway.clientToken().generate());
            return "redirect:/checkout";
        }

        attributes.addFlashAttribute("orderId",order.getId().toString());
        attributes.addFlashAttribute("paymentRef",order.getPaymentRef());
        return "redirect:/checkout/complete";
    }


    @GetMapping("/checkout/complete")
    public String orderComplete(Model model) {
        return "complete";
    }
}