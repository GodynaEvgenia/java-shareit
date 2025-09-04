package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemDtoResp;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class BookingDtoResponse {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemDtoResp item;
    private BookerDto booker;
    private String status;
}
