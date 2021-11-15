package ru.example.test.project.storki.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.example.test.project.storki.config.AppProperties;
import ru.example.test.project.storki.repo.LinkRepo;
import ru.example.test.project.storki.repo.UserRepo;
import ru.example.test.project.storki.service.api.LinkService;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class LinkServiceImplTest {

    @Mock
    private LinkRepo linkRepo;
    @Mock
    private UserRepo userRepo;

    private AppProperties appProperties;
    private LinkService linkService;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Before
    public void setUp() {
        appProperties = new AppProperties("localhost", 8080);
        linkService = new LinkServiceImpl(linkRepo, userRepo, appProperties);
    }

    @Test
    public void addLink() {
    }

    @Test
    public void getLinks() {
    }

    @Test
    public void getLink() {
    }

    @Test
    public void redirect() {
    }

    @Test
    public void deleteLink() {
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