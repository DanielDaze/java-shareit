package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.Collection;

public interface BookingService {
    Booking create(BookingDto booking, long userId);

    Booking approve(long bookingId, boolean approved, long userId);

    Booking get(long bookingId, long userId);

    Collection<Booking> getByBookerId(long userId, BookingSearch state);

    Collection<Booking> getByOwnerId(long userId, BookingSearch state);
}
