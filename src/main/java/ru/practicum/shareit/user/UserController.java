package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Возвращен список всех пользователей");
        return userService.getAllUsers().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        log.info("Возвращен пользователь с id = {}", id);
        return UserMapper.toUserDto(userService.getUserById(id));
    }

    @PostMapping
    public UserDto saveUser(@Valid @RequestBody UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        User savedUser = userService.saveUser(user);
        log.info("Пользователь сохранен, id = {}", savedUser.getId());
        return UserMapper.toUserDto(savedUser);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@RequestBody UserDto userDto,
                              @PathVariable Long userId) {
        User user = userService.getUserById(userId);
        List<String> fields = new ArrayList<>();
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
            fields.add("name");
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
            fields.add("email");
        }
        String allFields = "";
        for (String field : fields) {
            allFields = allFields + " " + field;
        }
        log.info("Обновлены поля у пользователя с id {}: {}", user.getId(), allFields);
        return UserMapper.toUserDto(userService.updateUser(user));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        log.info("Пользователь удален, id = {}", id);
    }
}
