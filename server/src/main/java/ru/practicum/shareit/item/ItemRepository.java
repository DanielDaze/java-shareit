package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerId(long userId);

    List<Item> findAllByAvailableTrueAndDescriptionContainingIgnoreCase(String text);

    List<Item> findAllByRequestId(long requestId);
}
