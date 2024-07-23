package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.NoSuchDataException;
import ru.practicum.shareit.exception.WrongUserException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;

    @Override
    public Collection<ItemDto> getAllByUserId(long userId) {
        return setBookings(itemRepository.findAllByOwnerId(userId).stream().map(ItemMapper::toItemDto).toList());
    }

    @Override
    public ItemDto get(long id, long userId) {
        Item item = itemRepository.findById(id).orElseThrow(NoSuchDataException::new);
        User user = Optional.of(userService.get(userId)).orElseThrow(NoSuchDataException::new);
        User owner = item.getOwner();
        ItemDto itemDto = ItemMapper.toItemDto(item);
        if (user.getId()!= owner.getId()) {
            return itemDto;
        }
        return setBookings(List.of(itemDto)).getFirst();
    }

    @Override
    public ItemDto create(ItemDto item, long userId) {
        User owner = Optional.of(userService.get(userId)).orElseThrow(NoSuchDataException::new);
        Item itemToSave = ItemMapper.toItem(item);
        itemToSave.setOwner(owner);
        return ItemMapper.toItemDto(itemRepository.save(itemToSave));
    }

    @Override
    public ItemDto update(ItemDto item, long id, long userId) {
        Item existingItem = itemRepository.findById(id).orElseThrow(NoSuchDataException::new);
        if (existingItem.getOwner().getId() != userId) {
            throw new WrongUserException("Вы не являетесь владельцем этого товара!");
        } else {
            if (item.getName() != null) {
                existingItem.setName(item.getName());
            }
            if (item.getAvailable() != null) {
                existingItem.setAvailable(item.getAvailable());
            }
            if (item.getDescription() != null) {
                existingItem.setDescription(item.getDescription());
            }
            return ItemMapper.toItemDto(itemRepository.save(existingItem));
        }
    }

    @Override
    public Collection<ItemDto> search(String text) {
        if (text.isBlank()) {
            return List.of();
        } else {
            List<ItemDto> items = itemRepository.findAllByAvailableTrueAndDescriptionContainingIgnoreCase(text)
                    .stream()
                    .map(ItemMapper::toItemDto).toList();
            return setBookings(items);
        }
    }

    private List<ItemDto> setBookings(List<ItemDto> items) {
        LocalDateTime now = LocalDateTime.now();
        for (ItemDto item : items) {
            Optional<Booking> nextBooking = bookingRepository.findTop1BookingByItem_IdAndEndAfterAndStatusOrderByEndAsc(
                    item.getId(), now, BookingStatus.APPROVED);
            nextBooking.ifPresent(booking -> item.setNextBooking(BookingMapper.toBookingInfo(booking)));
            Optional<Booking> lastBooking = bookingRepository.findTop1BookingByItem_IdAndEndBeforeAndStatusOrderByEndDesc(
                    item.getId(), now, BookingStatus.APPROVED);
            lastBooking.ifPresent(booking -> item.setLastBooking(BookingMapper.toBookingInfo(booking)));
        }
        return items;
    }
}
