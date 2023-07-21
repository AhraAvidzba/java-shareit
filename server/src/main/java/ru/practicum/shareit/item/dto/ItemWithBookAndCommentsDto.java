package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.dto.BookingIdOutDto;

import java.util.List;

@Getter
@Setter
@ToString
public class ItemWithBookAndCommentsDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long owner;
    private Long requestId;
    private BookingIdOutDto lastBooking;
    private BookingIdOutDto nextBooking;
    private List<CommentDto> comments;
}
