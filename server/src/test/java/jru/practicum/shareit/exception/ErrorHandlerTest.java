package jru.practicum.shareit.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.exception.DateConflictException;
import ru.practicum.shareit.exception.Error;
import ru.practicum.shareit.exception.ItemUnavailableException;
import ru.practicum.shareit.exception.NoSuchDataException;
import ru.practicum.shareit.exception.WrongUserException;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserService;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@ContextConfiguration(classes = {ShareItServer.class})
public class ErrorHandlerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService service;

    @Autowired
    MockMvc mvc;

    @Test
    void handleNoSuchDataExceptionReturnsStatus4xx() throws Exception {
        Mockito.when(service.getAll()).thenThrow(NoSuchDataException.class);
        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void handleWrongUserExceptionReturnsStatus4xx() throws Exception {
        Mockito.when(service.getAll()).thenThrow(WrongUserException.class);
        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void handleItemNotAvailableExceptionReturnsStatus4xx() throws Exception {
        Mockito.when(service.getAll()).thenThrow(ItemUnavailableException.class);
        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void handleDateConflictExceptionReturnsStatus4xx() throws Exception {
        Mockito.when(service.getAll()).thenThrow(DateConflictException.class);
        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void handleIllegalArgumentExceptionReturnsStatus4xx() throws Exception {
        Mockito.when(service.getAll()).thenThrow(IllegalArgumentException.class);
        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void errorConstructorTest() {
        ru.practicum.shareit.exception.Error response = new Error("test");
        assertEquals("test", response.getError());
    }
}
