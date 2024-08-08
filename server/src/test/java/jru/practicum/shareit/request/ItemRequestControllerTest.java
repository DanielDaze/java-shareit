package jru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.nio.charset.StandardCharsets;
import java.util.List;


import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@ContextConfiguration(classes = {ShareItServer.class})
public class ItemRequestControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemRequestService requestService;

    @Autowired
    MockMvc mvc;

    @Test
    void create() throws Exception {
        ItemRequest requestToCreate = new ItemRequest();
        requestToCreate.setDescription("desc");

        ItemRequest requestCreated = new ItemRequest();
        requestCreated.setId(1);
        requestCreated.setDescription(requestToCreate.getDescription());

        when(requestService.create(requestToCreate, 1)).thenReturn(requestCreated);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(requestToCreate))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.description", is(requestCreated.getDescription())));
    }

    @Test
    void getTest() throws Exception {
        ItemRequest requestToCreate = new ItemRequest();
        requestToCreate.setDescription("desc");

        ItemRequestDto requestCreated = new ItemRequestDto();
        requestCreated.setId(1);
        requestCreated.setDescription(requestToCreate.getDescription());

        when(requestService.find(1)).thenReturn(requestCreated);

        mvc.perform(get("/requests/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.description", is(requestCreated.getDescription())));
    }

    @Test
    void findAllByUserIdTest() throws Exception {
        ItemRequest requestToCreate = new ItemRequest();
        requestToCreate.setDescription("desc");

        ItemRequestDto requestCreated = new ItemRequestDto();
        requestCreated.setId(1);
        requestCreated.setDescription(requestToCreate.getDescription());

        when(requestService.findAllByUserId(1)).thenReturn(List.of(requestCreated));

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void findAllOtherTest() throws Exception {
        ItemRequest requestToCreate = new ItemRequest();
        requestToCreate.setDescription("desc");

        ItemRequestDto requestCreated = new ItemRequestDto();
        requestCreated.setId(1);
        requestCreated.setDescription(requestToCreate.getDescription());

        when(requestService.findAllOther(1, 0, 1)).thenReturn(List.of());

        mvc.perform(get("/requests/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(1))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
