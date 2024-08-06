package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;

public interface UserService {
    public Collection<User> getAll();

    public User get(long id);

    @Validated(Create.class)
    public User create(@Valid User user);

    public User update(long id, User user);

    public void delete(long id);
}
