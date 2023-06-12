package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDao {

    public Item saveItem(Item item, Long userId);
    ItemDto patchItem(Item item, Long itemId, Long userId);
    ItemDto getItemById(Long itemId);
    List<ItemDto> getItemsOfUser(Long userId);
    ItemDto searchItems(String text);
}
