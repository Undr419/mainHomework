package golovanov.andrey.user_service.user.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(description = "Ответ с данными пользователя")
public class UserResponse extends RepresentationModel<UserResponse> {

    @Schema(description = "ID пользователя")
    private Long id;

    @Schema(description = "Имя")
    private String name;

    @Schema(description = "Почта")
    private String email;

    @Schema(description = "Возраст")
    private Integer age;

    private LocalDateTime createdAt;
}
