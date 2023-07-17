package ru.practicum.shareit.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class User {
    private Long id;

    @NotBlank(message = "Необходимо указать имя пользователя")
    private String name;

    @NotNull
    @Email(message = "Невалидный email")
    private String email;
}
