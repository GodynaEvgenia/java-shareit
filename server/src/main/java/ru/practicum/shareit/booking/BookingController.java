package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDtoResponse create(
            @RequestHeader(name = "X-Sharer-User-Id") Long userId,
            @RequestBody BookingDtoRequest booking) {
        log.info("создание запроса на бронирование, booking = {}, user={}", booking.toString(), userId);
        BookingDtoResponse createdBooking = bookingService.createBooking(booking, userId);
        log.info("запрос создан, item={}", createdBooking.toString());
        return createdBooking;
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoResponse approveRequest(
            @RequestHeader(name = "X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId,
            @RequestParam Boolean approved) {
        log.info("запрос на подтверждение бронирования, userId={}, bookingId={}, approved={}",
                userId, bookingId, approved);
        BookingDtoResponse response = bookingService.approveRequest(userId, bookingId, approved);
        return response;
    }

    @GetMapping("/{bookingId}")
    public BookingDtoResponse get(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                  @PathVariable Long bookingId) {
        log.info("запрос данных о бронировании, userId={}, bookingId={}", userId, bookingId);
        BookingDtoResponse response = bookingService.get(userId, bookingId);
        return response;
    }

    @GetMapping("/owner")
    public List<BookingDtoResponse> getByOwner(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                               @RequestParam(value = "state", defaultValue = "ALL") String state) {
        log.info("получение списка вещей владельца, userId={}, state={}", userId, state);
        List<BookingDtoResponse> response = bookingService.getByOwner(userId, state);
        return response;
    }

    @GetMapping()
    public List<BookingDtoResponse> getAll(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                           @RequestParam(value = "state", defaultValue = "ALL") String state) {
        log.info("Получение списка бронирований пользователя, userId={}, state={}", userId, state);
        List<BookingDtoResponse> response = bookingService.getAll(userId, state);
        return response;
    }

}
