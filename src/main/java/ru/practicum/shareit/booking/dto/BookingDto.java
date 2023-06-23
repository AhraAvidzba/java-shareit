package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.Status;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@ToString
@Getter
@Setter
public class BookingDto {
    private Long id;

    @Future
    private LocalDateTime start;

    @Future
    private LocalDateTime end;

    private Long itemId;

    private Long bookerId;

    private Status status;
}
