package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements  ItemService {
    private final ItemDao itemDao;
    @Override
    public Item saveItem(Item item, Long userId) {
        return itemDao.saveItem(item, userId);
    }

    @Override
    public ItemDto patchItem(Item item, Long itemId, Long userId) {
        return itemDao.patchItem(item, itemId, userId);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        return itemDao.getItemById(itemId);
    }

    @Override
    public List<ItemDto> getItemsOfUser(Long userId) {
        return itemDao.getItemsOfUser(userId);
    }

    @Override
    public ItemDto searchItems(String text) {
        return itemDao.searchItems(text);
    }
}
