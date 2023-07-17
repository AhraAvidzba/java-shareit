package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.request.dto.ItemRequestOutWithItemsDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestsService itemRequestsService;

    @PostMapping
    public ItemRequestOutDto addRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @Valid @RequestBody ItemRequestInDto itemRequestInDto) {
        itemRequestInDto.setUserId(userId);
        ItemRequestOutDto savedItemRequestOutDto = itemRequestsService.addRequest(itemRequestInDto);
        log.info("Добавлен запрос, id = {}", savedItemRequestOutDto.getId());
        return savedItemRequestOutDto;
    }

    @GetMapping
    public List<ItemRequestOutWithItemsDto> getAllUserRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Возвращен список всех запросов пользователя");
        return itemRequestsService.getAllUserRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestOutWithItemsDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                           @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                           @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Возвращен список запросов других пользователей");
        return itemRequestsService.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestOutWithItemsDto getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @PathVariable(name = "requestId") Long requestId) {
        log.info("Возвращен запрос с id = {}", requestId);
        return itemRequestsService.getRequestById(userId, requestId);
    }

}
