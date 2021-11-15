package ru.example.test.project.storki.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.example.test.project.storki.exception.AppException;
import ru.example.test.project.storki.model.links.ShortLinkRq;
import ru.example.test.project.storki.model.user.RoleRq;
import ru.example.test.project.storki.model.user.RoleToUser;
import ru.example.test.project.storki.model.user.UserRq;
import ru.example.test.project.storki.service.api.LinkService;
import ru.example.test.project.storki.service.api.RoleService;
import ru.example.test.project.storki.service.api.UserService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Bean
    public CommandLineRunner run(UserService userService, RoleService roleService, LinkService linkService) {
        return args -> {
            // Извините, что не написал миграции...(

            List<UserRq> users = createUsers();
            List<RoleRq> roles = createRoles();
            List<ShortLinkRq> links = createLinks();

            roles.forEach(role -> {
                try {
                    roleService.saveRole(role);
                } catch (AppException e) {
                    log.error(e.getMessage());
                }
            });

            users.forEach(user -> {
                try {
                    userService.saveUser(user);
                } catch (AppException e) {
                    log.error(e.getMessage());
                }
            });
            userService.addRoleToUser(new RoleToUser(users.get(0).getUsername(), roles.get(0).getName()));

            linkService.addLink(users.get(0).getUsername(), links.get(0));
            linkService.addLink(users.get(0).getUsername(), links.get(1));
            linkService.addLink(users.get(0).getUsername(), links.get(2));
            linkService.addLink(users.get(0).getUsername(), links.get(3));
            linkService.addLink(users.get(1).getUsername(), links.get(4));
            linkService.addLink(users.get(2).getUsername(), links.get(5));
            linkService.addLink(users.get(3).getUsername(), links.get(6));
            linkService.addLink(users.get(4).getUsername(), links.get(7));
            linkService.addLink(users.get(4).getUsername(), links.get(8));
            linkService.addLink(users.get(5).getUsername(), links.get(9));

        };
    }

    private List<UserRq> createUsers() {
        return new ArrayList<>() {{
            add(new UserRq(null, "Admin", "Admin", "admin", "admin", new ArrayList<>(), new ArrayList<>()));
            add(new UserRq(null, "Penny", "Lopez", "penny", "123qwe", new ArrayList<>(), new ArrayList<>()));
            add(new UserRq(null, "Ed", "Rivera", "ed", "123qwe", new ArrayList<>(), new ArrayList<>()));
            add(new UserRq(null, "Roosevelt", "Maldonado", "roosevelt", "123qwe", new ArrayList<>(), new ArrayList<>()));
            add(new UserRq(null, "Ernest", "Duncan", "ernest", "123qwe", new ArrayList<>(), new ArrayList<>()));
            add(new UserRq(null, "Kevin", "Bowers", "kevin", "123qwe", new ArrayList<>(), new ArrayList<>()));
        }};
    }

    private List<RoleRq> createRoles() {
        return new ArrayList<>() {{
            add(new RoleRq(null, "ROLE_ADMIN"));
            add(new RoleRq(null, "ROLE_USER"));
            add(new RoleRq(null, "ROLE_MANAGER"));
            add(new RoleRq(null, "ROLE_DEVELOPER"));
        }};
    }

    private List<ShortLinkRq> createLinks() {
        return new ArrayList<>() {{
            add(new ShortLinkRq(null, "https://www.google.com", null, null));
            add(new ShortLinkRq(null, "https://www.youtube.com", null, null));
            add(new ShortLinkRq(null, "https://www.facebook.com", null, null));
            add(new ShortLinkRq(null, "https://www.wikipedia.org", null, null));
            add(new ShortLinkRq(null, "https://www.twitter.com", null, null));
            add(new ShortLinkRq(null, "https://www.amazon.com", null, null));
            add(new ShortLinkRq(null, "https://www.instagram.com", null, null));
            add(new ShortLinkRq(null, "https://www.reddit.com", null, null));
            add(new ShortLinkRq(null, "https://www.netflix.com", null, null));
            add(new ShortLinkRq(null, "https://www.spotify.com", null, null));
        }};
    }

}
