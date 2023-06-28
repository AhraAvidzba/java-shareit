package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookAndCommentsDto;

import java.util.List;

public interface ItemService {

    ItemDto saveItem(ItemDto itemDto, Long userId);

    ItemDto patchItem(ItemDto itemDto, Long userId);

    ItemWithBookAndCommentsDto getItemById(Long itemId, Long userId);

    List<ItemWithBookAndCommentsDto> getItemsOfUser(Long userId);

    List<ItemDto> searchItems(String text);

    CommentDto saveComment(CommentDto commentDto);
}
