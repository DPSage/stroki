package ru.example.test.project.storki.service.api;

import ru.example.test.project.storki.exception.AppException;
import ru.example.test.project.storki.model.user.RoleRq;
import ru.example.test.project.storki.model.user.RoleRs;

import java.util.List;

public interface RoleService {

    RoleRs saveRole(RoleRq roleRq) throws AppException;

    List<RoleRs> getRoles();

}
