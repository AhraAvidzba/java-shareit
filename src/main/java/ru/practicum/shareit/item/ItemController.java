package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.EditingNotAllowedException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;

    @PostMapping
    public Item saveItem(@Valid @RequestBody ItemDto itemDto,
                         @RequestHeader("X-Sharer-User-Id") Long userId) {
        Item item = ItemMapper.toItem(itemDto);
        User owner = userService.getUserById(userId);
        item.setOwner(owner);
        Item savedItem = itemService.putItem(item);
        log.info("Сохранена вещь, id = {}", savedItem.getId());
        return savedItem;
    }

    @PatchMapping("/{itemId}")
    public ItemDto patchItem(@RequestBody ItemDto itemDto,
                             @PathVariable Long itemId,
                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        Item item = itemService.getItemById(itemId);
        if (!userId.equals(item.getOwner().getId())) {
            throw new EditingNotAllowedException("Вещь может редактировать только ее владелец");
        }

        List<String> fields = new ArrayList<>();
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
            fields.add("name");
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
            fields.add("description");

        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
            fields.add("available");
        }
        Item newItem = itemService.patchItem(item);

        String allFields = "";
        for (String field : fields) {
            allFields = allFields + " " + field;
        }
        log.info("Обновлены поля у вещи с id {}: {}", newItem.getId(), allFields);
        return ItemMapper.toItemDto(newItem);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId) {
        log.info("Возвращена вещь с id = {}", itemId);
        return ItemMapper.toItemDto(itemService.getItemById(itemId));
    }

    @GetMapping
    public List<ItemDto> getItemsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Возвращен список вещей пользователя с id = {}", userId);
        return itemService.getItemsOfUser(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam(name = "text") String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        log.info("Возвращен список всех вещей содеражащих в названии либо описании текст: {} ", text);
        return itemService.searchItems(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
