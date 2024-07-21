package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NoSuchDataException;
import ru.practicum.shareit.exception.WrongUserException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public Collection<ItemDto> getAllByUserId(long userId) {
        return itemRepository.findAllByOwnerId(userId).stream().map(ItemMapper::toItemDto).toList();
    }

    @Override
    public ItemDto get(long id) {
        Item item = itemRepository.findById(id).orElseThrow(NoSuchDataException::new);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto create(ItemDto item, long userId) {
        User owner = Optional.of(userService.get(userId)).orElseThrow(NoSuchDataException::new);
        Item itemToSave = ItemMapper.toItem(item);
        return ItemMapper.toItemDto(itemRepository.create(item, owner));
    }

    @Override
    public ItemDto update(ItemDto item, long id, long userId) {
        if (itemRepository.get(id).getOwner().getId() != userId) {
            throw new WrongUserException("Вы не являетесь владельцем этого товара!");
        } else {
            return ItemMapper.toItemDto(itemRepository.update(item, id));
        }
    }

    @Override
    public Collection<ItemDto> search(String text) {
        if (text.isBlank()) {
            return List.of();
        } else {
            return itemRepository.search(text).stream().map(ItemMapper::toItemDto).toList();
        }
    }
}
