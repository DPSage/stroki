package ru.example.test.project.storki.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.test.project.storki.model.user.RoleRq;

public interface RoleRepo extends JpaRepository<RoleRq, Long> {
    RoleRq findByName(String name);
}
