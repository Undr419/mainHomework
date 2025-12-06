package golovanov.andrey.user_service.user.controller;


import golovanov.andrey.user_service.user.dto.CreateUserRequest;
import golovanov.andrey.user_service.user.dto.UpdateUserRequest;
import golovanov.andrey.user_service.user.dto.UserResponse;
import golovanov.andrey.user_service.user.model.User;
import golovanov.andrey.user_service.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "Методы для работы с пользователями")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Создание пользователя",
            description = "Создает пользователя. Возвращает 201 Created"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "409", description = "Пользователь уже существует")
    })
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request) {
        User user = toUser(request);
        User saved = userService.createUser(user);
        UserResponse response = toUserResponse(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
        summary = "Получение пользователя по ID",
        description = "Возвращает пользователя по заданному ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{id}")
    public UserResponse getUserById(
            @Parameter(description = "ID пользователя")
            @PathVariable Long id) {
        User user = userService.getUserById(id);
        return toUserResponse(user);
    }
        @Operation(
                summary = "Получение списка всех пользователей",
                description = "Возвращает пустой список, если нет пользователей"
        )
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Список пользователей получен")
        })
        @GetMapping
        public CollectionModel<UserResponse> getAllUsers() {

            List<User> users = userService.getAllUsers();

            List<UserResponse> userResponses = users.stream()
                    .map(this::toUserResponse)
                    .toList();

            CollectionModel<UserResponse> result = CollectionModel.of(userResponses);

            result.add(linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
            result.add(linkTo(methodOn(UserController.class).createUser(null)).withRel("create"));

            return result;
        }

    @Operation(
            summary = "Обновление пользователя",
            description = "Обновляет данные существующего пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные обновлены"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PutMapping("/{id}")
    public UserResponse updateUser (
            @Parameter (description = "ID пользователя")
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request) {
        User existing = userService.getUserById(id);
        existing.setName(request.getName());
        existing.setEmail(request.getEmail());
        existing.setAge(request.getAge());
        User updated = userService.updateUser(id, existing);
        return toUserResponse(updated);
    }
    @Operation(
            summary = "Удаление пользователя по ID",
            description = "Удаляет пользователя по заданному ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пользователь удален"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter (description = "ID пользователя")
            @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    private User toUser(CreateUserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setAge(request.getAge());
        return user;
    }

    private UserResponse toUserResponse(User user) {
        UserResponse response = new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getCreatedAt()
        );

        response.add(linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel());

        response.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("all-users"));

        response.add(linkTo(methodOn(UserController.class)
                .updateUser(user.getId(), null))
                .withRel("update"));

        response.add(linkTo(methodOn(UserController.class)
                .deleteUser(user.getId()))
                .withRel("delete"));

        return response;
    }
}
