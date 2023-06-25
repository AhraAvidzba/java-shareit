package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingIdOutDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exceptions.ContentNotFountException;
import ru.practicum.shareit.exceptions.EditingNotAllowedException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemIdDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

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
    public ItemIdDto getItemById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ContentNotFountException("Вещи с id = " + itemId + " не существует"));
        BookingIdOutDto lastBooking = getLastBookings(List.of(itemId), userId).get(itemId);
        BookingIdOutDto nextBooking = getNextBookings(List.of(itemId), userId).get(itemId);
        return ItemMapper.toItemIdDto(item, lastBooking, nextBooking);
    }

    @Override
    public List<ItemIdDto> getItemsOfUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ContentNotFountException("Пользователь не найден"));
        List<Item> items = itemRepository.findAllByOwnerId(userId, Sort.by("id").ascending());
        HashMap<Long, BookingIdOutDto> itemLastBookings = getLastBookings(items.stream()
                .map(Item::getId).collect(Collectors.toList()), userId);
        HashMap<Long, BookingIdOutDto> itemNextBookings = getNextBookings(items.stream()
                .map(Item::getId).collect(Collectors.toList()), userId);
        List<ItemIdDto> itemsIdDto = new ArrayList<>();
        for (Item item : items) {
            itemsIdDto.add(ItemMapper.toItemIdDto(item,
                    itemLastBookings.get(item.getId()),
                    itemNextBookings.get(item.getId())));
        }
        return itemsIdDto;
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

    private HashMap<Long, BookingIdOutDto> getLastBookings(List<Long> itemId, Long userId) {
        LocalDateTime targetDate = LocalDateTime.now();
        List<Booking> lastItemBookings = bookingRepository.findByItemIdIn(itemId, Sort.by("end").descending());
        HashMap<Long, BookingIdOutDto> itemBookings = new HashMap<>();
        for (Booking booking : lastItemBookings) {
            if (!itemBookings.containsKey(booking.getItem().getId())) {
                if (booking.getEnd().isBefore(targetDate)
                        && booking.getItem().getOwner().getId().equals(userId)
                        && booking.getStatus().equals(Status.APPROVED)) {
                    itemBookings.put(booking.getItem().getId(), BookingMapper.mapToBookingIdOutDto(booking));
                }
            }
        }
        return itemBookings;
    }

    private HashMap<Long, BookingIdOutDto> getNextBookings(List<Long> itemId, Long userId) {
        LocalDateTime targetDate = LocalDateTime.now();
        List<Booking> nextItemBookings = bookingRepository.findByItemIdIn(itemId, Sort.by("start").ascending());
        HashMap<Long, BookingIdOutDto> itemBookings = new HashMap<>();
        for (Booking booking : nextItemBookings) {
            if (!itemBookings.containsKey(booking.getItem().getId())) {
                if (booking.getStart().isAfter(targetDate)
                        && booking.getItem().getOwner().getId().equals(userId)
                        && booking.getStatus().equals(Status.APPROVED)) {
                    itemBookings.put(booking.getItem().getId(), BookingMapper.mapToBookingIdOutDto(booking));
                }
            }
        }
        return itemBookings;
    }

}
