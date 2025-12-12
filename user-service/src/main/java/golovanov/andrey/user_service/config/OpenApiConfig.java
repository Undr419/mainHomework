package golovanov.andrey.user_service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "User Service API",
                version = "1.0.0",
                description = "API для управления пользователями. Позволяет создавать, читать и обновлять данные.",
                contact = @Contact(
                        name = "Andrey Golovanov",
                        email = "testmail@mail.com"

                )
        )
)

public class OpenApiConfig {
}
