package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    @Test
    public void createBooking_Success() {
        Long userId = 1L;
        Long itemId = 10L;

        BookingDtoRequest bookingDto = new BookingDtoRequest();
        bookingDto.setItemId(itemId);

        User user = new User();
        user.setId(userId);

        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);

        Booking bookingModel = new Booking();
        Booking savedBooking = new Booking();
        savedBooking.setId(100L);

        BookingDtoResponse expectedResponse = new BookingDtoResponse();
        expectedResponse.setId(100L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemRepository.findByIdAndAvailableTrue(itemId)).thenReturn(Optional.of(item));
        when(bookingMapper.toModel(bookingDto, null, item, user)).thenReturn(bookingModel);
        when(bookingRepository.save(bookingModel)).thenReturn(savedBooking);
        when(bookingMapper.toDto(savedBooking, userId)).thenReturn(expectedResponse);

        BookingDtoResponse actualResponse = bookingService.createBooking(bookingDto, userId);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getId(), actualResponse.getId());

        verify(userRepository).findById(userId);
        verify(itemRepository).findById(itemId);
        verify(itemRepository).findByIdAndAvailableTrue(itemId);
        verify(bookingMapper).toModel(bookingDto, null, item, user);
        verify(bookingRepository).save(bookingModel);
        verify(bookingMapper).toDto(savedBooking, userId);
    }

    @Test
    public void createBooking_UserNotFound_ThrowsNotFoundException() {
        Long userId = 1L;
        BookingDtoRequest bookingDto = new BookingDtoRequest();
        bookingDto.setItemId(10L);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> bookingService.createBooking(bookingDto, userId));
        assertEquals("пользователь не найден", ex.getMessage());

        verify(userRepository).findById(userId);
        verifyNoMoreInteractions(itemRepository, bookingMapper, bookingRepository);
    }

    @Test
    public void createBooking_ItemNotFound_ThrowsNotFoundException() {
        Long userId = 1L;
        Long itemId = 10L;
        BookingDtoRequest bookingDto = new BookingDtoRequest();
        bookingDto.setItemId(itemId);

        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> bookingService.createBooking(bookingDto, userId));
        assertEquals("вещь не найдена", ex.getMessage());

        verify(userRepository).findById(userId);
        verify(itemRepository).findById(itemId);
        verifyNoMoreInteractions(bookingMapper, bookingRepository);
    }

    @Test
    public void createBooking_ItemNotAvailable_ThrowsNotAvailable() {
        Long userId = 1L;
        Long itemId = 10L;
        BookingDtoRequest bookingDto = new BookingDtoRequest();
        bookingDto.setItemId(itemId);

        User user = new User();
        user.setId(userId);

        Item item = new Item();
        item.setId(itemId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemRepository.findByIdAndAvailableTrue(itemId)).thenReturn(Optional.empty());

        NotAvailable ex = assertThrows(NotAvailable.class,
                () -> bookingService.createBooking(bookingDto, userId));
        assertEquals("вещь недоступна", ex.getMessage());

        verify(userRepository).findById(userId);
        verify(itemRepository).findById(itemId);
        verify(itemRepository).findByIdAndAvailableTrue(itemId);
        verifyNoMoreInteractions(bookingMapper, bookingRepository);
    }

    @Test
    public void approveRequest_Approved_Success() {
        Long userId = 1L;
        Long bookingId = 10L;

        User owner = new User();
        owner.setId(userId);

        Item item = new Item();
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setItem(item);
        booking.setStatus(BookingState.WAITING);

        BookingDtoResponse expectedDto = new BookingDtoResponse();
        expectedDto.setId(bookingId);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingMapper.toDto(booking, userId)).thenReturn(expectedDto);

        BookingDtoResponse result = bookingService.approveRequest(userId, bookingId, true);

        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
        assertEquals(BookingState.APPROVED, booking.getStatus());

        verify(bookingRepository).findById(bookingId);
        verify(bookingRepository).save(booking);
        verify(bookingMapper).toDto(booking, userId);
    }

    @Test
    public void approveRequest_Rejected_Success() {
        Long userId = 1L;
        Long bookingId = 10L;

        User owner = new User();
        owner.setId(userId);

        Item item = new Item();
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setItem(item);
        booking.setStatus(BookingState.WAITING);

        BookingDtoResponse expectedDto = new BookingDtoResponse();
        expectedDto.setId(bookingId);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingMapper.toDto(booking, userId)).thenReturn(expectedDto);

        BookingDtoResponse result = bookingService.approveRequest(userId, bookingId, false);

        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
        assertEquals(BookingState.REJECTED, booking.getStatus());

        verify(bookingRepository).findById(bookingId);
        verify(bookingRepository).save(booking);
        verify(bookingMapper).toDto(booking, userId);
    }

    @Test
    public void approveRequest_UserNotOwner_ThrowsNotAvailable() {
        Long userId = 1L;
        Long bookingId = 10L;

        User owner = new User();
        owner.setId(2L);

        Item item = new Item();
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setItem(item);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        NotAvailable exception = assertThrows(NotAvailable.class,
                () -> bookingService.approveRequest(userId, bookingId, true));

        assertEquals("", exception.getMessage());

        verify(bookingRepository).findById(bookingId);
        verifyNoMoreInteractions(bookingRepository, bookingMapper);
    }

    @Test
    public void getBooking_Success() {
        Long userId = 1L;
        Long bookingId = 10L;

        Booking booking = new Booking();
        booking.setId(bookingId);

        BookingDtoResponse expectedDto = new BookingDtoResponse();
        expectedDto.setId(bookingId);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingMapper.toDto(booking, userId)).thenReturn(expectedDto);

        BookingDtoResponse result = bookingService.get(userId, bookingId);

        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());

        verify(bookingRepository).findById(bookingId);
        verify(bookingMapper).toDto(booking, userId);
    }

    @Test
    public void getBooking_NotFound_ThrowsException() {
        Long userId = 1L;
        Long bookingId = 10L;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> bookingService.get(userId, bookingId));

        verify(bookingRepository).findById(bookingId);
        verifyNoInteractions(bookingMapper);
    }

    private final long userId = 1L;

    private Booking createBooking(long id) {
        Booking booking = new Booking();
        booking.setId(id);
        return booking;
    }

    private BookingDtoResponse createDto(long id) {
        BookingDtoResponse dto = new BookingDtoResponse();
        dto.setId(id);
        return dto;
    }

    @Test
    public void getAll_PastState_CallsCorrectRepositoryMethod() {
        List<Booking> bookings = List.of(createBooking(1L), createBooking(2L));
        when(bookingRepository.findAllByBooker_idAndStartDateBeforeOrderByStartDate(eq(userId), any(LocalDateTime.class)))
                .thenReturn(bookings);
        when(bookingMapper.toDto(any(), eq(userId))).thenAnswer(invocation -> {
            Booking b = invocation.getArgument(0);
            return createDto(b.getId());
        });

        List<BookingDtoResponse> result = bookingService.getAll(userId, "PAST");

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        verify(bookingRepository).findAllByBooker_idAndStartDateBeforeOrderByStartDate(eq(userId), any(LocalDateTime.class));
        verify(bookingMapper, times(2)).toDto(any(), eq(userId));
    }

    @Test
    public void getAll_FutureState_CallsCorrectRepositoryMethod() {
        List<Booking> bookings = List.of(createBooking(3L));
        when(bookingRepository.findAllByBooker_idAndStartDateAfterOrderByStartDate(eq(userId), any(LocalDateTime.class)))
                .thenReturn(bookings);
        when(bookingMapper.toDto(any(), eq(userId))).thenAnswer(invocation -> {
            Booking b = invocation.getArgument(0);
            return createDto(b.getId());
        });

        List<BookingDtoResponse> result = bookingService.getAll(userId, "FUTURE");

        assertEquals(1, result.size());
        assertEquals(3L, result.get(0).getId());

        verify(bookingRepository).findAllByBooker_idAndStartDateAfterOrderByStartDate(eq(userId), any(LocalDateTime.class));
        verify(bookingMapper).toDto(any(), eq(userId));
    }

    @Test
    public void getAll_WaitingState_CallsCorrectRepositoryMethod() {
        List<Booking> bookings = List.of(createBooking(4L));
        when(bookingRepository.findAllByBooker_idAndStatusOrderByStartDate(userId, "WAITING"))
                .thenReturn(bookings);
        when(bookingMapper.toDto(any(), eq(userId))).thenAnswer(invocation -> {
            Booking b = invocation.getArgument(0);
            return createDto(b.getId());
        });

        List<BookingDtoResponse> result = bookingService.getAll(userId, "WAITING");

        assertEquals(1, result.size());
        assertEquals(4L, result.get(0).getId());

        verify(bookingRepository).findAllByBooker_idAndStatusOrderByStartDate(userId, "WAITING");
        verify(bookingMapper).toDto(any(), eq(userId));
    }

    @Test
    public void getAll_RejectedState_CallsCorrectRepositoryMethod() {
        List<Booking> bookings = List.of(createBooking(5L));
        when(bookingRepository.findAllByBooker_idAndStatusOrderByStartDate(userId, "REJECTED"))
                .thenReturn(bookings);
        when(bookingMapper.toDto(any(), eq(userId))).thenAnswer(invocation -> {
            Booking b = invocation.getArgument(0);
            return createDto(b.getId());
        });

        List<BookingDtoResponse> result = bookingService.getAll(userId, "REJECTED");

        assertEquals(1, result.size());
        assertEquals(5L, result.get(0).getId());

        verify(bookingRepository).findAllByBooker_idAndStatusOrderByStartDate(userId, "REJECTED");
        verify(bookingMapper).toDto(any(), eq(userId));
    }

    @Test
    public void getAll_AllState_CallsCorrectRepositoryMethod() {
        List<Booking> bookings = List.of(createBooking(6L), createBooking(7L));
        when(bookingRepository.findAllByBooker_idOrderByStartDate(userId))
                .thenReturn(bookings);
        when(bookingMapper.toDto(any(), eq(userId))).thenAnswer(invocation -> {
            Booking b = invocation.getArgument(0);
            return createDto(b.getId());
        });

        List<BookingDtoResponse> result = bookingService.getAll(userId, "ALL");

        assertEquals(2, result.size());
        assertEquals(6L, result.get(0).getId());
        assertEquals(7L, result.get(1).getId());

        verify(bookingRepository).findAllByBooker_idOrderByStartDate(userId);
        verify(bookingMapper, times(2)).toDto(any(), eq(userId));
    }

    private final Long ownerId = 1L;

    @Test
    public void getByOwner_UserNotFound_ThrowsNotFoundException() {
        when(userRepository.findById(ownerId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> bookingService.getByOwner(ownerId, "ALL"));

        assertEquals("пользователь не найден", ex.getMessage());
        verify(userRepository).findById(ownerId);
        verifyNoMoreInteractions(bookingRepository, bookingMapper);
    }

    @Test
    public void getByOwner_PastState_CallsCorrectRepositoryMethod() {
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(new User()));
        List<Booking> bookings = List.of(createBooking(1L), createBooking(2L));
        when(bookingRepository.getByOwnerAndStartDateBefore(eq(ownerId), any(LocalDateTime.class)))
                .thenReturn(bookings);
        when(bookingMapper.toDto(any(), eq(ownerId))).thenAnswer(invocation -> {
            Booking b = invocation.getArgument(0);
            return createDto(b.getId());
        });

        List<BookingDtoResponse> result = bookingService.getByOwner(ownerId, "PAST");

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        verify(userRepository).findById(ownerId);
        verify(bookingRepository).getByOwnerAndStartDateBefore(eq(ownerId), any(LocalDateTime.class));
        verify(bookingMapper, times(2)).toDto(any(), eq(ownerId));
    }

    @Test
    public void getByOwner_FutureState_CallsCorrectRepositoryMethod() {
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(new User()));
        List<Booking> bookings = List.of(createBooking(3L));
        when(bookingRepository.getByOwnerAndStartDateAfter(eq(ownerId), any(LocalDateTime.class)))
                .thenReturn(bookings);
        when(bookingMapper.toDto(any(), eq(ownerId))).thenAnswer(invocation -> {
            Booking b = invocation.getArgument(0);
            return createDto(b.getId());
        });

        List<BookingDtoResponse> result = bookingService.getByOwner(ownerId, "FUTURE");

        assertEquals(1, result.size());
        assertEquals(3L, result.get(0).getId());

        verify(userRepository).findById(ownerId);
        verify(bookingRepository).getByOwnerAndStartDateAfter(eq(ownerId), any(LocalDateTime.class));
        verify(bookingMapper).toDto(any(), eq(ownerId));
    }

    @Test
    public void getByOwner_WaitingState_CallsCorrectRepositoryMethod() {
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(new User()));
        List<Booking> bookings = List.of(createBooking(4L));
        when(bookingRepository.getByOwner_Status(ownerId, BookingState.WAITING))
                .thenReturn(bookings);
        when(bookingMapper.toDto(any(), eq(ownerId))).thenAnswer(invocation -> {
            Booking b = invocation.getArgument(0);
            return createDto(b.getId());
        });

        List<BookingDtoResponse> result = bookingService.getByOwner(ownerId, "WAITING");

        assertEquals(1, result.size());
        assertEquals(4L, result.get(0).getId());

        verify(userRepository).findById(ownerId);
        verify(bookingRepository).getByOwner_Status(ownerId, BookingState.WAITING);
        verify(bookingMapper).toDto(any(), eq(ownerId));
    }

    @Test
    public void getByOwner_RejectedState_CallsCorrectRepositoryMethod() {
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(new User()));
        List<Booking> bookings = List.of(createBooking(5L));
        when(bookingRepository.getByOwner_Status(ownerId, BookingState.REJECTED))
                .thenReturn(bookings);
        when(bookingMapper.toDto(any(), eq(ownerId))).thenAnswer(invocation -> {
            Booking b = invocation.getArgument(0);
            return createDto(b.getId());
        });

        List<BookingDtoResponse> result = bookingService.getByOwner(ownerId, "REJECTED");

        assertEquals(1, result.size());
        assertEquals(5L, result.get(0).getId());

        verify(userRepository).findById(ownerId);
        verify(bookingRepository).getByOwner_Status(ownerId, BookingState.REJECTED);
        verify(bookingMapper).toDto(any(), eq(ownerId));
    }

    @Test
    public void getByOwner_AllState_CallsCorrectRepositoryMethod() {
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(new User()));
        List<Booking> bookings = List.of(createBooking(6L), createBooking(7L));
        when(bookingRepository.getByOwner(ownerId))
                .thenReturn(bookings);
        when(bookingMapper.toDto(any(), eq(ownerId))).thenAnswer(invocation -> {
            Booking b = invocation.getArgument(0);
            return createDto(b.getId());
        });

        List<BookingDtoResponse> result = bookingService.getByOwner(ownerId, "ALL");

        assertEquals(2, result.size());
        assertEquals(6L, result.get(0).getId());
        assertEquals(7L, result.get(1).getId());

        verify(userRepository).findById(ownerId);
        verify(bookingRepository).getByOwner(ownerId);
        verify(bookingMapper, times(2)).toDto(any(), eq(ownerId));
    }


}