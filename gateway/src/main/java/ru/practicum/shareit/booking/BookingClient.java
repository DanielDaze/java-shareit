package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingSearch;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> create(BookingDto booking, long userId) {
        return post("", userId, booking);
    }

    public ResponseEntity<Object> approve(long bookingId, boolean approved, long userId) {
        // TODO
        Map<String, Object> parameters = Map.of(
                "approved", String.valueOf(approved)
        );
        return patch("/" + bookingId, userId, parameters);
    }

    public ResponseEntity<Object> get(long bookingId, long userId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getByBookerId(long userId, BookingSearch state) {
        Map<String, Object> parameters = Map.of(
                "state", state.name()
        );
        return get("", userId, parameters);
    }

    public ResponseEntity<Object> getByOwnerId(long userId, BookingSearch state) {
        Map<String, Object> parameters = Map.of(
                "state", state.name()
        );
        return get("/owner", userId, parameters);
    }
}