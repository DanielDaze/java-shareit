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
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public Collection<User> getAll() {
        return userRepository.getAll();
    }

    public User get(long id) {
        return userRepository.get(id);
    }

    @Validated(Create.class)
    public User create(@Valid User user) {
        return userRepository.create(user);
    }

    public User update(long id, User user) {
        return userRepository.update(id, user);
    }

    public void delete(long id) {
        userRepository.delete(id);
    }
}
