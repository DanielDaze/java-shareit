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
    private final UserService userService;

    @GetMapping
    public Collection<User> getAll() {
        log.info("GET /users");
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable long id) {
        log.info("GET /users/{}", id);
        return userService.get(id);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("POST /users <- {}", user);
        return userService.create(user);
    }

    @PatchMapping("/{id}")
    public User update(@PathVariable long id, @Valid @RequestBody User user) {
        log.info("PATCH /users/{} <- {}", id, user);
        return userService.update(id, user);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable long id) {
        log.info("DELETE /users/{}", id);
        userService.delete(id);
        return "Пользователь успешно удален!";
    }
}
