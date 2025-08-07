package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Slf4j
@Component
public class ItemMapper {
    public Item toModel(ItemDto dto, Long itemId, User user) {
        log.info("item mapper, dto={}, itemId={}, user={}", dto, itemId, user);
        Item item = new Item(itemId, dto.getName(), dto.getDescription(), dto.getAvailable(), user);
        log.info("item mapper, item = {}", item);
        return item;
    }

    public Item toModelForUpdate(ItemDto dto, Item item) {
        if (dto.getName() != null) {
            item.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            item.setDescription(dto.getDescription());
        }
        if (dto.getAvailable() != null) {
            item.setAvailable(dto.getAvailable());
        }
        return item;
    }

    public ItemDto toDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }
}
