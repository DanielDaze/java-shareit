package ru.practicum.shareit.booking.dto;

import lombok.Data;

import java.time.LocalDateTime;

import static ru.practicum.shareit.booking.dto.BookingStatus.WAITING;

@Data
public class BookingInfo {
    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Long itemId;

    private Long bookerId;

    private BookingStatus status = WAITING;
}
