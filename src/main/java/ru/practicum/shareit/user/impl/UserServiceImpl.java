package ru.practicum.shareit.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ContentAlreadyExistException;
import ru.practicum.shareit.exceptions.ContentNotFountException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepositoryInMemoryImpl userDao;

    @Override
    public List<UserDto> getAllUsers() {
        return userDao.getAllUsers().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        if (userDao.getUserByEmail(user.getEmail()) != null) {
            throw new ContentAlreadyExistException("Пользователь с таким email уже существует");
        }
        return UserMapper.toUserDto(userDao.saveUser(user));
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        if (userDto.getId() == null) {
            throw new ContentNotFountException("Необходимо указать id пользователя");
        }
        User user = userDao.getUserById(userDto.getId());
        if (user == null) {
            throw new ContentNotFountException("Пользователь не найден");
        }

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }

        User sameEmailUser = userDao.getUserByEmail(user.getEmail());
        if (sameEmailUser != null && !sameEmailUser.getId().equals(user.getId())) {
            throw new ContentAlreadyExistException("Пользователь с таким email уже существует");
        }

        //Валидация User
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        validator.validate(user);
        return UserMapper.toUserDto(userDao.updateUser(user));
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userDao.getUserById(id);
        if (user == null) {
            throw new ContentNotFountException("Пользователя с id = " + id + " не существует");
        }
        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userDao.getUserById(id);
        if (user == null) {
            throw new ContentNotFountException("Пользователя с id = " + id + " не существует");
        }
        userDao.deleteUser(id);
    }
}
