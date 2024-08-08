package jru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

@DataJpaTest
@ContextConfiguration(classes = {ShareItServer.class})
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void save() {
        User userToSave = new User();
        userToSave.setName("name");
        userToSave.setEmail("email@email.com");
        User saved = userRepository.save(userToSave);
        userToSave.setId(1);
        Assertions.assertEquals(userToSave, saved);
    }

    @Test
    void update() {
        User updated = new User();
        updated.setId(1);
        updated.setName("new_name");
        updated.setEmail("new_mail@mail.com");
        Assertions.assertEquals(updated, userRepository.save(updated));
    }
}
