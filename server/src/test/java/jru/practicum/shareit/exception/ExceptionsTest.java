package jru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.Error;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExceptionsTest {
    @Test
    void getError() {
        Error response = new Error("test");
        assertEquals("test", response.getError());
    }
}
