package ru.example.test.project.storki.utils;

import ru.example.test.project.storki.model.links.ShortLinkRq;
import ru.example.test.project.storki.model.links.ShortLinkRs;
import ru.example.test.project.storki.model.user.RoleRq;
import ru.example.test.project.storki.model.user.RoleRs;
import ru.example.test.project.storki.model.user.UserRq;
import ru.example.test.project.storki.model.user.UserRs;

import java.util.stream.Collectors;

public class AdapterMapper {

    public static UserRs convertToUserRs(UserRq userRq) {
        UserRs user = new UserRs();

        user.setUsername(userRq.getUsername());
        user.setFirstName(userRq.getFirstName());
        user.setSecondName(userRq.getSecondName());
        user.setRoles(userRq.getRolesRq()
                .stream()
                .map(AdapterMapper::convertToRoleRs)
                .collect(Collectors.toList()));
        user.setLinks(userRq.getLinksRq()
                .stream()
                .map(AdapterMapper::convertToLinkRs)
                .collect(Collectors.toList()));

        return user;
    }

    public static RoleRs convertToRoleRs(RoleRq roleRq) {
        RoleRs role = new RoleRs();

        role.setName(roleRq.getName());

        return role;
    }

    public static ShortLinkRs convertToLinkRs(ShortLinkRq linkRq) {
        ShortLinkRs linkRs = new ShortLinkRs();

        linkRs.setFullUrl(linkRq.getFullUrl());
        linkRs.setShortUrl(linkRq.getShortUrl());
        linkRs.setRedirectCount(linkRq.getRedirectCount());

        return linkRs;
    }

}
