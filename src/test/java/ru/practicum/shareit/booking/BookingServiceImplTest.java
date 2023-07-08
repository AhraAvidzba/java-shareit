package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.exceptions.BookingBadRequestException;
import ru.practicum.shareit.exceptions.ContentNotFountException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private BookingServiceImpl bookingService;
    @Captor
    private ArgumentCaptor<Booking> bookingArgumentCaptor;

    private Item createItem() {
        User user = createUser();

        Item item = new Item();
        item.setRequestId(null);
        item.setId(1L);
        item.setName("Дрель");
        item.setAvailable(true);
        item.setDescription("мощная");
        item.setOwner(user);
        return item;
    }

    private User createUser() {
        User user = new User();
        user.setEmail("akhraa1@yandex.ru");
        user.setId(1L);
        user.setName("Akhra");
        return user;
    }

    private Booking createBooking() {
        User booker = createUser();
        booker.setId(2L);
        Item item = createItem();

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().minusDays(5));
        booking.setEnd(LocalDateTime.now().minusDays(4));
        booking.setItem(item);
        booking.setStatus(Status.APPROVED);
        booking.setBooker(booker);
        return booking;
    }

    @Test
    void saveBooking_whenItemNotFound_thenContentNotFountExceptionThrown() {
        Booking booking = createBooking();
        BookingInDto bookingInDto = BookingMapper.mapToBookingInDto(booking);

        Assertions.assertThrows(
                ContentNotFountException.class,
                () -> bookingService.saveBooking(bookingInDto));

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void saveBooking_whenOwnerIsBooker_thenContentNotFountExceptionThrown() {
        Booking booking = createBooking();
        booking.setBooker(createUser());
        BookingInDto bookingInDto = BookingMapper.mapToBookingInDto(booking);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(booking.getItem()));

        Assertions.assertThrows(
                ContentNotFountException.class,
                () -> bookingService.saveBooking(bookingInDto));

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void saveBooking_whenItemNotAvailable_thenBookingBadRequestExceptionThrown() {
        Booking booking = createBooking();
        booking.getItem().setAvailable(false);
        BookingInDto bookingInDto = BookingMapper.mapToBookingInDto(booking);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(booking.getItem()));

        Assertions.assertThrows(
                BookingBadRequestException.class,
                () -> bookingService.saveBooking(bookingInDto));

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void saveBooking_whenBookingTimeIsWrong_thenBookingBadRequestExceptionThrown() {
        Booking booking = createBooking();
        booking.setStart(LocalDateTime.now().plusDays(100));
        BookingInDto bookingInDto = BookingMapper.mapToBookingInDto(booking);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(booking.getItem()));

        Assertions.assertThrows(
                BookingBadRequestException.class,
                () -> bookingService.saveBooking(bookingInDto));

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void saveBooking_whenItemAlreadyBooked_thenContentNotFountExceptionThrown() {
        Booking booking = createBooking();
        BookingInDto bookingInDto = BookingMapper.mapToBookingInDto(booking);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(booking.getItem()));
        when(bookingRepository.findTimeCrossingBookings(anyLong(), any(), any())).thenReturn(List.of(booking));

        Assertions.assertThrows(
                ContentNotFountException.class,
                () -> bookingService.saveBooking(bookingInDto));

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void saveBooking_whenUserNotFound_thenContentNotFountExceptionThrown() {
        Booking booking = createBooking();
        BookingInDto bookingInDto = BookingMapper.mapToBookingInDto(booking);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(booking.getItem()));

        Assertions.assertThrows(
                ContentNotFountException.class,
                () -> bookingService.saveBooking(bookingInDto));

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void saveBooking_whenUserAndItemFoundAndNoTimeCrossing_thenReturnSavedBooking() {
        Booking booking = createBooking();
        BookingInDto bookingInDto = BookingMapper.mapToBookingInDto(booking);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(booking.getItem()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booking.getBooker()));
        when(bookingRepository.save(any())).thenReturn(booking);

        bookingService.saveBooking(bookingInDto);

        verify(bookingRepository, times(1)).save(bookingArgumentCaptor.capture());
        Booking savedBooking = bookingArgumentCaptor.getValue();

        assertThat(savedBooking.getStatus(), equalTo(Status.WAITING));
    }

    @Test
    void setStatus_whenBookingNotFound_thenContentNotFountExceptionThrown() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(
                ContentNotFountException.class,
                () -> bookingService.setStatus(1L, 1L, true));

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void setStatus_whenUserIsNotOwnerOfItem_thenContentNotFountExceptionThrown() {
        Booking booking = createBooking();
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        Assertions.assertThrows(
                ContentNotFountException.class,
                () -> bookingService.setStatus(1L, 2L, true));

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void setStatus_whenStatusIsUpToDate_thenBookingBadRequestExceptionThrown() {
        Booking booking = createBooking();
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        Assertions.assertThrows(
                BookingBadRequestException.class,
                () -> bookingService.setStatus(1L, 1L, true));

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void setStatus_whenBookingFoundAndUserIsOwner_thenReturnUpdatedUser() {
        Booking booking = createBooking();
        booking.setStatus(Status.WAITING);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenReturn(booking);

        bookingService.setStatus(1L, 1L, true);

        verify(bookingRepository, times(1)).save(bookingArgumentCaptor.capture());
        Booking savedBooking = bookingArgumentCaptor.getValue();

        verify(bookingRepository, times(1)).save(any());
        assertThat(savedBooking.getId(), equalTo(1L));
        assertThat(savedBooking.getStatus(), equalTo(Status.APPROVED));
    }

    @Test
    void getBooking_whenBookingNotFound_thenContentNotFountExceptionThrown() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(
                ContentNotFountException.class,
                () -> bookingService.getBooking(1L, 1L));
    }

    @Test
    void getBooking_whenUserIsNotOwnerOrBooker_thenContentNotFountExceptionThrown() {
        Booking booking = createBooking();
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        Assertions.assertThrows(
                ContentNotFountException.class,
                () -> bookingService.getBooking(3L, 1L));
    }

    @Test
    void getBooking_whenAllIsOk_thenReturnBooking() {
        Booking booking = createBooking();
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        BookingOutDto bookingOutDto = bookingService.getBooking(1L, 1L);
        assertThat(bookingOutDto.getId(), equalTo(booking.getId()));
    }

    @Test
    void findAllBookingsByState_whenUserNotFound_thenContentNotFountExceptionThrown() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(
                ContentNotFountException.class,
                () -> bookingService.findAllBookingsByState(1L, State.ALL, 0, 10));
    }

    @Test
    void findAllBookingsByState_whenAllBookingsRequired_thenInvokeRepositoryMethod() {
        Booking booking = createBooking();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booking.getBooker()));

        bookingService.findAllBookingsByState(1L, State.ALL, 0, 10);

        verify(bookingRepository, times(1)).findByBookerId(anyLong(), any());
    }

    @Test
    void findAllBookingsByState_whenCurrentBookingsRequired_thenInvokeRepositoryMethod() {
        Booking booking = createBooking();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booking.getBooker()));

        bookingService.findAllBookingsByState(1L, State.CURRENT, 0, 10);

        verify(bookingRepository, times(1)).findByCurrentBooker(anyLong(), any(), any());
    }

    @Test
    void findAllBookingsByState_whenPastBookingsRequired_thenInvokeRepositoryMethod() {
        Booking booking = createBooking();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booking.getBooker()));

        bookingService.findAllBookingsByState(1L, State.PAST, 0, 10);

        verify(bookingRepository, times(1)).findByBookerIdAndEndIsBefore(anyLong(), any(), any());
    }

    @Test
    void findAllBookingsByState_whenFutureBookingsRequired_thenInvokeRepositoryMethod() {
        Booking booking = createBooking();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booking.getBooker()));

        bookingService.findAllBookingsByState(1L, State.FUTURE, 0, 10);

        verify(bookingRepository, times(1)).findByBookerIdAndStartIsAfter(anyLong(), any(), any());
    }

    @Test
    void findAllBookingsByState_whenBookingsWithStatusRequired_thenInvokeRepositoryMethod() {
        Booking booking = createBooking();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booking.getBooker()));

        bookingService.findAllBookingsByState(1L, State.REJECTED, 0, 10);

        verify(bookingRepository, times(1)).findByBookerIdAndStatus(anyLong(), any(), any());
    }

    @Test
    void findAllOwnerBookingsByState_whenUserNotFound_thenContentNotFountExceptionThrown() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(
                ContentNotFountException.class,
                () -> bookingService.findAllOwnerBookingsByState(1L, State.ALL, 0, 10));
    }

    @Test
    void findAllOwnerBookingsByState_whenAllBookingsRequired_thenInvokeRepositoryMethod() {
        Booking booking = createBooking();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booking.getBooker()));

        bookingService.findAllOwnerBookingsByState(1L, State.ALL, 0, 10);

        verify(bookingRepository, times(1)).findByItemOwnerId(anyLong(), any());
    }

    @Test
    void findAllOwnerBookingsByState_whenCurrentBookingsRequired_thenInvokeRepositoryMethod() {
        Booking booking = createBooking();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booking.getBooker()));

        bookingService.findAllOwnerBookingsByState(1L, State.CURRENT, 0, 10);

        verify(bookingRepository, times(1)).findByOwnerCurrentBooker(anyLong(), any(), any());
    }

    @Test
    void findAllOwnerBookingsByState_whenPastBookingsRequired_thenInvokeRepositoryMethod() {
        Booking booking = createBooking();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booking.getBooker()));

        bookingService.findAllOwnerBookingsByState(1L, State.PAST, 0, 10);

        verify(bookingRepository, times(1)).findByItemOwnerIdAndEndIsBefore(anyLong(), any(), any());
    }

    @Test
    void findAllOwnerBookingsByState_whenFutureBookingsRequired_thenInvokeRepositoryMethod() {
        Booking booking = createBooking();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booking.getBooker()));

        bookingService.findAllOwnerBookingsByState(1L, State.FUTURE, 0, 10);

        verify(bookingRepository, times(1)).findByItemOwnerIdAndStartIsAfter(anyLong(), any(), any());
    }

    @Test
    void findAllOwnerBookingsByState_whenBookingsWithStatusRequired_thenInvokeRepositoryMethod() {
        Booking booking = createBooking();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booking.getBooker()));

        bookingService.findAllOwnerBookingsByState(1L, State.REJECTED, 0, 10);

        verify(bookingRepository, times(1)).findByItemOwnerIdAndStatus(anyLong(), any(), any());
    }

    @Test
    void findAllBookingsOfItem() {
        Booking booking = createBooking();
        when(bookingRepository.findByItemId(anyLong(), any())).thenReturn(List.of(booking));

        List<BookingOutDto> BookingOutDtoList = bookingService.findAllBookingsOfItem(1L);
        assertThat(booking.getId(), equalTo(BookingOutDtoList.get(0).getId()));
    }
}