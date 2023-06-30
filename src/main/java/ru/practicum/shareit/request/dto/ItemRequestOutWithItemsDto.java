package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ItemRequestOutWithItemsDto {
    private Long id;
    private String description;

    private List<ItemDto> items;

    private LocalDateTime created;
}
