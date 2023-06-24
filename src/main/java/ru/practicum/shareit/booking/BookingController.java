package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.exceptions.UnknownStateException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingOutDto saveBooking(@Valid @RequestBody BookingInDto bookingDto,
                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        bookingDto.setBookerId(userId);
        return bookingService.saveBooking(bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingOutDto setStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                   @PathVariable(name = "bookingId") Long bookingId,
                                   @RequestParam(name = "approved") Boolean isApproved) {
        return bookingService.setStatus(bookingId, userId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public BookingOutDto getBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @PathVariable(name = "bookingId") Long bookingId) {
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingOutDto> findAllBookingsByState(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @RequestParam(name = "state", defaultValue = "ALL") String strState) {
        State state;
        try {
            state = State.valueOf(strState);
        } catch (IllegalArgumentException e) {
            throw new UnknownStateException(strState);
        }
        return bookingService.findAllBookingsByState(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingOutDto> findAllOwnerBookingsByState(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                           @RequestParam(name = "state", defaultValue = "ALL") String strState) {
        State state;
        try {
            state = State.valueOf(strState);
        } catch (IllegalArgumentException e) {
            throw new UnknownStateException(strState);
        }
        return bookingService.findAllOwnerBookingsByState(userId, state);
    }


    @GetMapping("/item/{itemId}")
    public List<BookingOutDto> findAllBookingsOfItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @PathVariable(name = "itemId") Long itemId) {
        return bookingService.findAllBookingsOfItem(itemId);
    }
}
