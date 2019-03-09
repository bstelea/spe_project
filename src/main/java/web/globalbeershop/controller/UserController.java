package web.globalbeershop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import web.globalbeershop.data.Order;
import web.globalbeershop.data.QBeer;
import web.globalbeershop.data.QOrder;
import web.globalbeershop.data.User;

import web.globalbeershop.repository.BeerRepository;
import web.globalbeershop.repository.OrderRepository;
import web.globalbeershop.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    OrderRepository orderRepository;

    @GetMapping("/user")
    public String home(Model model, Authentication auth){
        User user = userService.findByEmail(auth.getName());
        model.addAttribute("user", user);
        model.addAttribute("greeting", "Welcome, " + user.getFirstName());
        return "user_index";
    }

    @GetMapping("/user/orders")
    public String getAllOrders (Model model, Authentication auth){
        User user = userService.findByEmail(auth.getName());
        model.addAttribute("user", user);
        model.addAttribute("orders", orderRepository.findAll(QOrder.order.user.eq(user), new Sort(Sort.Direction.DESC, "date")));
        return "user_orders";
    }

    @GetMapping("/user/orders/{orderId}")
    public String getOrder (Model model, Authentication auth, @PathVariable Long orderId) {
        User user = userService.findByEmail(auth.getName());
        for(Order order : orderRepository.findAll(QOrder.order.id.eq(orderId).and(QOrder.order.user.eq(user)))){
            model.addAttribute("order", order);
            return "user_order";
        }
        return "redirect:/user/orders";
    }

    @GetMapping("/user/details")
    public String getDetails (Model model,Authentication auth){
        User user = userService.findByEmail(auth.getName());
        model.addAttribute("user", user);
        return "user_details";
    }

    @PostMapping("/user/details")
    public String setDetails (Model model, Authentication authentication){
        return "redirect:/user_details";
    }









}
