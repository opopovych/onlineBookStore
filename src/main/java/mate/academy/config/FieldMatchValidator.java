package mate.academy.config;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mate.academy.model.User;
import org.apache.commons.lang3.StringUtils;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, User> {

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
    }

    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {
        if (user == null) {
            return false;
        }
        if (user.getEmail() == null) {
            return false;
        }

        if (StringUtils.isBlank(user.getEmail())) {
            return false;
        }
        return true;

    }
}
