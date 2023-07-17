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
        log.info("Getting all users");
        return userClient.getAllUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable Long userId) {
        log.info("Getting userId={}", userId);
        return userClient.getUserById(userId);
    }

    @PostMapping
    public ResponseEntity<Object> saveUser(@Validated(Create.class) @RequestBody UserDto userDto) {
        ResponseEntity<Object> savedUserDto = userClient.saveUser(userDto);
        log.info("Creating user {}", userDto);
        return savedUserDto;
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@Validated(Update.class) @RequestBody UserDto userDto,
                                             @PathVariable Long userId) {
        userDto.setId(userId);
        ResponseEntity<Object> savedUserDto = userClient.updateUser(userId, userDto);
        log.info("Updating user {}, userId={}", userDto, userId);
        return savedUserDto;
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        log.info("Deleting userId={}", userId);
        return userClient.deleteUser(userId);
    }
}
