package golovanov.andrey.user_service.user.controller;


import golovanov.andrey.user_service.user.dto.CreateUserRequest;
import golovanov.andrey.user_service.user.dto.UpdateUserRequest;
import golovanov.andrey.user_service.user.dto.UserResponse;
import golovanov.andrey.user_service.user.model.User;
import golovanov.andrey.user_service.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController (UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request) {
        User user = toUser(request);
        User saved = userService.createUser(user);
        UserResponse response = toUserResponse(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return toUserResponse(user);
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return users.stream()
                .map(this::toUserResponse)
                .toList();
    }

    @PutMapping("/{id}")
    public UserResponse updateUser (@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        User existing = userService.getUserById(id);
        existing.setName(request.getName());
        existing.setEmail(request.getEmail());
        existing.setAge(request.getAge());
        User updated = userService.updateUser(id, existing);
        return toUserResponse(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
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
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getCreatedAt()
        );
    }
}
