package ru.practicum.shareit.exceptions;

public class UserDontHaveBookingException extends RuntimeException {
    public UserDontHaveBookingException(String message) {
        super(message);
    }
}