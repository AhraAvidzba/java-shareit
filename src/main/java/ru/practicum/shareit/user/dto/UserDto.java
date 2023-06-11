package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class UserDto {
    private Long id;
    @EqualsAndHashCode.Exclude
    @NotBlank(message = "Необходимо указать имя пользователя")
    private String name;
    @EqualsAndHashCode.Exclude
    @Email(message = "Невалидный email")
    private String email;
}
