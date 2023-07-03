package ru.practicum.shareit.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NameWithNullValidator implements ConstraintValidator<NameWithNull, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return !value.isBlank();
    }
}
