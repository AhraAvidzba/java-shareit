package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingIdOutDto;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.List;

@Getter
@Setter
public class ItemWithBookAndCommentsDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    private Long owner;
    private Long request;
    @Past
    private BookingIdOutDto lastBooking;
    @Future
    private BookingIdOutDto nextBooking;
    private List<CommentDto> comments;
}
