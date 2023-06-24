package ru.practicum.shareit.exceptions;

public class UnavalableItemBookingException extends RuntimeException {
    public UnavalableItemBookingException(String message) {
        super(message);
    }
}
