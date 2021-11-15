package ru.example.test.project.storki.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.test.project.storki.model.links.ShortLinkRq;

public interface LinkRepo extends JpaRepository<ShortLinkRq, Long> {
    ShortLinkRq findByFullUrl(String fullUrl);

    ShortLinkRq findByShortUrl(String shortUrl);
}
