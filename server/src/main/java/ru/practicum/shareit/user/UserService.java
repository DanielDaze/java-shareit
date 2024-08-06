package ru.practicum.shareit.user;

import java.util.Collection;

public interface UserService {
    Collection<User> getAll();

    User get(long id);

    User create(User user);

    User update(long id, User user);

    void delete(long id);
}
