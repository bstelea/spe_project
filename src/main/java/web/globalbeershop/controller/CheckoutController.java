package web.globalbeershop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import web.globalbeershop.data.Order;
import web.globalbeershop.repository.OrderRepository;

import javax.validation.Valid;

@Controller
public class CheckoutController {

    @Autowired
    OrderRepository orderRepository;

    @GetMapping("/checkout")
    public String getCheckoutPage(Model model) {
        model.addAttribute("order", new Order());
        return "checkout";
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

    @PostMapping("/checkout/submitPayment")
    public String submitPayment(Model model, @Valid Order order) {

        // Successful checkout
        orderRepository.save(order);

        model.addAttribute("successMessage", "User checkout successful");
        model.addAttribute("order", new Order());

        return "redirect:/checkout";
    }
}
