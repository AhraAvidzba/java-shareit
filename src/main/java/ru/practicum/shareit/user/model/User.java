package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@Setter
@Builder(toBuilder = true)
public class User {
    private Long id;
    @NotBlank(message = "Необходимо указать имя пользователя")
    private String name;
    @NotNull
    @Email(message = "Невалидный email")
    private String email;
}
