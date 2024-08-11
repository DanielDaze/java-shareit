package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.Booking;

@UtilityClass
public class BookingMapper {
    public static Booking toBooking(BookingDto dto) {
        Booking booking = new Booking();
        booking.setStart(dto.getStart());
        booking.setEnd(dto.getEnd());
        return booking;
    }

    public static BookingInfo toBookingInfo(Booking booking) {
        BookingInfo bookingInfo = new BookingInfo();
        bookingInfo.setId(booking.getId());
        bookingInfo.setStart(booking.getStart());
        bookingInfo.setEnd(booking.getEnd());
        bookingInfo.setStatus(booking.getStatus());
        bookingInfo.setItemId(booking.getItem().getId());
        bookingInfo.setBookerId(booking.getBooker().getId());
        return bookingInfo;
    }
}
