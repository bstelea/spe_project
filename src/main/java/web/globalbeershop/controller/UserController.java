package web.globalbeershop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.globalbeershop.data.*;

import web.globalbeershop.repository.*;
import web.globalbeershop.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
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
    OrderItemRepository orderItemRepository;

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
    public String getAllOrders (Model model, Authentication auth, @RequestParam(value = "orderId", required = false) Long orderId,
                                RedirectAttributes attributes) {
        User user = userService.findByEmail(auth.getName());

        //view all orders
        if(orderId == null){
            model.addAttribute("user", user);
            model.addAttribute("orders", orderRepository.findAll(QOrder.order.user.eq(user), new Sort(Sort.Direction.DESC, "date")));
            return "user_orders";
        }
        //view given order
        else{
            for(Order order : orderRepository.findAll(QOrder.order.id.eq(orderId).and(QOrder.order.user.eq(user)))){
                Double total = 0.0;
                for(OrderItem item : order.getItems()) total+=item.getBeer().getPrice()*item.getQuantity();
                model.addAttribute("order", order);
                model.addAttribute("total", total);
                return "user_order";
            }
            attributes.addFlashAttribute("errorMessage", "You have no such order placed");
            return "redirect:/user/orders";
        }

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
        //for all items user has ordered before
        for(OrderItem item : orderItemRepository.findAll(QOrderItem.orderItem.order.user.eq(user))){
            products.put(item.getBeer().getId(), item.getBeer());
        }

        model.addAttribute("products", products);
        if(products.entrySet().isEmpty())  model.addAttribute("errorMessage", "You haven't purchased any beers recently to review!");
        model.addAttribute("review", new Review());



        return "user_reviews";
    }

    @PostMapping("/user/reviews")
    public String postReviews (Model model, Authentication auth, @Valid Review review, BindingResult result, RedirectAttributes attributes){

        if(result.hasErrors()) return "user_reviews";

        review.setUser(userService.findByEmail(auth.getName()));
        reviewRepository.save(review);
        attributes.addFlashAttribute("successMessage", "Review submitted!");
        return "redirect:/user/reviews";
    }












}
