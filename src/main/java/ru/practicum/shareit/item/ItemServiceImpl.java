package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.ItemUnavailableException;
import ru.practicum.shareit.exception.NoSuchDataException;
import ru.practicum.shareit.exception.WrongUserException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemDto> getAllByUserId(long userId) {
        List<ItemDto> items = setBookings(itemRepository.findAllByOwnerId(userId).stream().map(ItemMapper::toItemDto).toList());
        for (ItemDto item : items) {
            List<CommentDto> comments = commentRepository.findAllByItemId(item.getId()).stream().map(CommentMapper::toCommentDto).toList();
            item.setComments(comments);
        }
        log.info("GET /items -> {}", items);
        return items;
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto get(long id, long userId) {
        Item item = itemRepository.findById(id).orElseThrow(NoSuchDataException::new);
        User user = Optional.of(userService.get(userId)).orElseThrow(NoSuchDataException::new);
        User owner = item.getOwner();
        ItemDto itemDto = ItemMapper.toItemDto(item);
        List<CommentDto> comments = commentRepository.findAllByItemId(id).stream().map(CommentMapper::toCommentDto).toList();
        itemDto.setComments(comments);
        if (user.getId() != owner.getId()) {
            return itemDto;
        }
        ItemDto itemToReturn = setBookings(List.of(itemDto)).getFirst();
        log.info("GET /items/{} -> {}", id, itemToReturn);
        return itemToReturn;
    }

    @Override
    @Transactional
    public ItemDto create(ItemDto item, long userId) {
        User owner = Optional.of(userService.get(userId)).orElseThrow(NoSuchDataException::new);
        Item itemToSave = ItemMapper.toItem(item);
        itemToSave.setOwner(owner);
        if (item.getRequestId() != null) {
            ItemRequest itemRequest = itemRequestRepository.findById(item.getRequestId()).orElseThrow(NoSuchDataException::new);
            itemToSave.setRequest(itemRequest);
        }
        ItemDto itemToReturn =  ItemMapper.toItemDto(itemRepository.save(itemToSave));
        log.info("POST /items -> {}", itemToReturn);
        return itemToReturn;
    }

    @Override
    @Transactional
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
            ItemDto itemToReturn = ItemMapper.toItemDto(itemRepository.save(existingItem));
            log.info("PATCH /items/{} -> {}", id, itemToReturn);
            return itemToReturn;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemDto> search(String text) {
        if (text.isBlank()) {
            return List.of();
        } else {
            List<ItemDto> items = itemRepository.findAllByAvailableTrueAndDescriptionContainingIgnoreCase(text)
                    .stream()
                    .map(ItemMapper::toItemDto).toList();
            List<ItemDto> itemsToReturn = setBookings(items);
            log.info("GET /items/search?text={} -> {}", text, itemsToReturn);
            return itemsToReturn;
        }
    }

    private List<ItemDto> setBookings(List<ItemDto> items) {
        LocalDateTime now = LocalDateTime.now();
        for (ItemDto item : items) {
            Optional<Booking> lastBooking = bookingRepository.findTop1BookingByItem_IdAndStartBeforeAndStatusOrderByEndDesc(
                    item.getId(), now, BookingStatus.APPROVED);
            lastBooking.ifPresent(booking -> item.setLastBooking(BookingMapper.toBookingInfo(booking)));
            Optional<Booking> nextBooking = bookingRepository.findTop1BookingByItem_IdAndStartAfterAndStatusOrderByEndAsc(
                    item.getId(), now, BookingStatus.APPROVED);
            nextBooking.ifPresent(booking -> item.setNextBooking(BookingMapper.toBookingInfo(booking)));
        }
        return items;
    }

    @Override
    @Transactional
    public CommentDto addComment(CommentDto comment, long itemId, long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(NoSuchDataException::new);
        User author = userRepository.findById(userId).orElseThrow(NoSuchDataException::new);
        bookingRepository.findTop1BookingByItemIdAndBookerIdAndEndBeforeAndStatusOrderByEndDesc(
                itemId, userId, LocalDateTime.now(), BookingStatus.APPROVED).orElseThrow(
                () -> new ItemUnavailableException("Вы не пользовались этим товаром!"));

        Comment commentToSave = new Comment();
        commentToSave.setItem(item);
        commentToSave.setAuthor(author);
        commentToSave.setCreated(LocalDateTime.now());
        commentToSave.setText(comment.getText());
        Comment saved = commentRepository.save(commentToSave);
        CommentDto commentToReturn = CommentMapper.toCommentDto(saved);
        log.info("POST /items/{}/comment -> {}", itemId, commentToReturn);
        return commentToReturn;
    }
}
