package golovanov.andrey.notification_service.web;

import golovanov.andrey.notification_service.mail.MailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MailService mailService;

    @Test
    void whenCreateType_thenSendAccountCreatedIsCalled() throws Exception {
        String json = """
                {
                  "email": "test@example.com",
                  "type": "CREATE"
                }
                """;

        mockMvc.perform(post("/api/v1/notifications/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(mailService).sendAccountCreated("test@example.com");
    }

    @Test
    void whenDeleteType_thenSendAccountDeletedIsCalled() throws Exception {
        String json = """
                {
                  "email": "test@example.com",
                  "type": "DELETE"
                }
                """;

        mockMvc.perform(post("/api/v1/notifications/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(mailService).sendAccountDeleted("test@example.com");
    }

    @Test
    void whenUnknownType_thenBadRequestAndMailNotCalled() throws Exception {
        String json = """
                {
                  "email": "test@example.com",
                  "type": "SOMETHING"
                }
                """;

        mockMvc.perform(post("/api/v1/notifications/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verify(mailService, never()).sendAccountCreated("test@example.com");
        verify(mailService, never()).sendAccountDeleted("test@example.com");
    }

    @Test
    void whenInvalidEmail_thenBadRequest() throws Exception {
        String json = """
                {
                  "email": "not-an-email",
                  "type": "CREATE"
                }
                """;

        mockMvc.perform(post("/api/v1/notifications/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verify(mailService, never()).sendAccountCreated("not-an-email");
    }

    @Test
    void whenEmptyBody_thenBadRequest() throws Exception {
        String json = """
                {
                  "email": "",
                  "type": ""
                }
                """;

        mockMvc.perform(post("/api/v1/notifications/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verify(mailService, never()).sendAccountCreated("");
        verify(mailService, never()).sendAccountDeleted("");
    }
}

