package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.exceptions.ContentNotFountException;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDao;
    private final UserService userService;

    @Override
    public Item putItem(Item item) {
        return itemDao.saveItem(item);
    }

    @Override
    @Validated
    public Item patchItem(@Valid Item item) {
        if (item.getId() == null || itemDao.getItemById(item.getId()) == null) {
            throw new ContentNotFountException("Вещь не найдена");
        }
        return itemDao.patchItem(item);
    }

    @Override
    public Item getItemById(Long itemId) {
        Item item = itemDao.getItemById(itemId);
        if (item == null) {
            throw new ContentNotFountException("Вещи с id = " + itemId + " не существует");
        }
        return item;
    }

    @Override
    public List<Item> getItemsOfUser(Long userId) {
        userService.getUserById(userId); //Проверка существования пользователя с таким id
        return itemDao.getItemsOfUser(userId);
    }

    @Override
    public List<Item> searchItems(String text) {
        return itemDao.searchItems(text);
    }
}
