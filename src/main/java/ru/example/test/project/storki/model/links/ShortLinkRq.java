package ru.example.test.project.storki.model.links;

import lombok.*;
import org.hibernate.Hibernate;
import ru.example.test.project.storki.model.user.UserRq;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class ShortLinkRq {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fullUrl;
    private String shortUrl;
    private Integer redirectCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ShortLinkRq that = (ShortLinkRq) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
