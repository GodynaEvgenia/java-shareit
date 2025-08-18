package ru.practicum.shareit.item.dto;

import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class CommentDto {
    Long id ;
    String text;
    String authorName;
    LocalDateTime created;
}
