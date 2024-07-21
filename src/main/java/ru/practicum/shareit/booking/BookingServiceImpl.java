package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    public Booking create(BookingDto dto, long userId) {
        User user = userService.get(userId);
        Item item = ItemMapper.toItem(itemService.get(dto.getItemId()));
        Booking bookingToSave = BookingMapper.toBooking(dto);
        bookingToSave.setBooker(user);
        bookingToSave.setItem(item);
        return bookingRepository.save(bookingToSave);
    }
}
