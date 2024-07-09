package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    @GetMapping
    public Collection<ItemDto> getAllByUserId(long userId);

    public ItemDto get(long id);

    public ItemDto create(ItemDto item, long userId);

    public ItemDto update(ItemDto item, long id, long userId);

    public Collection<ItemDto> search(String text);
}
