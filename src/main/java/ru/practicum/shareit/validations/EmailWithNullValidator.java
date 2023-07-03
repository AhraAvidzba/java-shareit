package ru.practicum.shareit.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class EmailWithNullValidator implements ConstraintValidator<EmailWithNull, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return Pattern.matches("^(.+)@(\\S+)$", value);
    }
}
