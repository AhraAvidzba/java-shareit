package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice("ru.practicum.shareit")
public class ExceptionApiHandler {
    @ExceptionHandler(ContentNotFountException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleException(ContentNotFountException exception) {
        return Map.of("Ошибка поиска: ", exception.getMessage());
    }

    @ExceptionHandler(ContentAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleException(ContentAlreadyExistException exception) {
        return Map.of("Конфликт: ", exception.getMessage());
    }

    @ExceptionHandler(EditingNotAllowedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, String> handleException(EditingNotAllowedException exception) {
        return Map.of("Редактирование невозможно: ", exception.getMessage());
    }

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleException(NumberFormatException exception) {
        return Map.of("Неверный формат данных: ", exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleException(MethodArgumentNotValidException exception) {
        Map<String, String> messages = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(e -> {
            String field = ((FieldError) e).getField();
            String message = e.getDefaultMessage();
            messages.put(field, message);
        });
        return messages;
    }
}
