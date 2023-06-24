package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemIdDto;

import java.util.List;

public interface ItemService {

    ItemDto saveItem(ItemDto itemDto, Long userId);

    ItemDto patchItem(ItemDto itemDto, Long userId);

    ItemIdDto getItemById(Long itemId);

    List<ItemDto> getItemsOfUser(Long userId);

    List<ItemDto> searchItems(String text);
}
