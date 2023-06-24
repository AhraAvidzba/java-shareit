package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingIdOutDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

@UtilityClass
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setOwner(item.getOwner().getId());
//        itemDto.setRequest(item.getRequest().getId());
        return itemDto;
    }

    public static ItemIdDto toItemIdDto(Item item, BookingIdOutDto lastBooking, BookingIdOutDto nextBooking) {
        ItemIdDto itemDto = new ItemIdDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setOwner(item.getOwner().getId());
        itemDto.setLastBooking(lastBooking);
        itemDto.setNextBooking(nextBooking);
//        itemDto.setRequest(item.getRequest().getId());
        return itemDto;
    }

    public static Item toItem(ItemDto itemDto, User owner) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(owner);
//        item.setRequest(request);
        return item;
    }
}
