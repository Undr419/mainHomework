package golovanov.andrey.notification_service.kafka;

import golovanov.andrey.notification_service.dto.UserEvent;
import golovanov.andrey.notification_service.mail.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventsListener {

    private final MailService mailService;

    @KafkaListener(
            topics = "user-events",
            groupId = "notification-service",
            containerFactory = "userEventsKafkaListenerContainerFactory"
    )
    public void handleUserEvent(UserEvent event) {
        log.info("Received user event: {}", event);

        if (event.getOperation() == null || event.getEmail() == null) {
            log.warn("Invalid event: {}", event);
            return;
        }

        switch (event.getOperation()) {
            case "CREATE" -> mailService.sendAccountCreated(event.getEmail());
            case "DELETE" -> mailService.sendAccountDeleted(event.getEmail());
            default -> log.warn("Unknown operation: {}", event.getOperation());
        }
    }
}

