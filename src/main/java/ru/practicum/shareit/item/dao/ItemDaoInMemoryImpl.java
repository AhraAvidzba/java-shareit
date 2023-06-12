package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class ItemDaoInMemoryImpl implements ItemDao {
    private final Map<Long, Item> items = new HashMap<>();
    private Long globalId = 1L;

    @Override
    public Item saveItem(Item item) {
        item.setId(generateId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item patchItem(Item item) {
        items.put(item.getId(), item);
        return items.get(item.getId());
    }

    @Override
    public Item getItemById(Long itemId) {
        Item item = items.get(itemId);
        return item.toBuilder().build();
    }

    @Override
    public List<Item> getItemsOfUser(Long userId) {
        return items.values().stream()
                .filter(x -> Objects.equals(x.getOwner().getId(), userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItems(String text) {
        String lowText = text.toLowerCase();
        return items.values().stream()
                .filter(x -> x.getName().toLowerCase().contains(lowText)
                        || x.getDescription().toLowerCase().contains(lowText))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }

    private Long generateId() {
        return globalId++;
    }
}
