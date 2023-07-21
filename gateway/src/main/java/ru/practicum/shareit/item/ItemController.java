package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.validations.Create;
import ru.practicum.shareit.validations.Update;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> saveItem(@Validated(Create.class) @RequestBody ItemDto itemDto,
                                           @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Creating item {}, userId={}", itemDto, userId);
        return itemClient.saveItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> patchItem(@Validated(Update.class) @RequestBody ItemDto itemDto,
                                            @PathVariable Long itemId,
                                            @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Updating item {}, itemId={}, userId={}", itemDto, itemId, userId);
        return itemClient.patchItem(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable Long itemId,
                                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Getting item {}, userId={}", itemId, userId);
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                 @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Getting items of userId {}, from=={}, size={}", userId, from, size);
        return itemClient.getItemsOfUser(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam(name = "text") String text,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Searching items containing {}, from={}, size={} by userId={}", text, from, size, userId);
        return itemClient.searchItems(userId, from, size, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> saveComment(@Valid @RequestBody CommentDto commentDto,
                                              @RequestHeader("X-Sharer-User-Id") Long userId,
                                              @PathVariable(name = "itemId") Long itemId) {
        log.info("Creating comment {}, userId={}", commentDto, userId);
        return itemClient.saveComment(commentDto, userId, itemId);
    }
}
