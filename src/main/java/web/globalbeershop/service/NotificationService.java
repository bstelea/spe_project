package web.globalbeershop.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import web.globalbeershop.data.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.Email;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class NotificationService {

    private JavaMailSender javaMailSender;

    @Qualifier("freeMarkerConfiguration")
    @Autowired
    private Configuration freemarkerConfig;

    @Autowired
    public NotificationService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    public void sendNotification(String email) throws MailException {
        //send email
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom("globalbeershopmail@gmail.com");
        mail.setSubject("Order Placed");
        mail.setText("Thank you for your order!");


        javaMailSender.send(mail);
    }

    public void sendOrderCompleteToGBSEmail(Order order) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        String html = "<h1>New order as been placed</h1>" +
                "       <p>A new order from " + order.getName() + " " + order.getLastName() + " has placed a new order.</p>";

        helper.setTo("globalbeershopmail@gmail.com");
        helper.setText(html, true);
        helper.setSubject("Global Beer Shop - New Order");
        helper.setFrom("globalbeershopmail@gmail.com");

        javaMailSender.send(message);
    }

    public void sendRegistrationSuccessfullToUser(String email) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        String html = "<h1>Welcome to Global Beer Shop!</h1>" +
                "       <p>Thank you for creating an account on our website!</p>";

        helper.setTo(email);
        helper.setText(html, true);
        helper.setSubject("Welcome to Global Beer Shop!");
        helper.setFrom("globalbeershopmail@gmail.com");

        javaMailSender.send(message);
    }

    public void sendRegistrationSuccessfullToGBS(User user) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        String html = "<h1>New user on Global Beer Shop</h1>" +
                "       <p>A new user has created an account on Global Beer Shop.</p>" +
                "       <p>His details are:</p>" +
                "<ul>" +
                "<li> First Name: " + user.getFirstName() + "</li>" +
                "<li> Last Name: " + user.getLastName() + "</li>" +
                "<li> Email Address: " + user.getEmail() + "</li>" +
                "</ul>";

        helper.setTo("globalbeershopmail@gmail.com");
        helper.setText(html, true);
        helper.setSubject("Global Beer Shop - New User");
        helper.setFrom("globalbeershopmail@gmail.com");

        javaMailSender.send(message);
    }

    public void sendContactToUser(FormDTO form) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        String html = "<h1>Thank you for your feedback!</h1>" +
                "<p>We are going to reply to you ASAP!</p>";

        helper.setTo(form.getEmail());
        helper.setText(html, true);
        helper.setSubject("Global Beer Shop - Contact Form Submitted");
        helper.setFrom("globalbeershopmail@gmail.com");

        javaMailSender.send(message);
    }

    public void sendContactToGBS(FormDTO form) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        String html = "<h1>New Contact Form Submitted</h1>" +
                "       <p>A new contact form has been submitted on Global Beer Shop.</p>" +
                "       <p>Contents of the message:</p>" +
                "<ul>" +
                "<li> First Name: " + form.getFirstName() + "</li>" +
                "<li> Last Name: " + form.getLastName() + "</li>" +
                "<li> Email Address: " + form.getEmail() + "</li>" +
                "<li> Message: " + form.getComments() + "</li>" +
                "</ul>";

        helper.setTo("globalbeershopmail@gmail.com");
        helper.setText(html, true);
        helper.setSubject("Global Beer Shop - New Contact Form");
        helper.setFrom("globalbeershopmail@gmail.com");

        javaMailSender.send(message);
    }

    public void sendActivationEmail(ActivationToken token) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        User user = token.getUser();

        String html = "<br><h4>Hi "+ user.getFirstName() +",</h4>" +
                "<br><a href=\"https://globalbeershop.spe.cs.bris.ac.uk/register/activate?token="+token.getToken()+"\">Click Here to activate your account</a>" +
                "<br><h4>Global Beer Shop</h4>";

        helper.setTo(user.getEmail());
        helper.setText(html, true);
        helper.setSubject("Global Beer Shop - Activate your account");
        helper.setFrom("globalbeershopmail@gmail.com");

        javaMailSender.send(message);
    }

    public void sendResetEmail (ResetToken token) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
        User user = token.getUser();

        String activationUrl;

        String htmlMsg = "<br><h4>Hi "+ user.getFirstName() +",</h4>" +
                "<br><a href=\"http://localhost:8080/reset/set?token="+token.getToken()+"\">Click Here to reset your account password</a>" +
                "<br><h4>Global Beer Shop</h4>";
        mimeMessage.setContent(htmlMsg, "text/html");
        helper.setTo(token.getUser().getEmail());
        helper.setSubject("Reset your Global Beer Shop password");
        helper.setFrom("globalbeershopmail@gmail.com");
        javaMailSender.send(mimeMessage);
    }

    public void sendBeautifulMail(Mail mail) throws MessagingException, IOException, TemplateException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        Template t = freemarkerConfig.getTemplate("user-order-complete-email.ftl");
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, mail.getModel());

        helper.setTo(mail.getTo());
        helper.setText(html, true);
        helper.setSubject(mail.getSubject());
        helper.setFrom(mail.getFrom());

        javaMailSender.send(message);
    }

}
