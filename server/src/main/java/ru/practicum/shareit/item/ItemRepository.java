package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(Long ownerId);

    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.available = true and (LOWER(i.name) LIKE LOWER(:text) OR LOWER(i.description) LIKE LOWER(:text))")
    List<Item> search(String text);

    Optional<Item> findByIdAndAvailableTrue(Long itemId);

    List<Item> findByRequestId(Long requestorId);
}
