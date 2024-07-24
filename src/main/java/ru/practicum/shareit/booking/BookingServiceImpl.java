package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.DateConflictException;
import ru.practicum.shareit.exception.ItemUnavailableException;
import ru.practicum.shareit.exception.NoSuchDataException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Booking create(BookingDto dto, long userId) {
        Booking bookingToSave = BookingMapper.toBooking(dto);
        User user = userRepository.findById(userId).orElseThrow(NoSuchDataException::new);
        Item item = itemRepository.findById(dto.getItemId()).orElseThrow(NoSuchDataException::new);
        if (item.getOwner().getId() == userId) {
            throw new NoSuchDataException("Вы являетесь владельцем этого товара!");
        }
        if (item.getAvailable() == null || !item.getAvailable()) {
            throw new ItemUnavailableException("Предмет недоступен для бронирования!");
        }
        if (dto.getEnd().isBefore(dto.getStart()) || dto.getStart().equals(dto.getEnd())) {
            throw new DateConflictException("Вы неверно ввели даты брони!");
        }
        bookingToSave.setBooker(user);
        bookingToSave.setItem(item);
        return bookingRepository.save(bookingToSave);
    }

    @Override
    public Booking approve(long bookingId, boolean approved, long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(NoSuchDataException::new);
        if (booking.getItem().getOwner().getId() != userId) {
            throw new NoSuchDataException("Вы не можете изменить статус этой брони!");
        }
        if (booking.getStatus() == BookingStatus.APPROVED) {
            throw new ItemUnavailableException("Вы уже подтвердили эту бронь!");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return bookingRepository.save(booking);
    }

    @Override
    public Booking get(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(NoSuchDataException::new);
        userRepository.findById(userId).orElseThrow(NoSuchDataException::new);
        List<Long> correctIds = List.of(booking.getItem().getOwner().getId(), booking.getBooker().getId());
        if (!correctIds.contains(userId)) {
            throw new NoSuchDataException("Вы не можете посмотреть информацию об этой брони!");
        }
        return booking;
    }

    @Override
    public Collection<Booking> getByBookerId(long userId, BookingSearch state) {
        userRepository.findById(userId).orElseThrow(NoSuchDataException::new);
        return searchByBooker(userId, state);
    }

    @Override
    public Collection<Booking> getByOwnerId(long userId, BookingSearch state) {
        userRepository.findById(userId).orElseThrow(NoSuchDataException::new);
        return searchByOwner(userId, state);
    }

    private Collection<Booking> searchByBooker(long userId, BookingSearch state) {
        switch (state) {
            case WAITING -> {
                return bookingRepository.findAllByBookerIdAndStatusOrderByIdDesc(userId, BookingStatus.WAITING);
            } case PAST -> {
                return bookingRepository.findAllByBookerIdAndEndBeforeOrderByIdDesc(userId, LocalDateTime.now());
            } case CURRENT -> {
                LocalDateTime now = LocalDateTime.now();
                return bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByIdAsc(userId, now, now);
            } case FUTURE -> {
                LocalDateTime now = LocalDateTime.now();
                return bookingRepository.findAllByBookerIdAndStartAfterOrderByIdDesc(userId, now);
            } case REJECTED -> {
                return bookingRepository.findAllByBookerIdAndStatusOrderByIdDesc(userId, BookingStatus.REJECTED);
            } default -> {
                return bookingRepository.findAllByBookerIdOrderByIdDesc(userId);
            }
        }
    }

    private Collection<Booking> searchByOwner(long userId, BookingSearch state) {
        switch (state) {
            case WAITING -> {
                return bookingRepository.findAllByItem_Owner_IdAndStatusOrderByIdDesc(userId, BookingStatus.WAITING);
            } case PAST -> {
                return bookingRepository.findAllByItem_Owner_IdAndEndBeforeOrderByIdDesc(userId, LocalDateTime.now());
            } case CURRENT -> {
                LocalDateTime now = LocalDateTime.now();
                return bookingRepository.findAllByItem_Owner_IdAndStartBeforeAndEndAfterOrderByIdDesc(userId, now, now);
            } case FUTURE -> {
                LocalDateTime now = LocalDateTime.now();
                return bookingRepository.findAllByItem_Owner_IdAndStartAfterOrderByIdDesc(userId, now);
            } case REJECTED -> {
                return bookingRepository.findAllByItem_Owner_IdAndStatusOrderByIdDesc(userId, BookingStatus.REJECTED);
            } default -> {
                return bookingRepository.findAllByItem_Owner_IdOrderByIdDesc(userId);
            }
        }
    }
}
