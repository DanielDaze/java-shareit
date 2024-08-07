package jru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

@DataJpaTest
@ContextConfiguration(classes = {ShareItServer.class})
public class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void save() {
        Item itemToSave = new Item();
        itemToSave.setName("name");
        itemToSave.setDescription("hammer");
        itemToSave.setAvailable(false);
        User user = new User();
        user.setName("name");
        user.setEmail("mail@mail.com");
        user.setId(1);
        userRepository.save(user);
        itemToSave.setOwner(user);
        Item saved = itemRepository.save(itemToSave);
        itemToSave.setId(1L);
        Assertions.assertEquals(itemToSave, saved);
    }

    @Test
    void update() {
        Item itemToSave = new Item();
        itemToSave.setName("name");
        itemToSave.setDescription("hammer");
        itemToSave.setAvailable(false);
        User user = new User();
        user.setName("name");
        user.setEmail("mail@mail.com");
        user.setId(1);
        userRepository.save(user);
        itemToSave.setOwner(user);
        itemRepository.save(itemToSave);

        Item updated = new Item();
        updated.setId(1L);
        updated.setName("new_name");
        updated.setDescription("new_description");
        updated.setAvailable(true);
        updated.setOwner(user);
        Assertions.assertEquals(updated, itemRepository.save(updated));
    }

    @Test
    void findAllByOwnerId() {
        Item itemToSave = new Item();
        itemToSave.setName("name");
        itemToSave.setDescription("hammer");
        itemToSave.setAvailable(false);
        User user = new User();
        user.setName("name");
        user.setEmail("mail@mail.com");
        user.setId(1);
        userRepository.save(user);
        itemToSave.setOwner(user);
        itemRepository.save(itemToSave);
        Assertions.assertEquals(1, itemRepository.findAllByOwnerId(1).size());
    }
}
