package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validations.Create;
import ru.practicum.shareit.validations.Update;

@Slf4j
@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Возвращен список всех пользователей");
        return userClient.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Long id) {
        log.info("Возвращен пользователь с id = {}", id);
        return userClient.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<Object> saveUser(@Validated(Create.class) @RequestBody UserDto userDto) {
        ResponseEntity<Object> savedUserDto = userClient.saveUser(userDto);
        log.info("Пользователь сохранен");
        return savedUserDto;
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@Validated(Update.class) @RequestBody UserDto userDto,
                                             @PathVariable Long userId) {
        userDto.setId(userId);
        ResponseEntity<Object> savedUserDto = userClient.updateUser(userId, userDto);
        log.info("Обновлены поля у пользователя с id {}", userId);
        return savedUserDto;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        log.info("Пользователь удален, id = {}", id);
        return userClient.deleteUser(id);
    }
}
