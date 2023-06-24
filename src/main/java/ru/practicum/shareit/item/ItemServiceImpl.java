package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
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
import java.util.Collections;
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
    public ItemIdDto getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ContentNotFountException("Вещи с id = " + itemId + " не существует"));
        List<Booking> lastItemBookings = bookingRepository.findByItemId(itemId, Sort.by("start").descending());
        List<Booking> nextItemBookings = bookingRepository.findByItemId(itemId, Sort.by("end").ascending());
        BookingIdOutDto lastBooking = BookingMapper.mapToBookingIdOutDto(getLastBooking(lastItemBookings, LocalDateTime.now()));
        BookingIdOutDto nextBooking = BookingMapper.mapToBookingIdOutDto(getNextBooking(nextItemBookings, LocalDateTime.now()));
        return ItemMapper.toItemIdDto(item, lastBooking, nextBooking);
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

    private Booking getLastBooking(List<Booking> bookings, LocalDateTime targetDate) {
        for (Booking booking : bookings) {
            if (booking.getEnd().isBefore(targetDate)) return booking;
        }
        return null;
    }

    private Booking getNextBooking(List<Booking> bookings, LocalDateTime targetDate) {
        for (Booking booking : bookings) {
            if (booking.getEnd().isAfter(targetDate)) return booking;
        }
        return null;
    }

}
