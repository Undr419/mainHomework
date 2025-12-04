package golovanov.andrey.user_service.user.service;

import golovanov.andrey.user_service.user.event.UserEvent;
import golovanov.andrey.user_service.user.model.User;
import golovanov.andrey.user_service.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceKafkaIntegrationTest {

    @Autowired
    private UserService userService; // это твой UserServiceImpl

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private KafkaTemplate<String, UserEvent> userEventKafkaTemplate;

    @Test
    void whenCreateUser_thenKafkaEventWithCreateIsSent() {
        // given
        User user = new User("John", "john@example.com", 30);

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        userService.createUser(user);

        // then
        ArgumentCaptor<UserEvent> eventCaptor = ArgumentCaptor.forClass(UserEvent.class);

        verify(userEventKafkaTemplate, times(1))
                .send(eq("user-events"), eventCaptor.capture());

        UserEvent event = eventCaptor.getValue();
        assertThat(event.getOperation()).isEqualTo("CREATE");
        assertThat(event.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void whenDeleteUser_thenKafkaEventWithDeleteIsSent() {
        // given
        Long userId = 1L;
        User existing = new User("John", "john@example.com", 30);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existing));

        // when
        userService.deleteUser(userId);

        // then
        ArgumentCaptor<UserEvent> eventCaptor = ArgumentCaptor.forClass(UserEvent.class);

        verify(userEventKafkaTemplate, times(1))
                .send(eq("user-events"), eventCaptor.capture());

        UserEvent event = eventCaptor.getValue();
        assertThat(event.getOperation()).isEqualTo("DELETE");
        assertThat(event.getEmail()).isEqualTo("john@example.com");
    }
}
