package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Marker;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> getAllByUserId(@RequestHeader(USER_ID_HEADER) long userId) {
        log.info("GET /items");
        return itemClient.getAllByUserId(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") long id, @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("GET /items/{}", id);
        return itemClient.get(id, userId);
    }

    @PostMapping
    @Validated(Marker.Create.class)
    public ResponseEntity<Object> create(@Valid @RequestBody ItemDto item, @RequestHeader(USER_ID_HEADER) long userId) {
        log.info("POST /items <- {} with userId {}", item, userId);
        return itemClient.create(userId, item);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestBody ItemDto item, @PathVariable("id") long id,
                          @RequestHeader(name = USER_ID_HEADER) long userId) {
        log.info("PATCH /items/{} <- {} with userId {}", id, item, userId);
        return itemClient.update(item, id, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam("text") String text) {
        log.info("GET /items/search?text={}", text);
        return itemClient.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@Valid @RequestBody CommentDto comment, @PathVariable long itemId, @RequestHeader(USER_ID_HEADER) long userId) {
        log.info("POST /items/{}/comment", itemId);
        return itemClient.addComment(comment, itemId, userId);
    }
}
