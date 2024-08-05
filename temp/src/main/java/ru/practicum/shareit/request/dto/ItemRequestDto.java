package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
public class ItemRequestDto {
    private long id;

    private String description;

    private User requestor;

    private LocalDateTime created;

    private Collection<Item> items;
}
