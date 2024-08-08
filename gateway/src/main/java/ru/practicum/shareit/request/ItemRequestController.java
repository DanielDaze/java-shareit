package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequest;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody ItemRequest request, @RequestHeader(USER_ID_HEADER) long userId) {
        log.info("POST /requests <- {} with userId {}", request, userId);
        return itemRequestClient.create(request, userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> find(@PathVariable long requestId) {
        log.info("GET /requests/{}", requestId);
        return itemRequestClient.find(requestId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllByUserId(@RequestHeader(USER_ID_HEADER) long userId) {
        log.info("GET /requests <- with userId {}", userId);
        return itemRequestClient.findAllByUserId(userId);
    }


    @GetMapping("/all")
    public ResponseEntity<Object> findAllOther(@RequestHeader(USER_ID_HEADER) long userId, @RequestParam long from, @RequestParam int size) {
        log.info("GET /requests/all <- with userId {}, from={}, size={}", userId, from, size);
        return itemRequestClient.findAllOther(userId, from, size);
    }
}
