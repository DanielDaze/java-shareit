package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.Marker.Create;

import java.util.Collection;

@RequiredArgsConstructor
@Service
@Validated
public class UserService {
    private final UserRepository repository;

    public Collection<User> getAll() {
        return repository.getAll();
    }

    public User get(long id) {
        return repository.get(id);
    }

    @Validated(Create.class)
    public User create(@Valid User user) {
        return repository.create(user);
    }

    public User update(long id, User user) {
        return repository.update(id, user);
    }

    public void delete(long id) {
        repository.delete(id);
    }
}
