package jru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.WrongUserException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {ShareItServer.class})
public class ItemServiceTest {
    @Autowired
    private final ItemServiceImpl itemService;

    @MockBean
    UserRepository userRepository;
    @MockBean
    private final ItemRepository itemRepository;
    @MockBean
    private final UserService userService;
    @MockBean
    private final BookingRepository bookingRepository;
    @MockBean
    private final CommentRepository commentRepository;

    @Test
    void createReturnsCreatedUser() {
        User owner = new User();
        owner.setId(1);
        owner.setName("name");
        owner.setEmail("email@mail.com");

        when(userService.get(1)).thenReturn(owner);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("name");
        itemDto.setDescription("description");
        itemDto.setAvailable(false);

        Item item = new Item();
        item.setId(1L);
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(owner);

        when(itemRepository.save(Mockito.any())).thenReturn(item);

        itemDto.setId(1L);
        Assertions.assertEquals(itemDto, itemService.create(itemDto, 1L));
    }

    @Test
    void getReturnsUserId1() {
        User owner = new User();
        owner.setId(1);
        owner.setName("name");
        owner.setEmail("email@mail.com");

        ItemDto itemDto = new ItemDto();
        itemDto.setName("name");
        itemDto.setDescription("description");
        itemDto.setAvailable(false);

        Item item = new Item();
        item.setId(1L);
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(owner);

        when(userService.get(1)).thenReturn(owner);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(commentRepository.findAllByItemId(1)).thenReturn(List.of());
        when(bookingRepository.findTop1BookingByItem_IdAndStartBeforeAndStatusOrderByEndDesc(
                anyLong(), any(LocalDateTime.class), any(BookingStatus.class))).thenReturn(Optional.empty());
        when(bookingRepository.findTop1BookingByItem_IdAndStartAfterAndStatusOrderByEndAsc(
                anyLong(), any(LocalDateTime.class), any(BookingStatus.class))).thenReturn(Optional.empty());

        itemDto.setComments(List.of());
        itemDto.setId(1L);
        Assertions.assertEquals(itemDto, itemService.get(1, 1));
    }

    @Test
    void getAllByUserIdReturnsListSize1() {
        User owner = new User();
        owner.setId(1);
        owner.setName("name");
        owner.setEmail("email@mail.com");

        ItemDto itemDto = new ItemDto();
        itemDto.setName("name");
        itemDto.setDescription("description");
        itemDto.setAvailable(false);

        Item item = new Item();
        item.setId(1L);
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(owner);
        when(itemRepository.findAllByOwnerId(1)).thenReturn(List.of(item));
        when(commentRepository.findAllByItemId(1)).thenReturn(List.of());

        Assertions.assertEquals(1, itemService.getAllByUserId(1).size());
    }

    @Test
    void updateReturnsUpdatedUserNewNameAndDescription() {
        User owner = new User();
        owner.setId(1);
        owner.setName("name");
        owner.setEmail("email@mail.com");

        ItemDto itemDto = new ItemDto();
        itemDto.setName("name");
        itemDto.setDescription("description");
        itemDto.setAvailable(false);

        Item item = new Item();
        item.setId(1L);
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(owner);

        Item newItem = new Item();
        newItem.setId(item.getId());
        newItem.setName("new_name");
        newItem.setDescription("new_description");
        newItem.setAvailable(true);
        item.setOwner(owner);
        ItemDto newItemDto = ItemMapper.toItemDto(newItem);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);

        Assertions.assertEquals(newItemDto, itemService.update(newItemDto, 1, 1));
    }

    @Test
    void addCommentReturnsCreatedComment() {
        User owner = new User();
        owner.setId(1);
        owner.setName("name");
        owner.setEmail("email@mail.com");

        ItemDto itemDto = new ItemDto();
        itemDto.setName("name");
        itemDto.setDescription("description");
        itemDto.setAvailable(false);

        Item item = new Item();
        item.setId(1L);
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(owner);

        Item newItem = new Item();
        newItem.setId(item.getId());
        newItem.setName("new_name");
        newItem.setDescription("new_description");
        newItem.setAvailable(true);
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        Comment commentToSave = new Comment();
        commentToSave.setItem(item);
        commentToSave.setCreated(LocalDateTime.now());
        commentToSave.setText("text");

        Comment saved = new Comment();
        saved.setItem(item);
        saved.setCreated(LocalDateTime.now());
        saved.setText("text");
        saved.setId(1);
        saved.setAuthor(owner);


        CommentDto toSave = new CommentDto();
        toSave.setText("text");

        ItemMapper.toItemDto(newItem);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(bookingRepository.findTop1BookingByItemIdAndBookerIdAndEndBeforeAndStatusOrderByEndDesc(
                anyLong(), anyLong(), any(LocalDateTime.class), any(BookingStatus.class))).thenReturn(Optional.of(booking));
        when(commentRepository.save(any())).thenReturn(saved);

        Assertions.assertEquals(CommentMapper.toCommentDto(saved), itemService.addComment(toSave,1, 1));
    }

    @Test
    void throwsWrongUserExceptionTest() {
        User owner = new User();
        owner.setId(1);
        owner.setName("name");
        owner.setEmail("email@mail.com");

        ItemDto itemDto = new ItemDto();
        itemDto.setName("name");
        itemDto.setDescription("description");
        itemDto.setAvailable(false);

        Item item = new Item();
        item.setId(1L);
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(owner);

        Item newItem = new Item();
        newItem.setId(item.getId());
        newItem.setName("new_name");
        newItem.setDescription("new_description");
        newItem.setAvailable(true);
        item.setOwner(owner);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        Assertions.assertThrows(WrongUserException.class, () -> itemService.update(itemDto, 1, 2));
    }

    @Test
    void searchTestBlankReturnsEmptyList() {
        User owner = new User();
        owner.setId(1);
        owner.setName("name");
        owner.setEmail("email@mail.com");

        ItemDto itemDto = new ItemDto();
        itemDto.setName("name");
        itemDto.setDescription("description");
        itemDto.setAvailable(false);

        Item item = new Item();
        item.setId(1L);
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(owner);
        when(itemRepository.findAllByAvailableTrueAndDescriptionContainingIgnoreCase(anyString())).thenReturn(List.of());

        Assertions.assertEquals(0, itemService.search("").size());
    }

    @Test
    void searchReturnsListSize1() {
        User owner = new User();
        owner.setId(1);
        owner.setName("name");
        owner.setEmail("email@mail.com");

        ItemDto itemDto = new ItemDto();
        itemDto.setName("name");
        itemDto.setDescription("description");
        itemDto.setAvailable(false);


        Item item = new Item();
        item.setId(1L);
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        User booker = new User();
        booker.setId(2L);
        booker.setName("booker");
        booker.setEmail("booker@email.com");

        booking.setBooker(booker);

        when(itemRepository.findAllByAvailableTrueAndDescriptionContainingIgnoreCase(anyString())).thenReturn(List.of(item));
        when(bookingRepository.findTop1BookingByItem_IdAndStartBeforeAndStatusOrderByEndDesc(anyLong(), any(LocalDateTime.class), any(BookingStatus.class))).thenReturn(Optional.of(booking));
        when(bookingRepository.findTop1BookingByItem_IdAndStartAfterAndStatusOrderByEndAsc(anyLong(), any(LocalDateTime.class), any(BookingStatus.class))).thenReturn(Optional.empty());
        Assertions.assertEquals(1, itemService.search("desc").size());
    }
}
