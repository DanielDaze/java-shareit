package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.ItemUnavailableException;
import ru.practicum.shareit.exception.NoSuchDataException;
import ru.practicum.shareit.exception.WrongUserException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public Collection<ItemDto> getAllByUserId(long userId) {
        List<ItemDto> items = setBookings(itemRepository.findAllByOwnerId(userId).stream().map(ItemMapper::toItemDto).toList());
        for (ItemDto item : items) {
            List<Comment> comments = commentRepository.findAllByItem_Id(item.getId());
            if (!comments.isEmpty()) {

                item.setComments(comments);
            }
        }
        return items;
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
        List<Comment> comments = commentRepository.findAllByItem_Id(id);
        if (!comments.isEmpty()) {
            itemDto.setComments(comments);
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

    @Override
    public Comment addComment(CommentDto comment, long itemId, long userId) {
        User user = userRepository.findById(userId).orElseThrow(NoSuchDataException::new);
        Item item = itemRepository.findById(userId).orElseThrow(NoSuchDataException::new);
        if (validateAuthor(userId, itemId)) {
            Comment commentToSave = new Comment();
            commentToSave.setItem(item);
            commentToSave.setAuthor(user);
            commentToSave.setText(comment.getText());
            return commentRepository.save(commentToSave);
        } else {
            throw new ItemUnavailableException("Вы не пользовались этим товаром");
        }
    }

    private boolean validateAuthor(long userId, long itemId) {
        List<Booking> userBookings = bookingRepository.findAllByBookerIdAndItem_IdAndStatusAndEndBefore(userId, itemId, BookingStatus.APPROVED, LocalDateTime.now());
        return !userBookings.isEmpty();
    }
}
