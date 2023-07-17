package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exeptions.UnknownStateException;

import java.util.List;
import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getBookings(long userId, State state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }


    public ResponseEntity<Object> bookItem(long userId, BookItemRequestDto requestDto) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> getBooking(long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }


//
//
//
//    public ResponseEntity<Object> saveBooking(BookingInDto bookingDto, Long userId) {
//        bookingDto.setBookerId(userId);
//        return post("", userId, bookingDto);
//    }
//
//    public ResponseEntity<Object> setStatus(Long userId, Long bookingId, Boolean isApproved) {
//        Map<String, Object> parameters = Map.of(
//                "bookingId", bookingId,
//                "isApproved", isApproved
//        );
//        return patch("/" + bookingId, userId, parameters);
//    }
//
//    public BookingOutDto getBooking(Long userId, Long bookingId) {
//        return bookingService.getBooking(userId, bookingId);
//    }
//
//    public List<BookingOutDto> findAllBookingsByState(Long userId, String strState, int from, int size) {
//        State state;
//        try {
//            state = State.valueOf(strState);
//        } catch (IllegalArgumentException e) {
//            throw new UnknownStateException(strState);
//        }
//        return bookingService.findAllBookingsByState(userId, state, from, size);
//    }
//
//    public List<BookingOutDto> findAllOwnerBookingsByState(Long userId, String strState, int from, int size) {
//        State state;
//        try {
//            state = State.valueOf(strState);
//        } catch (IllegalArgumentException e) {
//            throw new UnknownStateException(strState);
//        }
//        return bookingService.findAllOwnerBookingsByState(userId, state, from, size);
//    }
//
//
//    public List<BookingOutDto> findAllBookingsOfItem(Long itemId) {
//        return bookingService.findAllBookingsOfItem(itemId);
//    }
}
