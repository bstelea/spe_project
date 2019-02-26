package web.globalbeershop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import web.globalbeershop.data.Order;
import web.globalbeershop.exception.NoBeersInCartException;
import web.globalbeershop.exception.NotEnoughBeersInStockException;
import web.globalbeershop.repository.OrderRepository;
import web.globalbeershop.service.BeerService;
import web.globalbeershop.service.ShoppingCartService;

import javax.validation.Valid;

@Controller
public class CheckoutController {

    @Autowired
    OrderRepository orderRepository;

    private final ShoppingCartService shoppingCartService;

    private final BeerService beerService;

    @Autowired
    public CheckoutController(ShoppingCartService shoppingCartService, BeerService beerService) {
        this.shoppingCartService = shoppingCartService;
        this.beerService = beerService;
    }

    @GetMapping("/checkout/delivery")
    public ModelAndView checkout() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("order", new Order());
        modelAndView.setViewName("/delivery");
        return modelAndView;
    }

    @GetMapping("/checkout/payment")
    public String getPaymentPage(Model model) {
        return "payment";
    }

    @PostMapping("/checkout/delivery/submit")
    public String submitDeliveryDetails(Model model, @Valid Order order, BindingResult bindingResult) {

        if (bindingResult.hasErrors())  return "redirect:/checkout/delivery";
        
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

            modelAndView.addObject("successMessage", "User checkout successful");
            modelAndView.addObject("order", new Order());
            modelAndView.setViewName("/payment");
        }
        return modelAndView;
    }
}
