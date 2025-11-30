package golovanov.andrey.notification_service.web;

import golovanov.andrey.notification_service.dto.SendMailRequest;
import golovanov.andrey.notification_service.mail.MailService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final MailService mailService;

    public NotificationController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendNotification(@Valid @RequestBody SendMailRequest request) {
        switch (request.getType()) {
            case "CREATE" -> mailService.sendAccountCreated(request.getEmail());
            case "DELETE" -> mailService.sendAccountDeleted(request.getEmail());
            default -> {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.ok().build();
    }
}
