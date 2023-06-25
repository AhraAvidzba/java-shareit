package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemIdDto;

import java.util.List;

public interface ItemService {

    ItemDto saveItem(ItemDto itemDto, Long userId);

    ItemDto patchItem(ItemDto itemDto, Long userId);

    ItemIdDto getItemById(Long itemId, Long userId);

    List<ItemIdDto> getItemsOfUser(Long userId);

    List<ItemDto> searchItems(String text);

    CommentDto saveComment(CommentDto commentDto);
}
