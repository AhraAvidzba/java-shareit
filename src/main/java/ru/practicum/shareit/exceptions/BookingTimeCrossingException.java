package ru.practicum.shareit.exceptions;

public class BookingTimeCrossingException extends RuntimeException {
    public BookingTimeCrossingException(String message) {
        super(message);
    }
}
