package ru.example.test.project.storki.service.impl;

import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import ru.example.test.project.storki.config.AppProperties;
import ru.example.test.project.storki.exception.AppException;
import ru.example.test.project.storki.model.links.ShortLinkRq;
import ru.example.test.project.storki.model.links.ShortLinkRs;
import ru.example.test.project.storki.model.user.UserRq;
import ru.example.test.project.storki.repo.LinkRepo;
import ru.example.test.project.storki.repo.UserRepo;
import ru.example.test.project.storki.service.api.LinkService;
import ru.example.test.project.storki.utils.AdapterMapper;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static ru.example.test.project.storki.utils.AdapterMapper.convertToLinkRs;

@Slf4j
@Service
public class LinkServiceImpl implements LinkService {

    private final LinkRepo linkRepo;
    private final UserRepo userRepo;
    private final String prefixUrl;

    public LinkServiceImpl(LinkRepo linkRepo, UserRepo userRepo, AppProperties appProperties) {
        this.linkRepo = linkRepo;
        this.userRepo = userRepo;
        prefixUrl = String.format("http://%s:%s/s/", appProperties.getHost(), appProperties.getPort());
    }

    @Override
    @Transactional
    public ShortLinkRs addLink(String username, ShortLinkRq link) throws AppException {
        try {
            log.info("LinkServiceImpl.addLink(), User: {}, Link: {}", username, link);
            UserRq userRq = userRepo.findByUsername(username);

            if (userRq == null) {
                log.error("User: {}, not found", userRq);
                throw new AppException("User not found!");
            }

            UrlValidator urlValidator = new UrlValidator(
                    new String[]{"http", "https"}
            );

            if (urlValidator.isValid(link.getFullUrl())) {
                String shortUrl = prefixUrl + Hashing.murmur3_32_fixed().hashString(link.getFullUrl(), StandardCharsets.UTF_8).toString();

                ShortLinkRq fullLink = linkRepo.findByFullUrl(link.getFullUrl());
                ShortLinkRq shortLink = linkRepo.findByShortUrl(shortUrl);

                if (fullLink != null || shortLink != null) {
                    log.error("URL: {} is exist!", fullLink);
                    throw new AppException("Current URL is exist!");
                }

                ShortLinkRq linkRq = new ShortLinkRq();
                linkRq.setFullUrl(link.getFullUrl());
                linkRq.setShortUrl(shortUrl);
                linkRq.setRedirectCount(0);

                userRq.getLinksRq().add(linkRq);

                linkRepo.save(linkRq);
                userRepo.save(userRq);

                log.info("Saved ShortLink: {}", linkRq);
                log.info("Update User: {}", userRq);

                return convertToLinkRs(linkRq);
            } else {
                log.error("URL: {}, is not valid", link);
                throw new AppException("URL is not valid");
            }
        } catch (Exception e) {
            throw new AppException(e.getMessage(), e);
        }
    }

    @Override
    public List<ShortLinkRs> getLinks() {
        log.info("Get all links");
        return linkRepo.findAll()
                .stream()
                .map(AdapterMapper::convertToLinkRs)
                .collect(Collectors.toList());
    }

    @Override
    public ShortLinkRs getLink(String shortUrl) throws AppException {
        log.info("LinkServiceImpl.getLink(), ShortUrl: {}", shortUrl);

        ShortLinkRq link = linkRepo.findByShortUrl(prefixUrl + shortUrl);

        if (link == null) {
            log.error("ShortUrl: {}, not found!", shortUrl);
            throw new AppException("Link not found!");
        }

        log.info("Fetching Link: {}", link);
        return convertToLinkRs(link);
    }

    @Override
    public ShortLinkRs redirect(String shortUrl) throws AppException {
        log.info("LinkServiceImpl.redirect(), ShortUrl: {}", shortUrl);

        ShortLinkRq link = linkRepo.findByShortUrl(prefixUrl + shortUrl);

        if (link == null) {
            log.error("ShortUrl: {}, not found!", shortUrl);
            throw new AppException("Link not found!");
        }

        link.setRedirectCount(link.getRedirectCount() + 1);
        linkRepo.save(link);

        log.info("Redirect to full URL, Link: {}", link);
        return convertToLinkRs(link);
    }

    @Override
    @Transactional
    public void deleteLink(String username, String shortUrl) throws AppException {
        log.info("LinkServiceImpl.deleteLink(), User: {}, ShortUrl: {}", username, shortUrl);

        UserRq userRq = userRepo.findByUsername(username);
        ShortLinkRq linkRq = linkRepo.findByShortUrl(prefixUrl + shortUrl);

        if (userRq != null && linkRq != null) {
            if (userRq.getLinksRq().contains(linkRq)) {
                linkRepo.deleteById(linkRq.getId());

                userRq.getLinksRq().remove(linkRq);
                userRepo.save(userRq);

                log.info("Remove Link: {}, from User: {}", linkRq, userRq);
            } else {
                log.error("User: {}, doesn't have Link: {}", userRq, linkRq);
                throw new AppException("Invalid data to delete of link");
            }
        } else {
            log.error("User: {}, or Link: {}, doesn't exist", userRq, linkRq);
            throw new AppException("User or Link doesn't exist");
        }
    }
}
