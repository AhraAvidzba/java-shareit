package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    //Методы для получения бронирований сделанных переданным пользователем
    List<Booking> findByBookerId(Long bookerId, Sort sort);

    @Query("select b " +
            "from Booking as b " +
            "where b.booker.id = ?1 " +
            "and b.start < ?2 " +
            "and b.end > ?2 ")
    List<Booking> findByCurrentBooker(Long bookerId, LocalDateTime start, Sort sort);

    List<Booking> findByBookerIdAndEndIsBefore(Long bookerId, LocalDateTime end, Sort sort);

    List<Booking> findByBookerIdAndStartIsAfter(Long bookerId, LocalDateTime end, Sort sort);

    List<Booking> findByBookerIdAndStatus(Long bookerId, Status status, Sort sort);


    //Методы для получения бронирований вещей владельцем которых являетя переданный пользователь
    List<Booking> findByItemOwnerId(Long ownerId, Sort sort);

    @Query("select b " +
            "from Booking as b " +
            "where b.item.owner.id = ?1 " +
            "and b.start < ?2 " +
            "and b.end > ?2 ")
    List<Booking> findByOwnerCurrentBooker(Long ownerId, LocalDateTime start, Sort sort);

    List<Booking> findByItemOwnerIdAndEndIsBefore(Long ownerId, LocalDateTime end, Sort sort);

    List<Booking> findByItemOwnerIdAndStartIsAfter(Long ownerId, LocalDateTime end, Sort sort);

    List<Booking> findByItemOwnerIdAndStatus(Long bookerId, Status status, Sort sort);


    List<Booking> findByItemId(Long itemId, Sort sort);

    List<Booking> findByItemIdIn(List<Long> itemId, Sort sort);

    @Query("select b " +
            "from Booking as b " +
            "where b.item.id = ?1 " +
            "and (b.start between ?2 and ?3 " +
            "or b.end between ?2 and ?3) ")
    List<Booking> findTimeCrossingBookings(Long itemId, LocalDateTime start, LocalDateTime end);

}
