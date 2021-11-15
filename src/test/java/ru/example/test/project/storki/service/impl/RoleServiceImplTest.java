package ru.example.test.project.storki.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.example.test.project.storki.repo.RoleRepo;
import ru.example.test.project.storki.service.api.RoleService;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

import static org.junit.Assert.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class RoleServiceImplTest {

    @Mock
    private RoleRepo roleRepo;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private RoleService roleService;

    @Before
    public void setUp() {
        roleService = new RoleServiceImpl(roleRepo);
    }

    @Test
    public void saveRole() {
    }

    @Test
    public void getRoles() {
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