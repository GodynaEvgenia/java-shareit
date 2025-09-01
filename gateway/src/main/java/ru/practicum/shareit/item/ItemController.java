package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping(path = "/items")
public class ItemController {
    private ItemClient itemClient;

    @Autowired
    public ItemController(ItemClient itemClient) {
        this.itemClient = itemClient;
    }

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestHeader(name = "X-Sharer-User-Id") Long ownerId,
            @RequestBody @Valid ItemDto item) {
        log.info("создание вещи, item = {}, ownerId={}", item.toString(), ownerId);
        return itemClient.create(item, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader(name = "X-Sharer-User-Id") Long ownerId,
                                         @PathVariable Long itemId,
                                         @RequestBody ItemDto item) {
        log.info("обновление вещи, item = {}", item.toString());
        return itemClient.update(itemId, item);
    }

    @GetMapping()
    public ResponseEntity<Object> getAll(@RequestHeader(name = "X-Sharer-User-Id") Long ownerId) {
        return itemClient.getAll(ownerId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> get(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                      @PathVariable Long itemId) {
        return itemClient.get(itemId, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                         @RequestParam String text) {
        return itemClient.search(text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                             @PathVariable Long itemId,
                                             @RequestBody CommentSaveDto comment) {
        return itemClient.addComment(userId, itemId, comment.getText(), LocalDateTime.now());
        //return com;
    }
}
