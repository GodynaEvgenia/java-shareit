package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.item.CommentDto;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class ItemDtoResp {
    Long id;
    String name;
    String description;
    Boolean available;
    LocalDateTime nextBooking;
    LocalDateTime lastBooking;
    List<CommentDto> comments;
}
