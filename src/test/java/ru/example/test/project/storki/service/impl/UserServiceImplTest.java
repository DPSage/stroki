package ru.example.test.project.storki.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.example.test.project.storki.model.user.UserRq;
import ru.example.test.project.storki.repo.RoleRepo;
import ru.example.test.project.storki.repo.UserRepo;
import ru.example.test.project.storki.service.api.UserService;
import ru.example.test.project.storki.validation.UserValidation;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;
    @Mock
    private RoleRepo roleRepo;
    @Mock
    private UserValidation validation;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private UserService userService;
    private UserDetailsService userDetailsService;

    private String USERNAME = "admin";
    private UserRq userRq = convertJsonToObject("json/UserRq.json", UserRq.class);

    @Before
    public void setUp() {
        userService = new UserServiceImpl(userRepo, roleRepo, validation, passwordEncoder);
    }

    @Test
    public void loadUserByUsername() {
        userDetailsService = new UserServiceImpl(userRepo, roleRepo, validation, passwordEncoder);

        when(userRepo.findByUsername(anyString()))
                .thenReturn(userRq);

        UserDetails userDetails = userDetailsService.loadUserByUsername(USERNAME);

        assertEquals(userRq.getUsername(), userDetails.getUsername());
        assertEquals(userRq.getPassword(), userDetails.getPassword());
    }

    @Test
    public void saveUser() {
    }

    @Test
    public void addRoleToUser() {
    }

    @Test
    public void getUser() {
    }

    @Test
    public void getUsers() {
    }


    private <T> T convertJsonToObject(String path, Class<T> tClass) {
        try {
            log.info("AppConfig.convertJsonToObject(), Path: {}, Class: {}", path, tClass);

            URL resource = Thread.currentThread().getContextClassLoader().getResource(path);

            return objectMapper.readValue(
                    new File(Objects.requireNonNull(resource).toURI()),
                    tClass
            );
        } catch (URISyntaxException | IOException e) {
            log.error("Fain to create Object");
            log.error(e.getMessage());
            return null;
        }
    }
}