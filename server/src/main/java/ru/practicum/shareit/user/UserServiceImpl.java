package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.exception.NoSuchDataException;

import java.util.Collection;

@RequiredArgsConstructor
@Service
@Validated
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Collection<User> getAll() {
        Collection<User> users = userRepository.findAll();
        log.info("GET /users -> {}", users);
        return users;
    }

    @Transactional(readOnly = true)
    public User get(long id) {
        User user = userRepository.findById(id).orElseThrow(NoSuchDataException::new);
        log.info("GET /users/{} -> {}", id, user);
        return user;
    }

    @Transactional
    public User create(User user) {
        User userToReturn = userRepository.save(user);
        log.info("POST /users -> {}", userToReturn);
        return userToReturn;
    }

    @Transactional
    public User update(long id, User user) {
        User foundUser = userRepository.findById(id).orElseThrow(NoSuchDataException::new);
        if (user.getName() == null) {
            user.setName(foundUser.getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(foundUser.getEmail());
        }
        user.setId(id);
        User userToReturn = userRepository.save(user);
        log.info("PATCH /users/{} -> {}", id, userToReturn);
        return userToReturn;
    }

    @Transactional
    public void delete(long id) {
        userRepository.deleteById(id);
        log.info("DELETE /users/{}: user deleted", id);
    }
}
