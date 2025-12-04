package golovanov.andrey.notification_service.web;

import golovanov.andrey.notification_service.dto.SendMailRequest;
import golovanov.andrey.notification_service.mail.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<Void> sendNotification(@Valid @RequestBody SendMailRequest request) {
        switch (request.type()) {
            case "CREATE" -> mailService.sendAccountCreated(request.email());
            case "DELETE" -> mailService.sendAccountDeleted(request.email());
            default ->
                throw new IllegalArgumentException("Unknown type: " + request.type());
        }
        return ResponseEntity.ok().build();
    }
}
