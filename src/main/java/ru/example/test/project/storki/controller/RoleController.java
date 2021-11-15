package ru.example.test.project.storki.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.example.test.project.storki.exception.AppException;
import ru.example.test.project.storki.model.user.RoleRq;
import ru.example.test.project.storki.model.user.RoleRs;
import ru.example.test.project.storki.service.api.RoleService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/roles")
    public ResponseEntity<List<RoleRs>> getRoles() {
        return ResponseEntity.ok().body(roleService.getRoles());
    }

    @PostMapping("/role/add")
    public ResponseEntity<RoleRs> addRole(@RequestBody RoleRq role) {
        try {
            return ResponseEntity.ok().body(roleService.saveRole(role));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
