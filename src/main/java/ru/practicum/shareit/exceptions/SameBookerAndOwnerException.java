package ru.practicum.shareit.exceptions;

public class SameBookerAndOwnerException extends RuntimeException {
    public SameBookerAndOwnerException(String message) {
        super(message);
    }
}