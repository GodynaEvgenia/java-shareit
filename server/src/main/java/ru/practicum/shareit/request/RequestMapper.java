package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Slf4j
@Component
public class RequestMapper {
    public Request toModel(ItemRequestDto dto, Long requestId, User user) {
        log.info("request mapper, dto={}, requestId={}, user={}", dto, requestId, user);
        Request request = new Request(requestId, dto.getDescription(), user);
        log.info("request mapper, request = {}", request);
        return request;
    }

    public ItemRequestDto toDto(Request request, Long requestorId, List<ItemDto> items) {
        return new ItemRequestDto(request.getId(), request.getDescription(), null, items);
    }

}
