package web.globalbeershop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import web.globalbeershop.data.ActivationToken;
import web.globalbeershop.data.User;
import web.globalbeershop.data.UserDTO;
import web.globalbeershop.repository.ActivationTokenRepository;
import web.globalbeershop.repository.UserRepository;
import web.globalbeershop.service.NotificationService;
import web.globalbeershop.service.UserService;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.Date;

@Controller
public class WebController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ActivationTokenRepository activationTokenRepository;

    @Autowired
    NotificationService notificationService;


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
        if (!userDTO.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=?!#(){}])(?=\\S+$).{8,}$")) {
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
            User user = userService.save(userDTO);

            ActivationToken token = new ActivationToken();
            token.setUser(user);
            activationTokenRepository.save(token);

            try {
                notificationService.sendActivationEmail(token);
            } catch (MessagingException e) {
                modelAndView.addObject("errorMessage", "There is something wrong with the e-mail address for your account, please try creating your account again");
                userRepository.delete(user);
                activationTokenRepository.delete(token);
                modelAndView.setViewName("/login");
                return modelAndView;
            }

            modelAndView.setViewName("/login");
            modelAndView.addObject("successMessage", "We have sent you an e-mail with a link to activate your account");
            modelAndView.addObject("errorMessage", "You cannot login until you have activated your account");
        }
        return modelAndView;
    }

    @GetMapping("/register/activate")
    public String newUser(Model model, @RequestParam(value = "token", required = true) String token) {
        ActivationToken activationToken = activationTokenRepository.findByToken(token);
        if(activationToken!=null){
            User user = activationToken.getUser();
            Date currentTime = new Date();
            if(activationToken.getExpiryDate().after(currentTime)){
                user.setEnabled(true);
                userRepository.save(user);
                activationTokenRepository.delete(activationToken);
                model.addAttribute("successMessage", "Your account has been activated, you can now login");
            }
            else {
                model.addAttribute("errorMessage", "Your activation link has expired, we have sent a new one to the same e-mail address");
                try {
                    notificationService.sendActivationEmail(activationToken);
                } catch (MessagingException e) {
                    model.addAttribute("errorMessage", "There is something wrong with the e-mail address for your account, please try creating your account again");
                    userRepository.delete(user);
                    activationTokenRepository.delete(activationToken);
                    return "/login";
                }
            }

        }else{
            model.addAttribute("errorMessage", "Not valid request");
        }
        return "/login";
    }
}
