package golovanov.andrey.notification_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMailRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String type; // "CREATE" или "DELETE"
}

