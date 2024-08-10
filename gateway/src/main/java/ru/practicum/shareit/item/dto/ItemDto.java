package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.Marker;
import ru.practicum.shareit.booking.dto.BookingInfo;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private long id;
    @NotBlank(groups = Marker.Create.class)
    private String name;

    @NotBlank(groups = Marker.Create.class)
    private String description;

    @NotNull(groups = Marker.Create.class)
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
