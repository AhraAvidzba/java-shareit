package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exceptions.ContentNotFountException;
import ru.practicum.shareit.exceptions.EditingNotAllowedException;
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
    public BookingDto saveBooking(BookingDto bookingDto) {
        Item item = getItem(bookingDto.getItemId());
        User user = getUser(bookingDto.getBookerId());
        return BookingMapper.mapToBookingDto(bookingRepository.save(BookingMapper.mapToBooking(bookingDto, item, user)));
    }

    @Override
    public BookingDto setStatus(Long bookingId, Long userId, Boolean isApproved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ContentNotFountException("Бронирования с id = " + bookingId + " не существует"));
        if (!Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new EditingNotAllowedException("Бранирование может подтвердить только владелец вещи");
        }
        booking.setStatus(isApproved ? Status.APPROVED : Status.REJECTED);
        return BookingMapper.mapToBookingDto(bookingRepository.save(booking));
    }

    public BookingDto getBooking(Long userId, Long bookingId) {
        Booking booking = getBooking(bookingId);
        if (!Objects.equals(booking.getItem().getOwner().getId(), userId) || !Objects.equals(booking.getBooker().getId(), bookingId)) {
            throw new ContentNotFountException("Просматривать бронирование может только его автор, " +
                    "либо владелец вещи, к которой относится это бронирование");
        }
        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public List<BookingDto> findAllBookingsByState(Long userId, State state) {
        List<Booking> bookings;
        switch (state) {
            case ALL:
                bookings = bookingRepository.findByBooker_Id(userId, Sort.by("start").descending());
                break;
            case CURRENT:
                bookings = bookingRepository.findByCurrentBooker(userId, LocalDateTime.now());
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
                .map(BookingMapper::mapToBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> findAllOwnerBookingsByState(Long ownerId, State state) {
        List<Booking> bookings;
        switch (state) {
            case ALL:
                bookings = bookingRepository.findByItemOwnerId(ownerId, Sort.by("start").descending());
                break;
            case CURRENT:
                bookings = bookingRepository.findByOwnerCurrentBooker(ownerId, LocalDateTime.now());
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
                .map(BookingMapper::mapToBookingDto)
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
