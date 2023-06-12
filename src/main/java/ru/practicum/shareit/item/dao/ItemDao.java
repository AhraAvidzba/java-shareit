package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDao {

    Item saveItem(Item item);

    Item patchItem(Item item);

    Item getItemById(Long itemId);

    List<Item> getItemsOfUser(Long userId);

    List<Item> searchItems(String text);
}
