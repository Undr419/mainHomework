package golovanov.andrey.notification_service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from:no-reply@your-site.com}")
    private String from;

    public void sendAccountCreated(String email) {
        String subject = "Ваш аккаунт создан";
        String text = "Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.";
        sendEmail(email, subject, text);
    }

    public void sendAccountDeleted(String email) {
        String subject = "Ваш аккаунт удалён";
        String text = "Здравствуйте! Ваш аккаунт был удалён.";
        sendEmail(email, subject, text);
    }

    public void sendCustom(String email, String subject, String text) {
        sendEmail(email, subject, text);
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);

        mailSender.send(msg);
    }
}
