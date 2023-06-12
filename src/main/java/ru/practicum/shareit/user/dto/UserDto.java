package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class UserDto {
    private Long id;
    @NotBlank(message = "Необходимо указать имя пользователя")
    private String name;
    @NotNull
    @Email(message = "Невалидный email")
    private String email;
}
