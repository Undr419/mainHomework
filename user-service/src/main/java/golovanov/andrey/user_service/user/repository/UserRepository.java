package golovanov.andrey.user_service.user.repository;

import golovanov.andrey.user_service.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}