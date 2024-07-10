package org.lumeninvestiga.backend.repositorio.tpi.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.lumeninvestiga.backend.repositorio.tpi.services.UserService;
import org.springframework.stereotype.Component;

@Component
public class ExistsByUsernameValidation implements ConstraintValidator<ExistsByUsername, String> {
    private final UserService userService;

    public ExistsByUsernameValidation(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (userService == null) return true;
        return !userService.existUserByUsername(s);
    }
}
