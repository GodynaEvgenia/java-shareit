package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class BookingDtoRequest {
    private Long id;
    private LocalDateTime start;

    private LocalDateTime end;
    private Long itemId;
    private Long bookerId;
    private String status;
}
