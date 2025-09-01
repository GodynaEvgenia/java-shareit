package ru.practicum.shareit.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import ru.practicum.shareit.item.dto.ItemDtoResp;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class ItemRequestDto {
    private Long id;
    @NotBlank
    @NotEmpty
    private String description;
    private LocalDateTime created;
    private List<ItemDtoResp> items;
}