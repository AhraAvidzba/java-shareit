package ru.practicum.shareit.user.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.validations.Create;
import ru.practicum.shareit.validations.EmailWithNull;
import ru.practicum.shareit.validations.NameWithNull;
import ru.practicum.shareit.validations.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserDto {
    private Long id;
    @NameWithNull(groups = Update.class)
    @NotEmpty(message = "Имя пользователя не должно быть пустым", groups = Create.class)
    private String name;
    @EmailWithNull(groups = Update.class)
    @NotNull(groups = Create.class)
    @Email(groups = Create.class)
    private String email;
}
