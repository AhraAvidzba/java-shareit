package ru.practicum.shareit.user;

import java.util.List;

public interface UserRepository {
    List<User> getAllUsers();

    User saveUser(User user);

    User updateUser(User user);

    User getUserById(Long id);

    User getUserByEmail(String email);

    void deleteUser(Long id);
}
