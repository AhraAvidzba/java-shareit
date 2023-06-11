package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class Item {
    private String name;
    private String description;
    private boolean available;
    private ItemRequest request;
}
