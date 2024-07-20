package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.Marker.Create;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NoSuchDataException;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
@Validated
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public Collection<User> getAll() {
        return userRepository.findAll();
    }

    public User get(long id) {
        return userRepository.findById(id).orElseThrow(NoSuchDataException::new);
    }

    @Validated(Create.class)
    public User create(@Valid User user) {
        List<String> emails = userRepository.findAll().stream().map(User::getEmail).toList();
        if (emails.contains(user.getEmail())) {
            throw new DuplicatedDataException("Пользователь с такой почтой уже зарегистрирован");
        } else {
            return userRepository.save(user);
        }
    }

    public User update(long id, User user) {
        List<String> emails = userRepository.findAll().stream().filter(curUser -> curUser.getId() != id).map(User::getEmail).toList();
        if (emails.contains(user.getEmail())) {
            throw new DuplicatedDataException("Пользователь с таким почтовым адресом уже создан");
        }
        
        User foundUser = userRepository.findById(id).orElseThrow(NoSuchDataException::new);
        if (user.getName() == null) {
            user.setName(foundUser.getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(foundUser.getEmail());
        }
        user.setId(id);
        return userRepository.save(user);
    }

    public void delete(long id) {
        userRepository.deleteById(id);
    }
}
