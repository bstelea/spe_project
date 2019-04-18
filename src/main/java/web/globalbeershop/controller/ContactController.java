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

    @PostMapping("/")
    public String formHandler(@Valid FormDTO formDTO) {

        Mail mail = new Mail();
        mail.setFrom("globalbeershopmail@gmail.com");
        mail.setTo("globalbeershopmail@gmail.com");
        mail.setSubject("New contact form message");

        Map model = new HashMap();
        model.put("name", formDTO.getFirstName());
        mail.setModel(model);

        try {
            notificationService.sendBeautifulMail(mail);
        } catch (MessagingException e) {
            System.out.println("Messaging exception error " + e.getMessage());
        } catch (IOException io) {
            System.out.println("IO exception error " + io.getMessage());
        } catch (TemplateException t) {
            System.out.println("Template exception error " + t.getMessage());
        }

        return "redirect:/shop";
    }
}
