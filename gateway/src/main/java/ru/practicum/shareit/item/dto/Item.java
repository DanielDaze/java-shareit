package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.user.dto.User;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private User owner;

    private ItemRequest request;
}
