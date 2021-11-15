package ru.example.test.project.storki.model.user;

import lombok.*;
import org.hibernate.Hibernate;
import ru.example.test.project.storki.model.links.ShortLinkRq;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class UserRq {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String secondName;
    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<RoleRq> rolesRq = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(targetEntity = ShortLinkRq.class, cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private Collection<ShortLinkRq> linksRq = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserRq userRq = (UserRq) o;
        return id != null && Objects.equals(id, userRq.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
