package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.Collection;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final BookingService bookingService;

    @PostMapping
    public Booking create(@Valid @RequestBody BookingDto booking, @RequestHeader(USER_ID_HEADER) long userId) {
        return bookingService.create(booking, userId);
    }

    @PatchMapping("/{bookingId}")
    public Booking approve(@PathVariable long bookingId, @RequestParam("approved") boolean approved,
                           @RequestHeader(USER_ID_HEADER) long userId) {
        return bookingService.approve(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public Booking get(@PathVariable long bookingId, @RequestHeader(USER_ID_HEADER) long userId) {
        return bookingService.get(bookingId, userId);
    }

    @GetMapping
    public Collection<Booking> getByBookerId(@RequestHeader(USER_ID_HEADER) long userId,
                                             @RequestParam(value = "state", defaultValue = "ALL") BookingSearch state) {
        return bookingService.getByBookerId(userId, state);
    }

    @GetMapping("/owner")
    public Collection<Booking> getByOwnerId(@RequestHeader(USER_ID_HEADER) long userId,
                                            @RequestParam(value = "state", defaultValue = "ALL") BookingSearch state) {
        return bookingService.getByOwnerId(userId, state);
    }
}
