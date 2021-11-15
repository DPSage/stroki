package ru.example.test.project.storki.service.api;

import org.springframework.security.core.userdetails.User;
import ru.example.test.project.storki.exception.AppException;
import ru.example.test.project.storki.model.links.ShortLinkRq;
import ru.example.test.project.storki.model.links.ShortLinkRs;
import ru.example.test.project.storki.model.user.UserRq;

import java.util.List;

public interface LinkService {

    ShortLinkRs addLink(String username, ShortLinkRq fullUrl) throws AppException;

    List<ShortLinkRs> getLinks();

    ShortLinkRs getLink(String shortUrl) throws AppException;

    ShortLinkRs redirect(String shortUrl) throws AppException;

    void deleteLink(String username, String shortUrl) throws AppException;

}
