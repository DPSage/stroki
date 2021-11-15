package ru.example.test.project.storki.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.example.test.project.storki.exception.AppException;
import ru.example.test.project.storki.model.links.ShortLinkRq;
import ru.example.test.project.storki.model.links.ShortLinkRs;
import ru.example.test.project.storki.service.api.LinkService;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class LinkController {

    private final LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping("/links")
    public ResponseEntity<List<ShortLinkRs>> getLinks() {
        return ResponseEntity.ok().body(linkService.getLinks());
    }

    @PostMapping("/link/add")
    public ResponseEntity<ShortLinkRs> addLink(@RequestBody ShortLinkRq link) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/link/add").toUriString());

            return ResponseEntity.created(uri).body(linkService.addLink(username, link));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/link/{link}/info")
    public ResponseEntity<ShortLinkRs> getLinkInfo(@PathVariable String link) {
        try {
            return ResponseEntity.ok().body(linkService.getLink(link));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping("/link/{link}/delete")
    public ResponseEntity<?> deleteLink(@PathVariable String link) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            linkService.deleteLink(username, link);

            return ResponseEntity.ok().build();
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
