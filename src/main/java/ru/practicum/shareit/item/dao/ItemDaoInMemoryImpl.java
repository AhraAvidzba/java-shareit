package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemDaoInMemoryImpl implements ItemDao {
    private final Map<Long, Item> items = new HashMap<>();
    private Long globalId = 0L;
    @Override
    public Item saveItem(Item item, Long userId) {
        return null;
    }

    @Override
    public ItemDto patchItem(Item item, Long itemId, Long userId) {
        return null;
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        return null;
    }

    @Override
    public List<ItemDto> getItemsOfUser(Long userId) {
        return null;
    }

    @Override
    public ItemDto searchItems(String text) {
        return null;
    }

    private Long generateId() {
        return globalId++;
    }
}
