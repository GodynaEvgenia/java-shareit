package ru.practicum.shareit.booking;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.NotAvailable;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {
    final BookingMapper bookingMapper;
    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository repository,
                          UserRepository userRepository,
                          ItemRepository itemRepository,
                          BookingMapper bookingMapper) {
        this.bookingRepository = repository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.bookingMapper = bookingMapper;
    }
    @Transactional
    public BookingDtoResponse createBooking(BookingDtoRequest bookingDto, Long userId) {
        Optional<User> booker = userRepository.findById(userId);
        if (!booker.isPresent()) {
            throw new NotFoundException("пользователь не найден");
        }
        Optional<Item> itemExists = itemRepository.findById(bookingDto.getItemId());
        if (!itemExists.isPresent()) {
            throw new NotFoundException("вещь не найдена");
        }
        Optional<Item> item = itemRepository.findByIdAndAvailableTrue(bookingDto.getItemId());
        if (!item.isPresent()) {
            throw new NotAvailable("вещь недоступна");
        }

        ru.practicum.shareit.booking.model.Booking booking = bookingMapper.toModel(bookingDto, null,
                item.get(), booker.get());
        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toDto(savedBooking, userId);
    }
    @Transactional
    public BookingDtoResponse approveRequest(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).get();
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotAvailable("");
        }
        if (approved) {
            booking.setStatus(BookingState.APPROVED);
        } else {
            booking.setStatus(BookingState.REJECTED);
        }
        bookingRepository.save(booking);

        return bookingMapper.toDto(booking, userId);
    }

    public BookingDtoResponse get(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).get();
        BookingDtoResponse response = bookingMapper.toDto(booking, userId);
        return response;
    }

    public List<BookingDtoResponse> getAll(long userId, String state) {
        List<Booking> bookings;
        switch (BookingRequestState.valueOf(state)) {
            case PAST:
                bookings = bookingRepository.findAllByBooker_idAndStartDateBeforeOrderByStartDate(userId,
                        LocalDateTime.now());
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBooker_idAndStartDateAfterOrderByStartDate(userId,
                        LocalDateTime.now());
                break;
            case WAITING:
                bookings = bookingRepository.findAllByBooker_idAndStatusOrderByStartDate(userId, state.toString());
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBooker_idAndStatusOrderByStartDate(userId, state.toString());
                break;
            case ALL:
                bookings = bookingRepository.findAllByBooker_idOrderByStartDate(userId);
                break;
            default:
                bookings = bookingRepository.findAllByBooker_idOrderByStartDate(userId);
                break;
        }
        return bookings.stream().map(booking -> bookingMapper.toDto(booking, userId)).toList();
    }

    public List<BookingDtoResponse> getByOwner(Long ownerId, String state) {
        Optional<User> owner = userRepository.findById(ownerId);
        if (!owner.isPresent()) {
            throw new NotFoundException("пользователь не найден");
        }
        List<Booking> bookings;
        switch (BookingRequestState.valueOf(state)) {
            case PAST:
                bookings = bookingRepository.getByOwnerAndStartDateBefore(ownerId, LocalDateTime.now());
                break;
            case FUTURE:
                bookings = bookingRepository.getByOwnerAndStartDateAfter(ownerId, LocalDateTime.now());
                break;
            case WAITING:
                bookings = bookingRepository.getByOwner_Status(ownerId, BookingState.valueOf(state));
                break;
            case REJECTED:
                bookings = bookingRepository.getByOwner_Status(ownerId, BookingState.valueOf(state));
                break;
            case ALL:
                bookings = bookingRepository.getByOwner(ownerId);
                break;
            default:
                bookings = bookingRepository.getByOwner(ownerId);
                break;
        }
        return bookings.stream().map(booking -> bookingMapper.toDto(booking, ownerId)).toList();
    }
}
