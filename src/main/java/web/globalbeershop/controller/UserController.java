package web.globalbeershop.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
    @ResponseBody
    public String home(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(auth.getName());
        return user.getEmail();
    }


    @GetMapping("/user/orders")
    public String getAllOrders (Model model, Authentication authentication){
        return "user_orders";
    }

    @GetMapping("/user/orders/{orderId}")
    public String getOrder (Model model, Authentication authentication, @PathVariable Long orderId) {
        User user = (User) authentication.getPrincipal();
        for(Order order : orderRepository.findAll(QOrder.order.id.eq(orderId).and(QOrder.order.user.eq(user)))){
            model.addAttribute("order", order);
            return "user_order";
        }
        model.addAttribute("userHasNoSuchOrder", true);
        return "user_order";
    }

    @GetMapping("/details")
    public String getDetails (Model model, Authentication authentication){
        model.addAttribute ("user", (User) authentication.getPrincipal());
        return "user_details";
    }

    @PostMapping("/user/details")
    public String setDetails (Model model, Authentication authentication){
        return "redirect:/user_details";
    }









}
