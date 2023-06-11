package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserDao {
    List<User> getAllUsers();

    User saveUser(User user);

    User updateUser(User user);

    User getUserById(Long id);
}
