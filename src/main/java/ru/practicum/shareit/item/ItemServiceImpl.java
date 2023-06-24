package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ContentNotFountException;
import ru.practicum.shareit.exceptions.EditingNotAllowedException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

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
    private final UserRepository userRepository;

    @Override
    public ItemDto saveItem(ItemDto itemDto, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new ContentNotFountException("Пользователь не найден"));
        Item item = ItemMapper.toItem(itemDto, owner);
        Item savedItem = itemRepository.save(item);
        return ItemMapper.toItemDto(savedItem);
    }

    @Override
    public ItemDto patchItem(ItemDto itemDto, Long userId) {
        if (itemDto.getId() == null) {
            throw new ContentNotFountException("Необходимо указать id вещи");
        }
        Item item = itemRepository.findById(itemDto.getId())
                .orElseThrow(() -> new ContentNotFountException("Вещь не найдена"));
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
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ContentNotFountException("Вещи с id = " + itemId + " не существует"));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getItemsOfUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ContentNotFountException("Пользователь не найден"));
        return itemRepository.findAllByOwnerId(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        return itemRepository.findAllByNameOrDescription(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

}
