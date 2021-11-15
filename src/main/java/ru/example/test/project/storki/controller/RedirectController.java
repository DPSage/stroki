package ru.example.test.project.storki.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.example.test.project.storki.exception.AppException;
import ru.example.test.project.storki.service.api.LinkService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
public class RedirectController {

    private final LinkService linkService;

    public RedirectController(LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping("/{link}")
    public void redirect(HttpServletResponse response, @PathVariable String link) {
        try {
            log.info("Redirect. ShortLink: {}", link);
            response.sendRedirect(linkService.redirect(link).getFullUrl());
        } catch (IOException | AppException e) {
            log.error("URL: {} not found!", link);
        }
    }

}
