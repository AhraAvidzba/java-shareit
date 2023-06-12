package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item putItem(Item item);

    Item patchItem(Item item);

    Item getItemById(Long itemId);

    List<Item> getItemsOfUser(Long userId);

    List<Item> searchItems(String text);
}
