package ru.practicum.shareit.item.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ContentNotFountException;
import ru.practicum.shareit.exceptions.EditingNotAllowedException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserMapper;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public ItemDto saveItem(ItemDto itemDto, Long userId) {
        Item item = ItemMapper.toItem(itemDto);
        User owner = UserMapper.toUser(userService.getUserById(userId));
        item.setOwner(owner);
        Item savedItem = itemRepository.saveItem(item);
        return ItemMapper.toItemDto(savedItem);
    }

    @Override
    public ItemDto patchItem(ItemDto itemDto, Long userId) {
        Item item = itemRepository.getItemById(itemDto.getId());
        if (item == null) {
            throw new ContentNotFountException("Вещь не найдена");
        }
        if (!userId.equals(item.getOwner().getId())) {
            throw new EditingNotAllowedException("Вещь может редактировать только ее владелец");
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());

        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        //Валидация Item
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        validator.validate(item);
        return ItemMapper.toItemDto(itemRepository.patchItem(item));
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        Item item = itemRepository.getItemById(itemId);
        if (item == null) {
            throw new ContentNotFountException("Вещи с id = " + itemId + " не существует");
        }
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getItemsOfUser(Long userId) {
        userService.getUserById(userId); //Проверка существования пользователя с таким id
        return itemRepository.getItemsOfUser(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        return itemRepository.searchItems(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
