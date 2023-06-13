package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.ContentNotFountException;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto saveItem(@Valid @RequestBody ItemDto itemDto,
                            @RequestHeader("X-Sharer-User-Id") Long userId) {
        ItemDto savedItemDto = itemService.saveItem(itemDto, userId);
        log.info("Сохранена вещь, id = {}", savedItemDto.getId());
        return savedItemDto;
    }

    @PatchMapping("/{itemId}")
    public ItemDto patchItem(@RequestBody ItemDto itemDto,
                             @PathVariable Long itemId,
                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        if (itemId == null) {
            throw new ContentNotFountException("Необходимо указать id вещи");
        }
        itemDto.setId(itemId);
        ItemDto savedItemDto = itemService.patchItem(itemDto, userId);
        log.info("Обновлены поля у вещи с id {}", savedItemDto.getId());
        return savedItemDto;
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId) {
        log.info("Возвращена вещь с id = {}", itemId);
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDto> getItemsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Возвращен список вещей пользователя с id = {}", userId);
        return itemService.getItemsOfUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam(name = "text") String text) {
        log.info("Возвращен список всех вещей содеражащих в названии либо описании текст: {} ", text);
        return itemService.searchItems(text);
    }
}
