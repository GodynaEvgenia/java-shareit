package ru.practicum.shareit.item.dto;

import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}
