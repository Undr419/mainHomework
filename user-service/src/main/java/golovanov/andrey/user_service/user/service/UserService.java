package golovanov.andrey.user_service.user.service;

import golovanov.andrey.user_service.user.model.User;

import java.util.List;

public interface UserService {

    User createUser(User user);

    User getUserById(Long id);

    List<User> getAllUsers();

    User updateUser(Long id, User updateUser);

    void deleteUser(Long id);
}
