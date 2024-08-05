package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NoSuchDataException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequest create(ItemRequest request, long userId) {
        User requestor = userRepository.findById(userId).orElseThrow(NoSuchDataException::new);
        request.setRequestor(requestor);
        request.setCreated(LocalDateTime.now());
        return itemRequestRepository.save(request);
    }

    @Override
    public ItemRequestDto find(long requestId) {
        ItemRequest request = itemRequestRepository.findById(requestId).orElseThrow(NoSuchDataException::new);
        List<Item> items = itemRepository.findAllByRequestId(requestId);
        ItemRequestDto dto = ItemRequestMapper.toItemRequestDto(request);
        dto.setItems(items);
        return dto;
    }

    @Override
    public Collection<ItemRequestDto> findAllByUserId(long userId) {
        userRepository.findById(userId).orElseThrow(NoSuchDataException::new);
        List<ItemRequest> requests = itemRequestRepository.getAllByRequestorIdOrderByCreatedDesc(userId);
        List<ItemRequestDto> dtos = new ArrayList<>();
        for (ItemRequest request : requests) {
            List<Item> items = itemRepository.findAllByRequestId(request.getId());
            ItemRequestDto dto = ItemRequestMapper.toItemRequestDto(request);
            dto.setItems(items);
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public Collection<ItemRequest> findAllOther(long userId, long from, int size) {
        Page<ItemRequest> page = itemRequestRepository.getAllByRequestorIdNotOrderByCreatedDesc(userId, PageRequest.ofSize(size));
        List<ItemRequest> requests = page.getContent();
        List<ItemRequest> requestsToReturn = new ArrayList<>();
        for (int i = 0; i < requests.size() - 1; i++) {
            if (i > from) {
                requestsToReturn.add(requests.get(i));
            }
        }
        return requestsToReturn;
    }
}
