package ru.practicum.shareit.item.service;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

public interface ItemService {

    public Item saveItem(Item item, Long userId);
    ItemDto patchItem(Item item, Long itemId, Long userId);
    ItemDto getItemById(Long itemId);
    List<ItemDto> getItemsOfUser(Long userId);
    ItemDto searchItems(String text);
}
