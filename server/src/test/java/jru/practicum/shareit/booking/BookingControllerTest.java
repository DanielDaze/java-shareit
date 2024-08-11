package jru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@ContextConfiguration(classes = {ShareItServer.class})
public class BookingControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookingService bookingService;

    @Autowired
    MockMvc mvc;

    @Test
    void createBookingReturnsBookingId1() throws Exception {
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

        final ItemDto itemToReturn = new ItemDto();
        itemToReturn.setId(1L);
        itemToReturn.setName("name");
        itemToReturn.setDescription("desc");
        itemToReturn.setAvailable(true);


        when(bookingService.create(bookingToSave, 1)).thenReturn(bookingToReturn);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingToSave))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingToReturn.getId()), Long.class));
    }

    @Test
    void approveReturnsApprovedBooking() throws Exception {
        LocalDateTime start = LocalDateTime.of(2024, 11, 20, 11, 30, 0);
        LocalDateTime end = LocalDateTime.of(2024, 11, 27, 11, 30);
        Booking approved = new Booking();
        approved.setId(1L);
        approved.setStart(start);
        approved.setEnd(end);
        approved.setStatus(BookingStatus.APPROVED);
        when(bookingService.approve(1, true, 1)).thenReturn(approved);
        mvc.perform(patch("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.status", is("APPROVED")));
    }

    @Test
    void getReturnsBookingId1() throws Exception {
        LocalDateTime start = LocalDateTime.of(2024, 11, 20, 11, 30, 0);
        LocalDateTime end = LocalDateTime.of(2024, 11, 27, 11, 30);

        Booking approved = new Booking();
        approved.setId(1L);
        approved.setStart(start);
        approved.setEnd(end);
        approved.setStatus(BookingStatus.APPROVED);
        when(bookingService.get(1, 1)).thenReturn(approved);
        mvc.perform(get("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.status", is("APPROVED")));
    }

    @Test
    void getByBookerIdTestReturnsListSize1() throws Exception {
        LocalDateTime start = LocalDateTime.of(2024, 11, 20, 11, 30, 0);
        LocalDateTime end = LocalDateTime.of(2024, 11, 27, 11, 30);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setStatus(BookingStatus.APPROVED);
        when(bookingService.getByBookerId(1, BookingSearch.FUTURE)).thenReturn(List.of(booking));
        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "FUTURE")
                        .accept(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getByOwnerIdReturnsListSize1() throws Exception {
        LocalDateTime start = LocalDateTime.of(2024, 11, 20, 11, 30, 0);
        LocalDateTime end = LocalDateTime.of(2024, 11, 27, 11, 30);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setStatus(BookingStatus.APPROVED);
        when(bookingService.getByOwnerId(1, BookingSearch.FUTURE)).thenReturn(List.of(booking));
        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "FUTURE")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
