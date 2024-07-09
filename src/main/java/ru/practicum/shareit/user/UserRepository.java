package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicatedDataException;

import java.util.*;

@Repository
public class UserRepository {
    long curId = 0;
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> getAll() {
        return users.values();
    }

    public User get(long id) {
        return users.get(id);
    }

    public User create(User user) {
        List<String> emails = users.values().stream().map(User::getEmail).toList();
        if (emails.contains(user.getEmail())) {
            throw new DuplicatedDataException("Пользователь с таким почтовым адресом уже создан");
        }

        curId++;
        user.setId(curId);
        users.put(curId, user);
        return user;
    }

    public User update(long id, User user) {
        List<String> emails = users.values().stream().filter(curUser -> curUser.getId() != id).map(User::getEmail).toList();
        if (emails.contains(user.getEmail())) {
            throw new DuplicatedDataException("Пользователь с таким почтовым адресом уже создан");
        }

        User oldUser = users.get(id);
        if (user.getName() == null) {
            user.setName(oldUser.getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(oldUser.getEmail());
        }
        user.setId(id);
        users.replace(id, user);
        return user;
    }

    public void delete(long id) {
        users.remove(id);
    }
}
