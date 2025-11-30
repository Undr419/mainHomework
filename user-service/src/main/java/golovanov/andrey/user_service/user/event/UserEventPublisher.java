package golovanov.andrey.user_service.user.event;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventPublisher {

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    private static final String TOPIC = "user-events";

    public void publishUserCreated(String email) {
        UserEvent event = new UserEvent("CREATE", email);
        kafkaTemplate.send(TOPIC, event);
    }

    public void publishUserDeleted(String email) {
        UserEvent event = new UserEvent("DELETE", email);
        kafkaTemplate.send(TOPIC, event);
    }
}
