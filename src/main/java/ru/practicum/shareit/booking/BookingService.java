package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto saveBooking(BookingDto bookingDto);

    BookingDto setStatus(Long bookingId, Long userId, Boolean isApproved);

    public BookingDto getBooking(Long userId, Long bookingId);

    List<BookingDto> findAllBookingsByState(Long userId, State state);

    List<BookingDto> findAllOwnerBookingsByState(Long ownerId, State state);
}
