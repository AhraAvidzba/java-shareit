package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class User {
    private Long id;
    @EqualsAndHashCode.Exclude
    @NotBlank(message = "Необходимо указать имя пользователя")
    private String name;
    @EqualsAndHashCode.Exclude
    @Email(message = "Невалидный email")
    private String email;
}
