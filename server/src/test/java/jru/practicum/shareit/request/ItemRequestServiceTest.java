package jru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {ShareItServer.class})
public class ItemRequestServiceTest {
    @Autowired
    ItemRequestService requestService;

    @MockBean
    ItemRequestRepository requestRepository;

    @MockBean
    UserRepository userRepository;
    @MockBean
    ItemRepository itemRepository;

    @Test
    void createReturnsCreatedRequest() {
        final User userToReturn = new User();
        userToReturn.setId(1);
        userToReturn.setEmail("mail");
        userToReturn.setName("name");

        ItemRequest requestToCreate = new ItemRequest();
        requestToCreate.setDescription("desc");

        ItemRequest requestCreated = new ItemRequest();
        requestCreated.setId(1);
        requestCreated.setDescription(requestToCreate.getDescription());

        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(userToReturn));


        Mockito.when(requestRepository.save(requestToCreate))
                .thenReturn(requestCreated);

        Assertions.assertEquals(requestCreated, requestService.create(requestToCreate, 1));
    }

    @Test
    void findReturnsRequestId1() {
        final User userToReturn = new User();
        userToReturn.setId(1);
        userToReturn.setEmail("mail");
        userToReturn.setName("name");

        ItemRequest requestToCreate = new ItemRequest();
        requestToCreate.setDescription("desc");

        ItemRequest requestCreated = new ItemRequest();
        requestCreated.setId(1);
        requestCreated.setDescription(requestToCreate.getDescription());


        Item itemToSave = new Item();
        itemToSave.setName("name");
        itemToSave.setDescription("description");
        itemToSave.setAvailable(false);
        itemToSave.setId(1L);
        itemToSave.setRequest(requestCreated);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(userToReturn));
        Mockito.when(itemRepository.findAllByRequestId(1)).thenReturn(List.of(itemToSave));
        Mockito.when(requestRepository.findById(1L)).thenReturn(Optional.of(requestCreated));

        ItemRequestDto saved = ItemRequestMapper.toItemRequestDto(requestCreated);
        saved.setItems(List.of(itemToSave));
        Assertions.assertEquals(saved, requestService.find(1L));
    }

    @Test
    void findAllByUserIdReturnsListSize1() {
        final User userToReturn = new User();
        userToReturn.setId(1);
        userToReturn.setEmail("mail");
        userToReturn.setName("name");

        ItemRequest requestToCreate = new ItemRequest();
        requestToCreate.setDescription("desc");

        ItemRequest requestCreated = new ItemRequest();
        requestCreated.setId(1);
        requestCreated.setDescription(requestToCreate.getDescription());


        Item itemToSave = new Item();
        itemToSave.setName("name");
        itemToSave.setDescription("description");
        itemToSave.setAvailable(false);
        itemToSave.setId(1L);
        itemToSave.setRequest(requestCreated);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(userToReturn));
        Mockito.when(requestRepository.getAllByRequestorIdOrderByCreatedDesc(1)).thenReturn(List.of(requestCreated));
        Mockito.when(itemRepository.findAllByRequestId(1)).thenReturn(List.of(itemToSave));

        ItemRequestDto saved = ItemRequestMapper.toItemRequestDto(requestCreated);
        saved.setItems(List.of(itemToSave));
        Assertions.assertEquals(1, requestService.findAllByUserId(1).size());
    }
}
