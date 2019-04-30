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
                "       <p>A new order from " + order.getName() + " " + order.getLastName() + " has been placed</p>" ;

        helper.setTo("globalbeershopmail@gmail.com");
        helper.setText(html, true);
        helper.setSubject("Global Beer Shop - New Order");
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
