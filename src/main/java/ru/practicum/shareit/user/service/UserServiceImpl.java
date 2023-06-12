package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.exceptions.ContentAlreadyExistException;
import ru.practicum.shareit.exceptions.ContentNotFountException;
import ru.practicum.shareit.user.dao.UserDaoInMemoryImpl;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
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
        if (userDao.getUserByEmail(user.getEmail()) != null) {
            throw new ContentAlreadyExistException("Пользователь с таким email уже существует");
        }
        return userDao.saveUser(user);
    }

    @Override
    @Validated
    public User updateUser(@Valid User user) {
        User sameEmailUser = userDao.getUserByEmail(user.getEmail());
        if (sameEmailUser != null && !sameEmailUser.getId().equals(user.getId())) {
            throw new ContentAlreadyExistException("Пользователь с таким email уже существует");
        }
        if (user.getId() == null || userDao.getUserById(user.getId()) == null) {
            throw new ContentNotFountException("Пользователь не найден");
        }
        //User t = userDao.updateUser(user);
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

    @Override
    public void deleteUser(Long id) {
        User user = userDao.getUserById(id);
        if (user == null) {
            throw new ContentNotFountException("Пользователя с id = " + id + " не существует");
        }
        userDao.deleteUser(id);
    }
}
