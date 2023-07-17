package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.request.dto.ItemRequestOutWithItemsDto;

import java.util.List;


@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestsService itemRequestsService;

    @PostMapping
    public ItemRequestOutDto addRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @RequestBody ItemRequestInDto itemRequestInDto) {
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
                                                           @RequestParam(name = "from", defaultValue = "0") int from,
                                                           @RequestParam(name = "size", defaultValue = "10") int size) {
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
