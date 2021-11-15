package ru.example.test.project.storki.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.test.project.storki.model.user.UserRq;

public interface UserRepo extends JpaRepository<UserRq, Long> {
    UserRq findByUsername(String username);
}
