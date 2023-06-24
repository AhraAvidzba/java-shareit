package ru.practicum.shareit.exceptions;

public class AccessRestrictedException extends RuntimeException {
    public AccessRestrictedException(String message) {
        super(message);
    }
}
