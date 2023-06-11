package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ContentNotFountException;
import ru.practicum.shareit.user.dao.UserDaoInMemoryImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDaoInMemoryImpl userDao;

    @Override
    public List<UserDto> getAllUsers() {
        return userDao.getAllUsers().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto saveUser(User user) {
        return UserMapper.toUserDto(userDao.saveUser(user));
    }

    @Override
    public UserDto updateUser(User user) {
        if (user.getId() == null || userDao.getUserById(user.getId()) == null) {
            throw new ContentNotFountException("Пользователь не найден");
        }
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

}
