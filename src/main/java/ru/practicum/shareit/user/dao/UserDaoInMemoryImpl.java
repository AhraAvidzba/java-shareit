package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoInMemoryImpl implements UserDao {
    private final Map<Long, User> users = new HashMap<>();
    private Long globalId = 0L;

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
        return users.put(user.getId(), user);
    }

    @Override
    public User getUserById(Long id) {
        return users.get(id);
    }

    private Long generateId() {
        return globalId++;
    }
}
