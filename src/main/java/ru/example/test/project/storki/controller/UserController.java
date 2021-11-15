package ru.example.test.project.storki.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.example.test.project.storki.exception.AppException;
import ru.example.test.project.storki.model.user.RoleToUser;
import ru.example.test.project.storki.model.user.UserRq;
import ru.example.test.project.storki.model.user.UserRs;
import ru.example.test.project.storki.service.api.UserService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserRs>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping("/user/info")
    public ResponseEntity<UserRs> getUser(HttpServletRequest request) {
        User user = (User) request.getUserPrincipal();
        log.info("Get info about authorization User: {}", user);

        return ResponseEntity.ok().body(userService.getUser(user.getUsername()));
    }

    @PostMapping("/user/add-role")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUser roleToUser) {
        userService.addRoleToUser(roleToUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/registration")
    public ResponseEntity<UserRs> saveUser(@RequestBody UserRq user) {
        try {
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/registration").toUriString());
            return ResponseEntity.created(uri).body(userService.saveUser(user));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
