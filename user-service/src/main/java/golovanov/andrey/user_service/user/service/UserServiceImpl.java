package golovanov.andrey.user_service.user.service;

import golovanov.andrey.user_service.user.event.UserEventPublisher;
import golovanov.andrey.user_service.user.exception.UserNotFoundException;
import golovanov.andrey.user_service.user.model.User;
import golovanov.andrey.user_service.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserEventPublisher userEventPublisher;

    public UserServiceImpl(UserRepository userRepository,
                           UserEventPublisher userEventPublisher) {
        this.userRepository = userRepository;
        this.userEventPublisher = userEventPublisher;
    }

    @Override
    public User createUser(User user) {
        User saved = userRepository.save(user);

        userEventPublisher.publishUserCreated(saved.getEmail());

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(Long id, User updateUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setName(updateUser.getName());
        user.setEmail(updateUser.getEmail());
        user.setAge(updateUser.getAge());

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        String email = user.getEmail();

        userRepository.delete(user);

        userEventPublisher.publishUserDeleted(email);
    }
}
