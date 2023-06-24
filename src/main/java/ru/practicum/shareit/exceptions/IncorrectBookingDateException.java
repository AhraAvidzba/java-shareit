package ru.practicum.shareit.exceptions;

public class IncorrectBookingDateException extends RuntimeException {
    public IncorrectBookingDateException(String message) {
        super(message);
    }
}