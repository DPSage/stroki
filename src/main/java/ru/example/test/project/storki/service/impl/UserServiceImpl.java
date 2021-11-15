package ru.example.test.project.storki.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.example.test.project.storki.exception.AppException;
import ru.example.test.project.storki.model.user.RoleRq;
import ru.example.test.project.storki.model.user.RoleToUser;
import ru.example.test.project.storki.model.user.UserRq;
import ru.example.test.project.storki.model.user.UserRs;
import ru.example.test.project.storki.repo.RoleRepo;
import ru.example.test.project.storki.repo.UserRepo;
import ru.example.test.project.storki.service.api.UserService;
import ru.example.test.project.storki.utils.AdapterMapper;
import ru.example.test.project.storki.validation.UserValidation;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ru.example.test.project.storki.utils.AdapterMapper.convertToUserRs;

@Slf4j
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final UserValidation validation;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepo userRepo, RoleRepo roleRepo, UserValidation validation, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.validation = validation;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserRq user = userRepo.findByUsername(username);

        if (user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        }

        log.info("User: {}", user);

        Collection<SimpleGrantedAuthority> auth = new ArrayList<>();
        user.getRolesRq().forEach(role ->
                auth.add(new SimpleGrantedAuthority(role.getName()))
        );

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), auth);
    }

    @Override
    @Transactional
    public UserRs saveUser(UserRq userRq) throws AppException {
        log.info("UserServiceImpl.saveUser(), User: {}", userRq);

        try {
            UserRq user = userRepo.findByUsername(userRq.getUsername());

            if (user != null) {
                log.error("User with this name is exist!");
                throw new AppException("User with this name is exist!");
            }

            validation.validateUserRq(userRq);

            userRq.setPassword(passwordEncoder.encode(userRq.getPassword()));
            userRq.getRolesRq().add(roleRepo.findByName("ROLE_USER"));

            userRepo.save(userRq);
            log.info("Saved User: {}, successful", userRq);

            return convertToUserRs(userRq);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void addRoleToUser(RoleToUser roleToUser) {
        log.info("UserServiceImpl.addRoleToUser(), RoleToUser: {}", roleToUser);

        UserRq userRq = userRepo.findByUsername(roleToUser.getUsername());
        RoleRq roleRq = roleRepo.findByName(roleToUser.getRoleName());

        if (userRq != null && roleRq != null) {
            log.info("Role: {} added to User: {}, successful", roleToUser.getRoleName(), roleToUser.getUsername());
            userRq.getRolesRq().add(roleRq);
            userRepo.save(userRq);
        } else {
            log.error("Username or Role doesn't exist!");
        }
    }

    @Override
    public UserRs getUser(String username) {
        log.info("Get user by username: {}", username);
        return convertToUserRs(userRepo.findByUsername(username));
    }

    @Override
    public List<UserRs> getUsers() {
        log.info("Get all users");
        return userRepo.findAll()
                .stream()
                .map(AdapterMapper::convertToUserRs)
                .collect(Collectors.toList());
    }
}
