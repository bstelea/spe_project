package web.globalbeershop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import web.globalbeershop.data.Order;
import web.globalbeershop.data.User;
import web.globalbeershop.data.UserDTO;
import web.globalbeershop.repository.UserRepository;
import web.globalbeershop.service.UserService;

import javax.validation.Valid;

@Controller
public class WebController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;


    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login/error")
    public String loginError(Model model) {
        model.addAttribute("errorMessage", "Wrong Email or Password");
        return "login";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/register")
    public String newUser(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "register";
    }

    @PostMapping("/register")
    public ModelAndView createNewUser(@Valid UserDTO userDTO, BindingResult bindingResult) {

        ModelAndView modelAndView = new ModelAndView();

        //check if password meets requirements
        if (!userDTO.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
            bindingResult.rejectValue("password", "error.user", "The password must contain at least 8 characters, contain at least one digit, contain at least one lower and one upper case alphabetic characters, contain at least one special symbol (@#%$^ etc.) and does not contain space or a tab, etc.");
        }

        //check if email valid (standard spring Email valid annotation is not very good)
        if (!userDTO.getEmail().matches(
                "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@"+"[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$"
        )) {
            bindingResult.rejectValue("email", "error.user", "Please enter valid email address");
        }

        //checks if email already in use
        if(userRepository.findByEmail(userDTO.getEmail()) != null){
            bindingResult.rejectValue("email", "error.user", "This email is already used");
        }
        //checks if passwords match
        if(!userDTO.getPassword().equals(userDTO.getMatchingPassword())){
            bindingResult.rejectValue("matchingPassword", "error.user", "The passwords do not match");
        }

        //checks if any other invalid data given
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("/register");
        }
        else{
            userService.save(userDTO);
            modelAndView.setViewName("/login");
            modelAndView.addObject("successMessage", "Account created, you can now login");
        }

        return modelAndView;
    }
}
