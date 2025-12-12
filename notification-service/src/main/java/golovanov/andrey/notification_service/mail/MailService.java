package golovanov.andrey.notification_service.mail;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from:no-reply@your-site.com}")
    private String from;

    @CircuitBreaker(name = "mail", fallbackMethod = "sendAccountCreatedFallback")
    @Retry(name = "mail")
    public void sendAccountCreated(String email) {
        String subject = "Ваш аккаунт создан";
        String text = "Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.";
        sendEmail(email, subject, text);
    }

    @CircuitBreaker(name = "mail", fallbackMethod = "sendAccountDeletedFallback")
    @Retry(name = "mail")
    public void sendAccountDeleted(String email) {
        String subject = "Ваш аккаунт удалён";
        String text = "Здравствуйте! Ваш аккаунт был удалён.";
        sendEmail(email, subject, text);
    }

    @CircuitBreaker(name = "mail", fallbackMethod = "sendCustomFallback")
    @Retry(name = "mail")
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

    private void sendAccountCreatedFallback(String email, Throwable ex) {
        log.error("[MAIL FALLBACK] sendAccountCreated to={}, cause={}", email, ex.toString());
    }

    private void sendAccountDeletedFallback(String email, Throwable ex) {
        log.error("[MAIL FALLBACK] sendAccountDeleted to={}, cause={}", email, ex.toString());
    }

    private void sendCustomFallback(String email, String subject, String text, Throwable ex) {
        log.error("[MAIL FALLBACK] sendCustom to={}, subject={}, cause={}", email, subject, ex.toString());
    }
}