package ru.practicum.shareit.user.impl;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryInMemoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private Long globalId = 1L;

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User saveUser(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public User getUserById(Long id) {
        User returnedUser = users.get(id);
        return returnedUser == null ? null : returnedUser.toBuilder().build();
    }

    @Override
    public User getUserByEmail(String email) {
        User returnedUser = users.values().stream()
                .filter(x -> x.getEmail().equals(email))
                .findFirst()
                .orElse(null);
        return returnedUser == null ? null : returnedUser.toBuilder().build();
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
    }

    private Long generateId() {
        return globalId++;
    }
}
