package web.globalbeershop.controller;

import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import web.globalbeershop.data.FormDTO;
import web.globalbeershop.data.Mail;
import web.globalbeershop.service.NotificationService;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ContactController {

    @Autowired
    NotificationService notificationService;

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("formDTO", new FormDTO());
        return "contact";
    }

    @PostMapping("/contact")
    public String formHandler(@Valid FormDTO formDTO) {

        try {
            notificationService.sendContactToUser(formDTO);
            notificationService.sendContactToGBS(formDTO);
        } catch (MessagingException m) {
            System.out.println("Error in sending contact mail " + m.getMessage());
        }

        return "redirect:/";
    }
}
