package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.exeptions.UnknownStateException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> saveBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestBody @Valid BookingInDto bookingInDto) {
        log.info("Creating booking {}, userId={}", bookingInDto, userId);
        return bookingClient.saveBooking(bookingInDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> setStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @PathVariable(name = "bookingId") Long bookingId,
                                            @RequestParam(name = "approved") Boolean isApproved) {
        log.info("Updating booking {}, userId={}, status={}", bookingId, userId, isApproved);
        return bookingClient.setStatus(userId, bookingId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long bookingId) {
        log.info("Getting booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllBookingsByState(@RequestHeader("X-Sharer-User-Id") long userId,
                                                         @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                         @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        State state = State.from(stateParam)
                .orElseThrow(() -> new UnknownStateException(stateParam));
        log.info("Getting booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.findAllBookingsByState(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findAllOwnerBookingsByState(@RequestHeader("X-Sharer-User-Id") long userId,
                                                              @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        State state = State.from(stateParam)
                .orElseThrow(() -> new UnknownStateException(stateParam));

        log.info("Getting booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.findAllOwnerBookingsByState(userId, state, from, size);
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<Object> findAllBookingsOfItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                                        @PathVariable(name = "itemId") Long itemId) {
        log.info("Finding bookings od itemId {}, userId={}", itemId, userId);
        return bookingClient.findAllBookingsOfItem(itemId, userId);
    }
}



