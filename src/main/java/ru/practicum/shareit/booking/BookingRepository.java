package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBooker_idOrderByStartDate(Long bookerId);

    List<Booking> findAllByBooker_idAndStatusOrderByStartDate(Long bookerId, String status);

    List<Booking> findAllByBooker_idAndStartDateBeforeOrderByStartDate(Long bookerId, LocalDateTime date);

    List<Booking> findAllByBooker_idAndStartDateAfterOrderByStartDate(Long bookerId, LocalDateTime date);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.owner as o " +
            "where o.id = ?1 ")
    List<Booking> getByOwner(Long ownerId);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.owner as o " +
            "where o.id = ?1 " +
            "and b.startDate < ?2")
    List<Booking> getByOwnerAndStartDateBefore(Long ownerId, LocalDateTime date);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.owner as o " +
            "where o.id = ?1 " +
            "and b.startDate > ?2")
    List<Booking> getByOwnerAndStartDateAfter(Long ownerId, LocalDateTime date);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.owner as o " +
            "where o.id = ?1 " +
            "and b.status = ?2")
    List<Booking> getByOwner_Status(Long ownerId, BookingState status);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join b.booker as bk " +
            "where i.id = ?2 " +
            "and bk.id = ?1")
    List<Booking> getByItemAndBooker(Long bookerId, Long itemId);
}
