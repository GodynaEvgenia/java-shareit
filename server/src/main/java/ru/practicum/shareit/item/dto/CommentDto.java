package ru.practicum.shareit.item.dto;

import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
public class CommentDto {
    Long id;
    String text;
    String authorName;
    LocalDateTime created;
}
