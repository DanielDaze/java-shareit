package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingInfo;
import ru.practicum.shareit.item.comment.CommentDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private long id;

    private String name;

    private String description;

    private Boolean available;

    private BookingInfo lastBooking;

    private BookingInfo nextBooking;

    private List<CommentDto> comments;

    private Long requestId;

    public ItemDto(long id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
