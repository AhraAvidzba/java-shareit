package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    @PostMapping
    public Item saveItem(@Valid @RequestBody ItemDto itemDto,
                         @RequestHeader("X-Sharer-User-Id") Long userId) {
        return null;
    }

    @PatchMapping("/{itemId}")
    public ItemDto patchItem(@Valid @RequestBody ItemDto itemDto,
                              @PathVariable Long itemId,
                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        //обновлять только разрешенные и указанные в теле запроса поля (а не весь объект целиком как при post запросе),
        //остальные поля оставлять неизменными (как были)
        //возвращать объект целиком с обновленными полями
        return null;
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId) {
        return null;
    }

    @GetMapping
    public List<ItemDto> getItemsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return null;
    }

    @GetMapping("/items/search")
    public ItemDto searchItems(@RequestParam(name = "text") Long text) {
        //выдавать только доступные для вааренды вещи
        return null;
    }
}
