package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingSearch;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody BookingDto booking, @RequestHeader(USER_ID_HEADER) long userId) {
        log.info("POST /bookings <- {} with userId {}", booking, userId);
        return bookingClient.create(booking, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(@RequestHeader(USER_ID_HEADER) Long userId,
                                          @PathVariable Long bookingId,
                                          @RequestParam("approved") Boolean approved) {
        return bookingClient.approve(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> get(@PathVariable long bookingId, @RequestHeader(USER_ID_HEADER) long userId) {
        log.info("GET /bookings/{} <- with userId {}", bookingId, userId);
        return bookingClient.get(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getByBookerId(@RequestHeader(USER_ID_HEADER) long userId,
                                             @RequestParam(value = "state", defaultValue = "ALL") BookingSearch state) {
        log.info("GET /bookings <- with userId {} and state {}", userId, state);
        return bookingClient.getByBookerId(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getByOwnerId(@RequestHeader(USER_ID_HEADER) long userId,
                                            @RequestParam(value = "state", defaultValue = "ALL") BookingSearch state) {
        log.info("GET /bookings/owner <- with userId {} and state {}", userId, state);
        return bookingClient.getByOwnerId(userId, state);
    }
}
