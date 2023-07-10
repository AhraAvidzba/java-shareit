package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@Transactional
@SpringBootTest(
        properties = "spring.datasource.url=jdbc:h2:mem:shareit",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplIntegrationTest {
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;


    @Test
    void findAllBookingsByState() {
        UserDto savedUser1 = userService.saveUser(UserMapper.toUserDto(maketeUser("Akhra", "akhra@yandex.ru")));
        UserDto savedUser2 = userService.saveUser(UserMapper.toUserDto(maketeUser("Anri", "anri@yandex.ru")));
        Item item = makeItem("Отвертка", UserMapper.toUser(savedUser1));
        ItemDto savedItem = itemService.saveItem(ItemMapper.toItemDto(item), savedUser1.getId());
        item.setId(savedItem.getId());
        Booking booking = makeBooking(item, UserMapper.toUser(savedUser2));
        BookingOutDto savedBooking = bookingService.saveBooking(BookingMapper.mapToBookingInDto(booking));
        booking.setId(savedBooking.getId());

        List<BookingOutDto> targetBookings = bookingService.findAllBookingsByState(savedUser2.getId(), State.ALL, 0, 10);

        assertThat(targetBookings, hasSize(1));
        assertThat(targetBookings.get(0).getId(), notNullValue());
        assertThat(savedBooking, equalTo(targetBookings.get(0)));
    }

    private Booking makeBooking(Item item, User booker) {
        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setStart(LocalDateTime.now().plusDays(4));
        booking.setEnd(LocalDateTime.now().plusDays(4));
        booking.setBooker(booker);
        booking.setItem(item);
        return booking;
    }

    private Item makeItem(String itemName, User owner) {
        Item item = new Item();
        item.setName(itemName);
        item.setAvailable(true);
        item.setDescription("cool");
        item.setOwner(owner);
        return item;
    }

    private User maketeUser(String name, String Email) {
        User user = new User();
        user.setName(name);
        user.setEmail(Email);
        return user;
    }
}