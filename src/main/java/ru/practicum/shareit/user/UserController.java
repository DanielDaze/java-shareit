package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService service;

    @GetMapping
    public Collection<User> getAll() {
        log.info("GET /users");
        Collection<User> users = service.getAll();
        return users;
    }

    @GetMapping("/{id}")
    public User get(@PathVariable long id) {
        log.info("GET /users/{}", id);
        User user = service.get(id);
        return user;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("POST /users <- {}", user);
        User userToReturn = service.create(user);
        return userToReturn;
    }

    @PatchMapping("/{id}")
    public User update(@PathVariable long id, @Valid @RequestBody User user) {
        log.info("PATCH /users/{} <- {}", id, user);
        User userToReturn = service.update(id, user);
        return userToReturn;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable long id) {
        log.info("DELETE /users/{}", id);
        service.delete(id);
        return "Пользователь успешно удален!";
    }
}
