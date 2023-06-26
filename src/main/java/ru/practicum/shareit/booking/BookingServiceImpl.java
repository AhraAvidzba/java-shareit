package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingOutDto saveBooking(BookingInDto bookingDto) {
        Item item = getItem(bookingDto.getItemId());
        if (item.getOwner().getId().equals(bookingDto.getBookerId())) {
            throw new SameBookerAndOwnerException("Вледелец вещи не может ее забронировать");
        }
        if (!item.getAvailable()) {
            throw new UnavalableItemBookingException("Данная вещь недоступна для бранирования");
        }
        if (bookingDto.getStart().isAfter(bookingDto.getEnd())
                || bookingDto.getStart().isEqual(bookingDto.getEnd())) {
            throw new IncorrectBookingDateException("Время начала бронирования не может быть позже либо равным времени его окончания");
        }
        List<BookingOutDto> bookingsOfItem = findAllBookingsOfItem(bookingDto.getItemId());
        for (BookingOutDto booking : bookingsOfItem) {
            if (bookingDto.getStart().isAfter(booking.getStart()) && bookingDto.getStart().isBefore(booking.getEnd())
                    || bookingDto.getEnd().isAfter(booking.getStart()) && bookingDto.getEnd().isBefore(booking.getEnd())) {
                throw new BookingTimeCrossingException("Данная вещь уже забронирована в запрашиваемые даты");
            }
        }
        User user = getUser(bookingDto.getBookerId());
        bookingDto.setStatus(Status.WAITING);
        Booking booking = BookingMapper.mapToBooking(bookingDto, item, user);
        Booking savedBooking = bookingRepository.save(booking);
        return BookingMapper.mapToBookingOutDto(savedBooking);
    }

    @Override
    public BookingOutDto setStatus(Long bookingId, Long userId, Boolean isApproved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ContentNotFountException("Бронирования с id = " + bookingId + " не существует"));
        if (!Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new AccessRestrictedException("Бранирование может подтвердить только владелец вещи");
        }
        Status status = isApproved ? Status.APPROVED : Status.REJECTED;
        if (Objects.equals(booking.getStatus(), status)) {
            throw new IsUpToDateException("Статус в актуальном состоянии");
        }
        booking.setStatus(status);
        Booking savedBooking = bookingRepository.save(booking);
        return BookingMapper.mapToBookingOutDto(savedBooking);
    }

    public BookingOutDto getBooking(Long userId, Long bookingId) {
        Booking booking = getBooking(bookingId);
        if (Objects.equals(booking.getItem().getOwner().getId(), userId) || Objects.equals(booking.getBooker().getId(), userId)) {
            return BookingMapper.mapToBookingOutDto(booking);
        }
        throw new AccessRestrictedException("Просматривать бронирование может только его автор, " +
                "либо владелец вещи, к которой относится это бронирование");

    }

    @Override
    public List<BookingOutDto> findAllBookingsByState(Long userId, State state) {
        //Проверка существования пользователя
        getUser(userId);
        List<Booking> bookings;
        switch (state) {
            case ALL:
                bookings = bookingRepository.findByBooker_Id(userId, Sort.by("start").descending());
                break;
            case CURRENT:
                bookings = bookingRepository.findByCurrentBooker(userId, LocalDateTime.now(), Sort.by("id").ascending());
                break;
            case PAST:
                bookings = bookingRepository.findByBooker_IdAndEndIsBefore(userId, LocalDateTime.now(), Sort.by("start").descending());
                break;
            case FUTURE:
                bookings = bookingRepository.findByBooker_IdAndStartIsAfter(userId, LocalDateTime.now(), Sort.by("start").descending());
                break;
            default:
                bookings = bookingRepository.findByBooker_IdAndStatus(userId, Status.valueOf(state.toString()), Sort.by("start").descending());
        }
        return bookings.stream()
                .map(BookingMapper::mapToBookingOutDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingOutDto> findAllOwnerBookingsByState(Long ownerId, State state) {
        //Проверка существования пользователя
        getUser(ownerId);
        List<Booking> bookings;
        switch (state) {
            case ALL:
                bookings = bookingRepository.findByItemOwnerId(ownerId, Sort.by("start").descending());
                break;
            case CURRENT:
                bookings = bookingRepository.findByOwnerCurrentBooker(ownerId, LocalDateTime.now(), Sort.by("id").ascending());
                break;
            case PAST:
                bookings = bookingRepository.findByItemOwnerIdAndEndIsBefore(ownerId, LocalDateTime.now(), Sort.by("start").descending());
                break;
            case FUTURE:
                bookings = bookingRepository.findByItemOwnerIdAndStartIsAfter(ownerId, LocalDateTime.now(), Sort.by("start").descending());
                break;
            default:
                bookings = bookingRepository.findByItemOwnerIdAndStatus(ownerId, Status.valueOf(state.toString()), Sort.by("start").descending());
        }
        return bookings.stream()
                .map(BookingMapper::mapToBookingOutDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingOutDto> findAllBookingsOfItem(Long itemId) {
        return bookingRepository.findByItemId(itemId, Sort.by("start").descending()).stream()
                .map(BookingMapper::mapToBookingOutDto)
                .collect(Collectors.toList());
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ContentNotFountException("Пользователя с id = " + userId + " не существует"));
    }

    private Item getItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ContentNotFountException("Вещи с id = " + itemId + " не существует"));
    }

    private Booking getBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ContentNotFountException("Бранирования с id = " + bookingId + " не существует"));
    }
}
