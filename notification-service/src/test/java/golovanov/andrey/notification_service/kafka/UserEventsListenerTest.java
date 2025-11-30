package golovanov.andrey.notification_service.kafka;

import golovanov.andrey.notification_service.dto.UserEvent;
import golovanov.andrey.notification_service.mail.MailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        topics = { "user-events" },
        bootstrapServersProperty = "spring.kafka.bootstrap-servers"
)
class UserEventsListenerTest {

    @Autowired
    private KafkaTemplate<String, UserEvent> userEventsKafkaTemplate;

    @MockBean
    private MailService mailService;

    @Test
    void whenUserCreatedEventSent_thenAccountCreatedEmailIsSent() {
        // given
        UserEvent event = new UserEvent("CREATE", "test@example.com");

        // when
        userEventsKafkaTemplate.send("user-events", event);

        // then
        verify(mailService, timeout(5000))
                .sendAccountCreated("test@example.com");
    }

    @Test
    void whenUserDeletedEventSent_thenAccountDeletedEmailIsSent() {
        // given
        UserEvent event = new UserEvent("DELETE", "test@example.com");

        // when
        userEventsKafkaTemplate.send("user-events", event);

        // then
        verify(mailService, timeout(5000))
                .sendAccountDeleted("test@example.com");
    }
}
