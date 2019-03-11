package web.globalbeershop.controller;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import web.globalbeershop.data.Order;
import web.globalbeershop.data.User;
import web.globalbeershop.exception.NoBeersInCartException;
import web.globalbeershop.exception.NotEnoughBeersInStockException;
import web.globalbeershop.repository.OrderRepository;
import web.globalbeershop.service.BeerService;
import web.globalbeershop.service.NotificationService;
import web.globalbeershop.service.ShoppingCartService;

import javax.management.Notification;
import javax.validation.Valid;
import java.util.logging.Logger;

@Controller
public class CheckoutController {

    @Autowired
    OrderRepository orderRepository;

    private NotificationService notificationService;

    private final ShoppingCartService shoppingCartService;

    private final BeerService beerService;

    @Autowired
    public CheckoutController(ShoppingCartService shoppingCartService, BeerService beerService, NotificationService notificationService) {
        this.shoppingCartService = shoppingCartService;
        this.beerService = beerService;
        this.notificationService = notificationService;
    }

    private Logger logger = Logger.getLogger(String.valueOf(CheckoutController.class));

    @GetMapping("/checkout")
    public ModelAndView checkout() {

        ModelAndView modelAndView = new ModelAndView();
        Order order = new Order();
        modelAndView.addObject("order", order);
        modelAndView.setViewName("/checkout");
        return modelAndView;
    }

    @GetMapping("/checkout/payment")
    public String getPaymentPage(Model model) {
        return "payment";
    }

    @PostMapping("/checkout/submitDelivery")
    public String submitDeliveryDetails(Model model, @Valid Order order, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "redirect:/checkout";
        }

        model.addAttribute("order", order);

        return "redirect:/checkout/payment";
    }

    @PostMapping("/checkout/complete")
    public ModelAndView logOrder(@Valid Order order, BindingResult bindingResult) {

        ModelAndView modelAndView = new ModelAndView();

        try {
            shoppingCartService.finish();
        } catch (NotEnoughBeersInStockException e) {
            return checkout().addObject("outOfStockMessage", e.getMessage());
        } catch (NoBeersInCartException b) {
            return checkout().addObject("emptyCartMessage", b.getMessage());
        }

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("/checkout");
        } else {
            // Valid order delivery details

            //Send notification
            try {
                notificationService.sendNotification("globalbeershopmail@gmail.com");
//                notificationService.sendNotification(order.getEmail());
            } catch (MailException e){
                //catch error
                logger.info("Email didn't send. Error: " + e.getMessage());
            }

            modelAndView.addObject("successMessage", "User checkout successful");
            modelAndView.addObject("order", new Order());
            modelAndView.setViewName("/payment");
        }
        return modelAndView;
    }
}
