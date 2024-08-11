package jru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@ContextConfiguration(classes = {ShareItServer.class})
public class ItemControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemService itemService;

    @Autowired
    MockMvc mvc;

    final ItemDto itemToSave = new ItemDto();
    final ItemDto itemToReturn = new ItemDto();

    @Test
    void createReturnsCreatedItemId1() throws Exception {
        itemToSave.setName("name");
        itemToSave.setDescription("description");
        itemToSave.setAvailable(false);

        itemToReturn.setId(1L);
        itemToReturn.setName(itemToSave.getName());
        itemToReturn.setDescription(itemToSave.getDescription());
        itemToReturn.setAvailable(itemToSave.getAvailable());
        when(itemService.create(itemToSave, 1)).thenReturn(itemToReturn);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemToSave))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemToReturn.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemToReturn.getName())))
                .andExpect(jsonPath("$.available", is(itemToReturn.getAvailable())))
                .andExpect(jsonPath("$.description", is(itemToReturn.getDescription())));
    }

    @Test
    void getAllReturnsListSize1() throws Exception {
        when(itemService.getAllByUserId(1)).thenReturn(List.of(itemToReturn));
        mvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1))
                .andExpect(jsonPath("$", hasSize(1)));

        when(itemService.get(1, 1)).thenReturn(itemToReturn);
        mvc.perform(get("/items/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(jsonPath("$.id", is(itemToReturn.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemToReturn.getName())))
                .andExpect(jsonPath("$.available", is(itemToReturn.getAvailable())))
                .andExpect(jsonPath("$.description", is(itemToReturn.getDescription())));
    }

    @Test
    void updateReturnsUpdatedItem() throws Exception {
        ItemDto newItem = new ItemDto();
        newItem.setName("new_name");
        newItem.setAvailable(true);
        newItem.setDescription("new_description");

        ItemDto updated = new ItemDto();
        updated.setName(newItem.getName());
        updated.setAvailable(newItem.getAvailable());
        updated.setDescription(newItem.getDescription());
        updated.setId(1L);
        when(itemService.update(newItem, 1, 1)).thenReturn(updated);

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(newItem))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(jsonPath("$.id", is(updated.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(updated.getName())))
                .andExpect(jsonPath("$.description", is(updated.getDescription())))
                .andExpect(jsonPath("$.available", is(updated.getAvailable())));
    }

    @Test
    void searchReturnsListSize1() throws Exception {
        ItemDto itemWithDescReturn = new ItemDto();
        itemWithDescReturn.setName("cool_name");
        itemWithDescReturn.setDescription("hammer");
        itemWithDescReturn.setId(1);
        when(itemService.search("hammer")).thenReturn(List.of(itemWithDescReturn));
        mvc.perform(get("/items/search?text=hammer")
                .content(mapper.writeValueAsString(itemWithDescReturn))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void addCommentTestReturnsCreatedCommendId1() throws Exception {
        CommentDto comment = new CommentDto();
        comment.setText("text");

        CommentDto created = new CommentDto();
        created.setId(1);

        when(itemService.addComment(comment, 1, 1)).thenReturn(created);

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(comment))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(jsonPath("$.id", is(1)));
    }
}

