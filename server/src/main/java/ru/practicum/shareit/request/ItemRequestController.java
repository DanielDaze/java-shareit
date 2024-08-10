package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequest create(@RequestBody ItemRequest request, @RequestHeader(USER_ID_HEADER) long userId) {
        log.info("POST /requests <- {} with userId {}", request, userId);
        return itemRequestService.create(request, userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto find(@PathVariable long requestId) {
        log.info("GET /requests/{}", requestId);
        return itemRequestService.find(requestId);
    }

    @GetMapping
    public Collection<ItemRequestDto> findAllByUserId(@RequestHeader(USER_ID_HEADER) long userId) {
        log.info("GET /requests <- with userId {}", userId);
        return itemRequestService.findAllByUserId(userId);
    }


    @GetMapping("/all")
    public Collection<ItemRequest> findAllOther(@RequestHeader(USER_ID_HEADER) long userId, @RequestParam long from, @RequestParam int size) {
        log.info("GET /requests/all <- with userId {}, from={}, size={}", userId, from, size);
        return itemRequestService.findAllOther(userId, from, size);
    }
}
