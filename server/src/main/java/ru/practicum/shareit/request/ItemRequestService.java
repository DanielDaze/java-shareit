package ru.practicum.shareit.request;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequest create(ItemRequest request, long userId);

    ItemRequestDto find(@PathVariable long requestId);

    Collection<ItemRequestDto> findAllByUserId(long userId);

    Collection<ItemRequest> findAllOther(long userId, long from, int size);
}
