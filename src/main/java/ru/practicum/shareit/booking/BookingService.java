package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;

import java.util.List;

public interface BookingService {
    BookingOutDto saveBooking(BookingInDto bookingDto);

    BookingOutDto setStatus(Long bookingId, Long userId, Boolean isApproved);

    public BookingOutDto getBooking(Long userId, Long bookingId);

    List<BookingOutDto> findAllBookingsByState(Long userId, State state);

    List<BookingOutDto> findAllOwnerBookingsByState(Long ownerId, State state);

    List<BookingOutDto> findAllBookingsOfItem(Long itemId);
}
