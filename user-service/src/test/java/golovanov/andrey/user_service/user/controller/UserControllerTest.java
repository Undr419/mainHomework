package golovanov.andrey.user_service.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import golovanov.andrey.user_service.user.dto.CreateUserRequest;
import golovanov.andrey.user_service.user.dto.UpdateUserRequest;
import golovanov.andrey.user_service.user.model.User;
import golovanov.andrey.user_service.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUser_shouldReturn201AndUserResponse() throws Exception {

        CreateUserRequest request = new CreateUserRequest();
        request.setName("Andrey");
        request.setEmail("andrey@example.com");
        request.setAge(30);

        User savedUser = Mockito.mock(User.class);
        when(savedUser.getId()).thenReturn(1L);
        when(savedUser.getName()).thenReturn("Andrey");
        when(savedUser.getEmail()).thenReturn("andrey@example.com");
        when(savedUser.getAge()).thenReturn(30);
        when(savedUser.getCreatedAt()).thenReturn(LocalDateTime.now());

        when(userService.createUser(any(User.class))).thenReturn(savedUser);

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                        post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Andrey"))
                .andExpect(jsonPath("$.email").value("andrey@example.com"))
                .andExpect(jsonPath("$.age").value(30));
    }

    @Test
    void getUserById_shouldReturn200AndUserResponse() throws Exception {

        User user = Mockito.mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getName()).thenReturn("Andrey");
        when(user.getEmail()).thenReturn("andrey@example.com");
        when(user.getAge()).thenReturn(30);
        when(user.getCreatedAt()).thenReturn(LocalDateTime.now());

        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(
                        get("/api/users/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Andrey"))
                .andExpect(jsonPath("$.email").value("andrey@example.com"))
                .andExpect(jsonPath("$.age").value(30));
    }

    @Test
    void getAllUsers_shouldReturn200AndListOfUserResponse() throws Exception {

        User user1 = Mockito.mock(User.class);
        when(user1.getId()).thenReturn(1L);
        when(user1.getName()).thenReturn("Andrey");
        when(user1.getEmail()).thenReturn("andrey@example.com");
        when(user1.getAge()).thenReturn(30);
        when(user1.getCreatedAt()).thenReturn(LocalDateTime.now());

        User user2 = Mockito.mock(User.class);
        when(user2.getId()).thenReturn(2L);
        when(user2.getName()).thenReturn("Ivan");
        when(user2.getEmail()).thenReturn("ivan@example.com");
        when(user2.getAge()).thenReturn(25);
        when(user2.getCreatedAt()).thenReturn(LocalDateTime.now());

        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        mockMvc.perform(
                        get("/api/users")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Andrey"))
                .andExpect(jsonPath("$[0].email").value("andrey@example.com"))
                .andExpect(jsonPath("$[0].age").value(30))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Ivan"))
                .andExpect(jsonPath("$[1].email").value("ivan@example.com"))
                .andExpect(jsonPath("$[1].age").value(25));
    }

    @Test
    void updateUser_shouldReturn200AndUpdatedUserResponse() throws Exception {

        Long userId = 1L;

        UpdateUserRequest request = new UpdateUserRequest();
        request.setName("Updated Name");
        request.setEmail("updated@example.com");
        request.setAge(35);

        User updatedUser = Mockito.mock(User.class);
        when(updatedUser.getId()).thenReturn(userId);
        when(updatedUser.getName()).thenReturn("Updated Name");
        when(updatedUser.getEmail()).thenReturn("updated@example.com");
        when(updatedUser.getAge()).thenReturn(35);
        when(updatedUser.getCreatedAt()).thenReturn(LocalDateTime.now());

        when(userService.updateUser(eq(userId), any(User.class))).thenReturn(updatedUser);

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                        put("/api/users/{id}", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.email").value("updated@example.com"))
                .andExpect(jsonPath("$.age").value(35));
    }

    @Test
    void deleteUser_shouldReturn204() throws Exception {

        Long userId = 1L;

        mockMvc.perform(
                        delete("/api/users/{id}", userId)
                )
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(userId);
    }
}
