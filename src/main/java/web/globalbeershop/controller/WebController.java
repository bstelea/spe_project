package web.globalbeershop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.globalbeershop.data.*;
import web.globalbeershop.repository.ActivationTokenRepository;
import web.globalbeershop.repository.ResetTokenRepository;
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
    ResetTokenRepository resetTokenRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    NotificationService notificationService;


    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login/error")
    public String loginError(Model model, RedirectAttributes attributes) {
        attributes.addFlashAttribute("errorMessage", "Wrong Email or Password");
        return "redirect:/login";
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
        if (!userDTO.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")) {
            bindingResult.rejectValue("password", "error.user", "MUST contain at least one number, one upper-case letter and 8 characters in total. MUST NOT contain any spaces or tabs.");
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
            modelAndView.setViewName("register");
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
                modelAndView.setViewName("login");
                return modelAndView;
            }

            modelAndView.setViewName("login");
            modelAndView.addObject("successMessage", "We have sent you an e-mail with a link to activate your account");
            modelAndView.addObject("errorMessage", "You cannot login until you have activated your account");
        }
        return modelAndView;
    }

    @GetMapping("/register/activate")
    public String newUser(Model model, @RequestParam(value = "token", required = true) String token,  RedirectAttributes attributes) {
        ActivationToken activationToken = activationTokenRepository.findByToken(token);
        if(activationToken!=null){
            User user = activationToken.getUser();
            if(!activationToken.isExpired()){
                user.setEnabled(true);
                userRepository.save(user);
                activationTokenRepository.delete(activationToken);
                attributes.addFlashAttribute("successMessage", "Your account has been activated, you can now login");

                try {
                    notificationService.sendRegistrationSuccessfullToUser(user.getEmail());
                    notificationService.sendRegistrationSuccessfullToGBS(user);
                } catch (MessagingException m) {
                    System.out.println("Confirmation email failed to be sent " + m.getMessage());
                }
            }
            else {
                attributes.addFlashAttribute("errorMessage", "Your activation link has expired, we have sent a new one to the same e-mail address");
                try {
                    notificationService.sendActivationEmail(activationToken);
                } catch (MessagingException e) {
                    attributes.addFlashAttribute("errorMessage", "There is something wrong with the e-mail address for your account, please try creating your account again");
                    userRepository.delete(user);
                    activationTokenRepository.delete(activationToken);
                    return "redirect:/login";
                }
            }

        }else{
            attributes.addFlashAttribute("errorMessage", "Not valid request");
        }
        return "redirect:/login";
    }

    @GetMapping("/reset/get")
    public String getResetPage(){
        return "get-reset";
    }

    @PostMapping("/reset/get")
    public String sendResetEmail (Model model, @RequestParam(value = "email", required = true) String email, RedirectAttributes attributes) {

        User user = userRepository.findByEmail(email);
        if(user!=null){
            ResetToken token = new ResetToken();
            token.setUser(user);
            resetTokenRepository.save(token);

            try {
                notificationService.sendResetEmail(token);
            } catch (MessagingException e) {
                resetTokenRepository.delete(token);
                attributes.addFlashAttribute("errorMessage", "An error occurred, please try again later");
                return "redirect:/reset/get";
            }
        }

        attributes.addFlashAttribute("successMessage", "If such account exists, we have sent that email a Password Reset link");
        return "redirect:/reset/get";
    }

    @GetMapping("/reset/set")
    public String resetPassword(Model model, @RequestParam(value = "token", required = true) String token, RedirectAttributes attributes) {
        ResetToken resetToken = resetTokenRepository.findByToken(token);

        if(resetToken!=null) {
            User user = resetToken.getUser();
            if (!resetToken.isExpired()) {
                model.addAttribute("email", user.getEmail());
                return "set-reset";

            } else {
                resetTokenRepository.delete(resetToken);
                attributes.addFlashAttribute("errorMessage", "Invalid request");
                return "redirect:/login";
            }
        }

        else {
            attributes.addFlashAttribute("errorMessage", "Invalid request");
            resetTokenRepository.delete(resetToken);
            return "redirect:/login";
        }
    }


    @PostMapping("/reset/set")
    public String setNewPassword (Model model, @RequestParam(value = "email", required = true) String email,
                                  @RequestParam(value = "password", required = true) String password,
                                  @RequestParam(value = "matchingPassword", required = true) String matchingPassword,
                                  RedirectAttributes attributes) {

        //check if password meets requirements
        if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=?!#(){}])(?=\\S+$).{8,}$")) {
            model.addAttribute("errorMessage", "The password must contain at least 8 characters, contain at least one digit, contain at least one lower and one upper case alphabetic characters, contain at least one special symbol (@#%$^ etc.) and does not contain space or a tab, etc.");
            model.addAttribute("email", email);

        }
        //checks if passwords match
        else if (!password.equals(matchingPassword) ){
            model.addAttribute("errorMessage", "Passwords do not match");
            model.addAttribute("email", email);
        }

        else {
            User user = userRepository.findByEmail(email);
            if (user != null) {
                resetTokenRepository.deleteAll(resetTokenRepository.findAll(QResetToken.resetToken.user.eq(user)));
                userService.enableUser(userService.setPassword(user, password));
                attributes.addFlashAttribute("successMessage", "Password Reset");

            } else {
                attributes.addFlashAttribute("errorMessage", "Something went wrong, please try again later");
            }

            return "redirect:/login";
        }
        return "set-reset";
    }
}
