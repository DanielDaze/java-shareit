package jru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@ContextConfiguration(classes = {ShareItServer.class})
public class UserControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService userService;

    @Autowired
    MockMvc mvc;

    final User userToSave = new User();
    final User userToReturn = new User();

    @Test
    void createUser() throws Exception {
        userToSave.setName("name");
        userToSave.setEmail("mail@mail.com");

        userToReturn.setId(1);
        userToReturn.setEmail(userToSave.getEmail());
        userToReturn.setName(userToSave.getName());
        when(userService.create(userToSave)).thenReturn(userToReturn);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userToSave))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userToReturn.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userToReturn.getName())))
                .andExpect(jsonPath("$.email", is(userToReturn.getEmail())));
    }

    @Test
    void getAll() throws Exception {
        when(userService.getAll()).thenReturn(List.of(userToReturn));
        mvc.perform(get("/users")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));

        when(userService.get(1)).thenReturn(userToReturn);
        mvc.perform(get("/users/1")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(userToReturn.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userToReturn.getName())))
                .andExpect(jsonPath("$.email", is(userToReturn.getEmail())));
    }

    @Test
    void update() throws Exception {
        User newUser = new User();
        newUser.setName("new_name");
        newUser.setEmail("new_mail@mail.com");

        User updated = new User();
        updated.setEmail(newUser.getEmail());
        updated.setName(newUser.getName());
        updated.setId(1);
        when(userService.update(1, newUser)).thenReturn(updated);

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(newUser))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(updated.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(updated.getName())))
                .andExpect(jsonPath("$.email", is(updated.getEmail())));
    }

    @Test
    void deleteUserDto() throws Exception {
        mvc.perform(delete("/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(userService).delete(1L);
    }
}
