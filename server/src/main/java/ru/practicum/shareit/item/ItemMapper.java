package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResp;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class ItemMapper {
    public Item toModel(ItemDto dto, Long itemId, User user, Request request) {
        log.info("item mapper, dto={}, itemId={}, user={}", dto, itemId, user);
        Item item = new Item(itemId, dto.getName(), dto.getDescription(), dto.getAvailable(), user, request);
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

    public ItemDto toDto(Item item, Long ownerId, List<Comment> comments, Long requestId) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), requestId);
    }

    public ItemDtoResp toDtoResp(Item item, Long ownerId, List<CommentDto> comments) {
        ItemDtoResp itemDtoResp = new ItemDtoResp();
        itemDtoResp.setId(item.getId());
        itemDtoResp.setName(item.getName());
        itemDtoResp.setDescription(item.getDescription());
        itemDtoResp.setAvailable(item.getAvailable());
        itemDtoResp.setComments(comments);
        if (item.getOwner().getId().equals(ownerId)) {
            itemDtoResp.setNextBooking(LocalDateTime.now());
            itemDtoResp.setLastBooking(LocalDateTime.now());
        } else {
            itemDtoResp.setNextBooking(null);
            itemDtoResp.setLastBooking(null);
        }
        return itemDtoResp;
    }
}
