package web.globalbeershop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import web.globalbeershop.data.*;

import web.globalbeershop.repository.BeerRepository;
import web.globalbeershop.repository.OrderRepository;
import web.globalbeershop.repository.ReviewRepository;
import web.globalbeershop.repository.UserRepository;
import web.globalbeershop.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ReviewRepository reviewRepository;

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
    public String setDetails (Model model, @RequestParam(value = "firstName", required = false) String firstName,
                                            @RequestParam(value = "lastName", required = false) String lastName,
                                             Authentication auth)
    {
        User user = userService.findByEmail(auth.getName());
        if(firstName != "" && firstName!=null) user.setFirstName(firstName);
        else{
            model.addAttribute("errorMessage", "Invalid details");
            model.addAttribute("user", user);
            return "user_details";

        }
        if(lastName !=  "" && lastName!=null) user.setLastName(lastName);
        else{
            model.addAttribute("errorMessage", "Invalid details");
            model.addAttribute("user", user);
            return "user_details";

        }

        userRepository.save(user);
        model.addAttribute("successMessage", "Your details have been updated");
        model.addAttribute("user", user);
        return "user_details";
    }

    @GetMapping("/user/reviews")
    public String getReviews (Model model,Authentication auth){
        User user = userService.findByEmail(auth.getName());
        model.addAttribute("user", user);

        HashMap<Long, Beer> products = new HashMap<>();
        for(Order order : orderRepository.findAll(QOrder.order.user.eq(user))){
            for(OrderItem item : order.getItems()){
                products.put(item.getBeer().getId(), item.getBeer());
            }
        }
        model.addAttribute("products", products);
        if(products.entrySet().isEmpty())  model.addAttribute("errorMessage", "You haven't purchased any beers yet to review!");
        model.addAttribute("review", new Review());



        return "user_reviews";
    }

    @PostMapping("/user/reviews")
    public String postReviews (Model model, Authentication auth, @Valid Review review, BindingResult result){
        return "redirect:/user_reviews";
    }












}
