package ru.practicum.shareit.item;

import java.util.List;

public interface ItemRepository {

    Item saveItem(Item item);

    Item patchItem(Item item);

    Item getItemById(Long itemId);

    List<Item> getItemsOfUser(Long userId);

    List<Item> searchItems(String text);
}
