package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ContentNotFountException;
import ru.practicum.shareit.user.dao.UserDaoInMemoryImpl;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDaoInMemoryImpl userDao;

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public User saveUser(User user) {
        return userDao.saveUser(user);
    }

    @Override
    public User updateUser(User user) {
        if (user.getId() == null || userDao.getUserById(user.getId()) == null) {
            throw new ContentNotFountException("Пользователь не найден");
        }
        return userDao.updateUser(user);
    }

    @Override
    public User getUserById(Long id) {
        User user = userDao.getUserById(id);
        if (user == null) {
            throw new ContentNotFountException("Пользователя с id = " + id + " не существует");
        }
        return user;
    }

}
