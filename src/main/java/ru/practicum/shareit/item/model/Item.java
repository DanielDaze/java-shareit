package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class Item {
    long id;
    String name;
    String description;
    boolean available;
    User owner;
    ItemRequest request;
}
