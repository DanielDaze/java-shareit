package jru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {ShareItServer.class})
public class UserServiceTest {
    @Autowired
    private final UserServiceImpl userService;

    @MockBean
    UserRepository userRepository;

    final User userToSave = new User();
    final User userToReturn = new User();

    @Test
    void create() {
        userToSave.setName("name");
        userToSave.setEmail("mail@mail.com");

        userToReturn.setId(1);
        userToReturn.setEmail(userToSave.getEmail());
        userToReturn.setName(userToSave.getName());
        when(userRepository.save(userToSave)).thenReturn(userToReturn);

        Assertions.assertEquals(userToReturn, userService.create(userToSave));
    }

    @Test
    void getAndGetAll() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userToReturn));
        when(userRepository.findAll()).thenReturn(List.of(userToReturn));

        Assertions.assertEquals(userToReturn, userService.get(1));
        Assertions.assertEquals(1, userService.getAll().size());
    }

    @Test
    void update() {
        User newUser = new User();
        newUser.setName("new_name");
        newUser.setEmail("new_mail@mail.com");
        User updated = new User();
        updated.setId(1);
        updated.setName(newUser.getName());
        updated.setEmail(newUser.getEmail());
        when(userRepository.save(newUser)).thenReturn(updated);
    }

    @Test
    void delete() {
        userService.delete(1);

        verify(userRepository).deleteById(1L);
    }
}
