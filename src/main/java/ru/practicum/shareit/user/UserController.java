package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Возвращен список всех пользователей");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        log.info("Возвращен пользователь с id = {}", id);
        return userService.getUserById(id);
    }

    @PostMapping
    public UserDto saveUser(@Valid @RequestBody UserDto userDto) {
        UserDto savedUserDto = userService.saveUser(userDto);
        log.info("Пользователь сохранен, id = {}", savedUserDto.getId());
        return savedUserDto;
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@RequestBody UserDto userDto,
                              @PathVariable Long userId) {
        userDto.setId(userId);
        UserDto savedUserDto = userService.updateUser(userDto);
        log.info("Обновлены поля у пользователя с id {}", savedUserDto.getId());
        return userService.updateUser(savedUserDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        log.info("Пользователь удален, id = {}", id);
    }
}
