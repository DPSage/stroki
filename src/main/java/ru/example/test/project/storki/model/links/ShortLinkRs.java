package ru.example.test.project.storki.model.links;

import lombok.Data;

@Data
public class ShortLinkRs {

    private String fullUrl;
    private String shortUrl;
    private Integer redirectCount;

}
