package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ItemRepository {
    private long curId = 0;
    private final Map<Long, Item> items = new HashMap<>();

    public Collection<Item> getAllByUserId(long userId) {
        return items.values().stream().filter(item -> item.getOwner().getId() == userId).toList();
    }

    public Item get(long id) {
        return items.get(id);
    }

    public Item create(ItemDto item, User owner) {
        curId++;
        Item itemToSave = Item.builder()
                .id(curId)
                .owner(owner)
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
        items.put(curId, itemToSave);
        return itemToSave;
    }

    public Item update(ItemDto updatedItem, long id) {
        Item existingItem = items.get(id);
        if (updatedItem.getName() != null) {
            existingItem.setName(updatedItem.getName());
        }
        if (updatedItem.getAvailable() != null) {
            existingItem.setAvailable(updatedItem.getAvailable());
        }
        if (updatedItem.getDescription() != null) {
            existingItem.setDescription(updatedItem.getDescription());
        }
        items.replace(id, existingItem);
        existingItem.setId(id);
        return existingItem;
    }

    public Collection<Item> search(String text) {
            Collection<Item> correctItems = items.values().stream()
                    .filter(item -> (item.getDescription().toLowerCase().contains(text.toLowerCase()) ||
                            item.getName().toLowerCase().contains(text.toLowerCase())))
                    .filter(Item::isAvailable)
                    .toList();
            return correctItems;
    }
}
