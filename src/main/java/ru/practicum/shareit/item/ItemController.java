package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Marker.Create;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemService itemService;
    private final static String USER_ID_HEADER = "X-Sharer-User-Id";

    @GetMapping
    public Collection<ItemDto> getAllByUserId(@RequestHeader(USER_ID_HEADER) long userId) {
        log.info("GET /items");
        return itemService.getAllByUserId(userId);
    }

    @GetMapping("/{id}")
    public ItemDto get(@PathVariable("id") long id) {
        log.info("GET /items <- {}", id);
        return itemService.get(id);
    }

    @PostMapping
    @Validated(Create.class)
    public ItemDto create(@Valid @RequestBody ItemDto item, @RequestHeader(USER_ID_HEADER) long userId) {
        log.info("POST /items <- {} with userId {}", item, userId);
        return itemService.create(item, userId);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestBody ItemDto item, @PathVariable("id") long id,
                          @RequestHeader(name = USER_ID_HEADER) long userId) {
        log.info("PATCH /items/{} <- {} with userId {}", item, id, userId);
        return itemService.update(item, id, userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> search(@RequestParam("text") String text) {
        log.info("GET /items/search?text={}", text);
        return itemService.search(text);
    }
}
