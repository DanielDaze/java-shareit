package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    @GetMapping
    Collection<ItemDto> getAllByUserId(long userId);

    ItemDto get(long id, long userId);

    ItemDto create(ItemDto item, long userId);

    ItemDto update(ItemDto item, long id, long userId);

    Collection<ItemDto> search(String text);

    CommentDto addComment(CommentDto comment, long itemId, long userId);
}
