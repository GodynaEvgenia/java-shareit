package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    public ItemRequestDto create(
            @RequestHeader(name = "X-Sharer-User-Id") Long ownerId,
            @RequestBody ItemRequestDto requestDto) {
        log.info("server создание запроса, requesrDto = {}, ownerId={}", requestDto.toString(), ownerId);
        ItemRequestDto createdRequest = itemRequestService.create(requestDto, ownerId);
        log.info("запрос создан, createdRequest={}", createdRequest.toString());
        return createdRequest;
    }

    @GetMapping("")
    public List<ItemRequestDto> getAllByOwner(@RequestHeader(name = "X-Sharer-User-Id") Long ownerId) {
        return itemRequestService.getAllByOwner(ownerId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllByAnotherUsers(@RequestHeader(name = "X-Sharer-User-Id") Long ownerId) {
        return itemRequestService.getAllByAnotherUsers(ownerId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findById(@RequestHeader(name = "X-Sharer-User-Id") Long ownerId,
                                   @PathVariable Long requestId) {
        return itemRequestService.findbyId(requestId);
    }
}
