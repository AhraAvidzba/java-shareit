package ru.practicum.shareit.exeptions;

public class UnknownStateException extends RuntimeException {
    public UnknownStateException(String message) {
        super(message);
    }
}
