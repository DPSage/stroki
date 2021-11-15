package ru.example.test.project.storki.model.user;

import lombok.Data;
import ru.example.test.project.storki.model.links.ShortLinkRs;

import java.util.Collection;

@Data
public class UserRs {

    private String firstName;
    private String secondName;
    private String username;
    private Collection<RoleRs> roles;
    private Collection<ShortLinkRs> links;

}
