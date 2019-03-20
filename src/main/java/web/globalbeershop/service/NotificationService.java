package web.globalbeershop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import web.globalbeershop.data.ActivationToken;
import web.globalbeershop.data.Order;
import web.globalbeershop.data.ResetToken;
import web.globalbeershop.data.User;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.Email;

@Service
public class NotificationService {

    private JavaMailSender javaMailSender;

    @Autowired
    public NotificationService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    public void sendNotification(Order order) throws MessagingException {
        //send email
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");

        String activationUrl;

        String htmlMsg = "<br><h4>Thanks you for shopping with us "+ order.getName() +",</h4>" +
                "<br><p>Your order will arrive with you shortly.</p>" +
                "<br><h4>Global Beer Shop</h4>";
        mimeMessage.setContent(htmlMsg, "text/html");
        helper.setTo(order.getEmail());
        helper.setSubject("Order Confirmation");
        helper.setFrom("globalbeershopmail@gmail.com");
        javaMailSender.send(mimeMessage);
    }

    public void sendActivationEmail(ActivationToken token) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
        User user = token.getUser();

        String activationUrl;

        String htmlMsg = "<br><h4>Hi "+ user.getFirstName() +",</h4>" +
                        "<br><a href=\"http://localhost:8080/register/activate?token="+token.getToken()+"\">Click Here to activate your account</a>" +
                        "<br><h4>Global Beer Shop</h4>";
        mimeMessage.setContent(htmlMsg, "text/html");
        helper.setTo(token.getUser().getEmail());
        helper.setSubject("Activate your Global Beer Shop account");
        helper.setFrom("globalbeershopmail@gmail.com");
        javaMailSender.send(mimeMessage);
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

}
