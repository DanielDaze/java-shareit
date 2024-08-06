package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Marker;
import ru.practicum.shareit.user.dto.User;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("GET /users");
        return userClient.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable long id) {
        log.info("GET /users/{}", id);
        return userClient.get(id);
    }

    @PostMapping
    @Validated(Marker.Create.class)
    public ResponseEntity<Object> create(@Valid @RequestBody User user) {
        log.info("POST /users <- {}", user);
        return userClient.create(user);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable long id, @Valid @RequestBody User user) {
        log.info("PATCH /users/{} <- {}", id, user);
        return userClient.update(id, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable long id) {
        log.info("DELETE /users/{}", id);
        return userClient.delete(id);
    }
}
