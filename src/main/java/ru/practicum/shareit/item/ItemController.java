package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/items")
public class ItemController {
    private ItemServiceImpl itemService;

    @Autowired
    public ItemController(ItemServiceImpl itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto create(
            @RequestHeader(name = "X-Sharer-User-Id") Long ownerId,
            @RequestBody @Valid ItemDto item) {
        log.info("создание вещи, item = {}", item.toString());
        ItemDto createdItem = itemService.createItem(item, ownerId);
        log.info("вещь создана, item={}", createdItem.toString());
        return createdItem;
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(name = "X-Sharer-User-Id") Long ownerId,
                          @PathVariable Long itemId,
                          @RequestBody ItemDto item) {
        log.info("обновление вещи, item = {}", item.toString());
        ItemDto updatedItem = itemService.updateItem(item, itemId, ownerId);
        log.info("вещь обновлена, item = {}", item.toString());
        return updatedItem;
    }

    @GetMapping()
    public List<ItemDto> getAll(@RequestHeader(name = "X-Sharer-User-Id") Long ownerId) {
        return itemService.getAll(ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto get(@PathVariable Long itemId) {
        return itemService.getItem(itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return itemService.search(text);
    }
}
