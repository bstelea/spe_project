package web.globalbeershop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    public ModelAndView checkout() {
        ModelAndView modelAndView = new ModelAndView();
        Order order = new Order();
        modelAndView.addObject("order", order);
        modelAndView.setViewName("/checkout");
        return modelAndView;
    }

    @PostMapping("/checkout")
    public ModelAndView logOrder(@Valid Order order, BindingResult bindingResult) {

        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("/checkout");
        } else {
            // Successful checkout
            orderRepository.save(order);

            modelAndView.addObject("successMessage", "User checkout successful");
            modelAndView.addObject("order", new Order());
            modelAndView.setViewName("/checkout");
        }
        return modelAndView;
    }
}
