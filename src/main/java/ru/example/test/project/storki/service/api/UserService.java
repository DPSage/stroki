package ru.example.test.project.storki.service.api;

import ru.example.test.project.storki.exception.AppException;
import ru.example.test.project.storki.model.user.RoleToUser;
import ru.example.test.project.storki.model.user.UserRq;
import ru.example.test.project.storki.model.user.UserRs;

import java.util.List;

public interface UserService {

    UserRs saveUser(UserRq userRq) throws AppException;

    void addRoleToUser(RoleToUser roleToUser);

    UserRs getUser(String username);

    List<UserRs> getUsers();

}
