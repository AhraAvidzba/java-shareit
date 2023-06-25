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
        return Map.of("Операция невозможна: ", exception.getMessage());
    }

    @ExceptionHandler(AccessRestrictedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleException(AccessRestrictedException exception) {
        return Map.of("Доступ невозможен: ", exception.getMessage());
    }

    @ExceptionHandler(UnavalableItemBookingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleException(UnavalableItemBookingException exception) {
        return Map.of("Бронирование невозможно: ", exception.getMessage());
    }

    @ExceptionHandler(IncorrectBookingDateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleException(IncorrectBookingDateException exception) {
        return Map.of("Бронирование невозможно: ", exception.getMessage());
    }

    @ExceptionHandler(BookingTimeCrossingException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleException(BookingTimeCrossingException exception) {
        return Map.of("Бронирование невозможно: ", exception.getMessage());
    }

    @ExceptionHandler(SameBookerAndOwnerException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleException(SameBookerAndOwnerException exception) {
        return Map.of("Бронирование невозможно: ", exception.getMessage());
    }

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleException(NumberFormatException exception) {
        return Map.of("Неверный формат данных: ", exception.getMessage());
    }

    @ExceptionHandler(UserDontHaveBookingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleException(UserDontHaveBookingException exception) {
        return Map.of("Неверный запрос: ", exception.getMessage());
    }

    @ExceptionHandler(UnknownStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleException(UnknownStateException exception) {
        return Map.of("error", "Unknown state: " + exception.getMessage());
    }

    @ExceptionHandler(IsUpToDateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleException(IsUpToDateException exception) {
        return Map.of("error", exception.getMessage());
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
