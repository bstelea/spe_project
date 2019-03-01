package web.globalbeershop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import web.globalbeershop.GlobalbeershopApplication;
import web.globalbeershop.data.Order;
import web.globalbeershop.exception.NoBeersInCartException;
import web.globalbeershop.exception.NotEnoughBeersInStockException;
import web.globalbeershop.repository.OrderRepository;
import web.globalbeershop.service.BeerService;
import web.globalbeershop.service.ShoppingCartService;
import java.math.BigDecimal;
import java.util.Arrays;

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

import javax.validation.Valid;

@Controller
public class CheckoutController {


    @Autowired
    OrderRepository orderRepository;

    private final ShoppingCartService shoppingCartService;

    private final BeerService beerService;

    private BraintreeGateway gateway = GlobalbeershopApplication.gateway;

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
    public CheckoutController(ShoppingCartService shoppingCartService, BeerService beerService) {
        this.shoppingCartService = shoppingCartService;
        this.beerService = beerService;
    }

    @ModelAttribute("order")
    public Order order(){
        return new Order();
    }

    @GetMapping("/checkout/delivery")
    public String checkout(Model model) {
        model.addAttribute("order", new Order());
        return "checkout";
    }

    @GetMapping("/checkout/payment")
    public String getPaymentPage(Model model) {
        model.addAttribute("clientToken", gateway.clientToken().generate());
        model.addAttribute("amount", shoppingCartService.getTotal());
        return "payment";
    }

    @PostMapping("/checkout/delivery/submit")
    public String submitDeliveryDetails(@Valid Order order, BindingResult bindingResult, RedirectAttributes attributes) {
        ModelAndView modelAndView =  new ModelAndView();

        if (bindingResult.hasErrors()) return "checkout";

        attributes.addFlashAttribute("order", order);
        return "redirect:/checkout/payment";
    }

    @PostMapping("/checkout/payment/submit")
    public RedirectView submitPayment(@RequestParam("amount") String amount, @RequestParam("payment_method_nonce") String nonce, RedirectAttributes attributes) {


        //check stock before trying payment
        try {
            shoppingCartService.finish();
        } catch (NotEnoughBeersInStockException e) {
            attributes.addFlashAttribute("outOfStockMessage", e.getMessage());
            return new RedirectView("/checkout/delivery");
        } catch (NoBeersInCartException b) {
            attributes.addFlashAttribute("emptyCartMessage", b.getMessage());
            return new RedirectView("/checkout/delivery");
        }


        BigDecimal decimalAmount;
        try {
            decimalAmount = new BigDecimal(amount);
        } catch (NumberFormatException e) {
            attributes.addFlashAttribute("paymentErrorMessage","Error: 81503: Amount is an invalid format.");
            return new RedirectView("/checkout/delivery");
        }

        TransactionRequest request = new TransactionRequest()
                .amount(decimalAmount)
                .paymentMethodNonce(nonce)
                .options()
                .submitForSettlement(true)
                .done();

        //attempt payment
        Result<Transaction> result = gateway.transaction().sale(request);

        if (result.isSuccess()) {

            attributes.addFlashAttribute("transaction", result.getTarget());
            return new RedirectView("/checkout/complete");
        } else if (result.getTransaction() != null) {
            attributes.addFlashAttribute("transaction", result.getTarget());
            return new RedirectView("/checkout/complete");
        } else {
            String errorString = "";
            for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
                errorString += "Error: " + error.getCode() + ": " + error.getMessage() + "\n";
            }
            attributes.addFlashAttribute("paymentErrorMessage",errorString);
            return new RedirectView("/checkout/delivery");
        }
    }

    @GetMapping("/checkout/complete")
    public String orderComplete(Model model, @ModelAttribute("order") Order order, @ModelAttribute("transaction") Transaction transaction) {

        //LOG ORDER AND EMAIL SERVICE
        orderRepository.save(order);

        model.addAttribute("successMessage", "User checkout successful");
        model.addAttribute("order", new Order());
        return "checkout";
    }
}
