package ru.practicum.shareit.user.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.validations.Create;
import ru.practicum.shareit.validations.EmailOrNull;
import ru.practicum.shareit.validations.NotBlankOrNull;
import ru.practicum.shareit.validations.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode
public class UserDto {
    private Long id;
    @NotBlank(message = "Имя пользователя не должно быть пустым", groups = Create.class)
    @NotBlankOrNull(message = "Имя пользователя не должно быть пустым", groups = Update.class)
    private String name;
    @NotNull(groups = Create.class)
    @Email(groups = Create.class)
    @EmailOrNull(groups = Update.class)
    private String email;
}
