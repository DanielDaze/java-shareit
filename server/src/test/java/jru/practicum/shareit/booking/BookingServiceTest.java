package jru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {ShareItServer.class})
public class BookingServiceTest {
    @Autowired
    BookingServiceImpl bookingService;

    @MockBean
    BookingRepository bookingRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    ItemRepository itemRepository;

    @Test
    void createBooking() {
        LocalDateTime start = LocalDateTime.of(2024, 11, 20, 11, 30, 0);
        LocalDateTime end = LocalDateTime.of(2024, 11, 27, 11, 30);
        final BookingDto bookingToSave = new BookingDto(start, end, 1L);
        final Booking bookingToReturn = new Booking();
        bookingToReturn.setStart(start);
        bookingToReturn.setEnd(end);
        bookingToReturn.setId(1L);
        bookingToReturn.setStatus(BookingStatus.WAITING);

        final User userToReturn = new User();
        userToReturn.setId(1);
        userToReturn.setEmail("mail");
        userToReturn.setName("name");
        bookingToReturn.setBooker(userToReturn);

        final Item itemToReturn = new Item();
        itemToReturn.setId(1L);
        itemToReturn.setName("name");
        itemToReturn.setDescription("desc");
        itemToReturn.setAvailable(true);
        itemToReturn.setOwner(userToReturn);

        when(userRepository.findById(2L)).thenReturn(Optional.of(userToReturn));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(itemToReturn));
        when(bookingRepository.save(any())).thenReturn(bookingToReturn);

        Assertions.assertEquals(bookingToReturn, bookingService.create(bookingToSave, 2));
    }

    @Test
    void approve() {
        LocalDateTime start = LocalDateTime.of(2024, 11, 20, 11, 30, 0);
        LocalDateTime end = LocalDateTime.of(2024, 11, 27, 11, 30);

        User owner = new User();
        owner.setId(1L);
        owner.setName("owner");
        owner.setEmail("mail@mail.com");

        Item item = new Item();
        item.setId(1L);
        item.setName("name");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(owner);

        User booker = new User();
        booker.setId(3L);
        booker.setName("booker");
        booker.setEmail("booker@email.com");

        Booking booking = new Booking();
                booking.setId(1L);
                booking.setStart(start);
                booking.setEnd(end);
                booking.setItem(item);
                booking.setBooker(booker);
                booking.setStatus(BookingStatus.WAITING);

        Booking approved = new Booking();
        approved.setId(1L);
        approved.setStart(start);
        approved.setEnd(end);
        approved.setItem(item);
        approved.setBooker(booker);
        approved.setStatus(BookingStatus.APPROVED);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenReturn(approved);

        Assertions.assertEquals(approved, bookingService.approve(1, true, 1));
    }

    @Test
    void getTest() {
        LocalDateTime start = LocalDateTime.of(2024, 11, 20, 11, 30, 0);
        LocalDateTime end = LocalDateTime.of(2024, 11, 27, 11, 30);

        User owner = new User();
        owner.setId(1L);
        owner.setName("owner");
        owner.setEmail("mail@mail.com");

        Item item = new Item();
        item.setId(1L);
        item.setName("name");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(owner);

        User booker = new User();
        booker.setId(2L);
        booker.setName("booker");
        booker.setEmail("booker@email.com");

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));

        Assertions.assertEquals(booking, bookingService.get(1, 2));
    }

    @Test
    void getByBookerId() {
        LocalDateTime start = LocalDateTime.of(2024, 11, 20, 11, 30, 0);
        LocalDateTime end = LocalDateTime.of(2024, 11, 27, 11, 30);

        User owner = new User();
        owner.setId(1L);
        owner.setName("owner");
        owner.setEmail("mail@mail.com");

        Item item = new Item();
        item.setId(1L);
        item.setName("name");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(owner);

        User booker = new User();
        booker.setId(2L);
        booker.setName("booker");
        booker.setEmail("booker@email.com");

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBookerIdAndStatusOrderByIdDesc(2L, BookingStatus.WAITING)).thenReturn(List.of(booking));

        Assertions.assertEquals(1, bookingService.getByBookerId(2L, BookingSearch.WAITING).size());
    }
}
