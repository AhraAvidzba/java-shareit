package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    @PostMapping
    public Item saveItem(@Valid @RequestBody ItemDto itemDto,
                         @RequestHeader("X-Sharer-User-Id") Long userId) {
        Item item = ItemMapper.toItem(itemDto);
        return itemService.saveItem(item, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto patchItem(@Valid @RequestBody ItemDto itemDto,
                              @PathVariable Long itemId,
                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        //обновлять только разрешенные и указанные в теле запроса поля (а не весь объект целиком как при post запросе),
        //остальные поля оставлять неизменными (как были)
        //возвращать объект целиком с обновленными полями
        Item item = ItemMapper.toItem(itemDto);
        return itemService.patchItem(item, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDto> getItemsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItemsOfUser(userId);
    }

    @GetMapping("/items/search")
    public ItemDto searchItems(@RequestParam(name = "text") String text) {
        //выдавать только доступные для вааренды вещи
        return itemService.searchItems(text);
    }
}
