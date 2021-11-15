package ru.example.test.project.storki.validation;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.example.test.project.storki.exception.AppException;
import ru.example.test.project.storki.model.user.UserRq;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserValidation {
    public void validateUserRq(UserRq userRq) throws AppException {
        try {
            checkNotNull(userRq);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(e.getMessage(), e);
        }
    }

    private void checkNotNull(UserRq userRq) throws AppException {
        try {
            List<Field> fields = Arrays.stream(userRq.getClass().getDeclaredFields())
                    .filter(f -> !f.getName().equals("id"))
                    .collect(Collectors.toList());

            for (Field f : fields) {
                f.setAccessible(true);
                Preconditions.checkNotNull(f.get(userRq), "Поле " + f.getName() + " не заполнено");
            }
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
    }
}
