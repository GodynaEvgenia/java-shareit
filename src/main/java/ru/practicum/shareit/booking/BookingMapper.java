package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookerDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDtoResp;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Slf4j
@Component
public class BookingMapper {
    public Booking toModel(BookingDtoRequest dto, Long bookingId, Item item, User user) {
        log.info("booking mapper, dto={}, bookingId={}, booking={}, user={}", dto, bookingId, user);
        Booking booking = new Booking(bookingId, dto.getStart(), dto.getEnd(), item, user, BookingState.WAITING);
        log.info("booking mapper, item = {}", item);
        return booking;
    }

    public BookingDtoResponse toDto(Booking booking, Long ownerId) {
        ItemDtoResp itemDtoResp = new ItemDtoResp();
        itemDtoResp.setId(booking.getItem().getId());
        itemDtoResp.setName(booking.getItem().getName());
        if (booking.getItem().getOwner().getId().equals(ownerId)) {
            itemDtoResp.setNextBooking(LocalDateTime.now());
            itemDtoResp.setLastBooking(LocalDateTime.now());
        }
        return new BookingDtoResponse(booking.getId(), booking.getStartDate(), booking.getEndDate(),
                itemDtoResp, new BookerDto(booking.getBooker().getId()), booking.getStatus().name());
    }
}
