package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto saveBooking(@Valid @RequestBody BookingDto bookingDto,
                                  @RequestHeader("X-Sharer-User-Id") Long userId) {
        bookingDto.setBookerId(userId);
        bookingDto.setStatus(Status.WAITING);
        return bookingService.saveBooking(bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto setStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                @PathVariable(name = "bookingId") Long bookingId,
                                @RequestParam(name = "approved") Boolean isApproved) {
        return bookingService.setStatus(bookingId, userId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable(name = "bookingId") Long bookingId) {
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> findAllBookingsByState(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(name = "state", defaultValue = "ALL") State state) {
        return bookingService.findAllBookingsByState(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> findAllOwnerBookingsByState(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @RequestParam(name = "state", defaultValue = "ALL") State state) {
        return bookingService.findAllOwnerBookingsByState(userId, state);
    }
}
