package ru.practicum.shareit.booking;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.bookingState.BookingState;
import ru.practicum.shareit.booking.bookingStatus.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.exception.CustomException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {
  private final BookingRepository bookingRepository;
  private final UserRepository userRepository;
  private final ItemRepository itemRepository;

  @Transactional
  public BookingResponseDto create(Long userId, BookingRequestDto bookingRequestDto) {
    log.info(
        "Создаем новое бронирование. itemId = {}, userId = {}",
        bookingRequestDto.getItemId(),
        userId);

    Item item =
        itemRepository
            .findById(bookingRequestDto.getItemId())
            .orElseThrow(() -> new CustomException.ItemNotFoundException("Вещь не найдена"));

    if (!item.getAvailable()) {
      throw new CustomException.ItemNotAvailableException("Вещь не доступна для бронирования");
    }

    if (item.getOwner().getId().equals(userId)) {
      throw new CustomException.ItemNotFoundException("Владелец не может бронировать свои вещи");
    }

    User user = getUser(userId);

    Booking booking = BookingMapper.toModel(bookingRequestDto, item, user, BookingStatus.WAITING);

    return BookingMapper.toResponseDto(bookingRepository.saveAndFlush(booking));
  }

  private User getUser(Long userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new CustomException.UserNotFoundException("Пользователь не найден"));
  }

  @Transactional
  public BookingResponseDto approveBooking(Long bookingId, Boolean approved, Long ownerId) {
    Booking booking =
        bookingRepository
            .findBookingByIdAndOwnerId(ownerId, bookingId)
            .orElseThrow(() -> new CustomException.BookingNotFoundException("Запросы не найдены"));

    if (booking.getStatus() == BookingStatus.APPROVED && approved) {
      throw new CustomException.BookingStatusException("Статус уже APPROVED");
    }

    booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);

    return BookingMapper.toResponseDto(bookingRepository.saveAndFlush(booking));
  }

  @Transactional
  public BookingResponseDto getBookingByIdForOwnerOrBooker(Long bookingId, Long userId) {
    Booking booking =
        bookingRepository
            .findBookingByIdAndOwnerIdOrBookerId(bookingId, userId)
            .orElseThrow(() -> new CustomException.BookingNotFoundException("Запросы не найдены"));
    return BookingMapper.toResponseDto(bookingRepository.saveAndFlush(booking));
  }

  private List<BookingResponseDto> getList(List<Booking> bookings, BookingState state) {
    if (bookings.isEmpty()) {
      throw new CustomException.BookingNotFoundException("Запросов не найдено");
    }

    List<BookingResponseDto> bookingResponseDtos = new ArrayList<>();

    bookings.forEach(
            booking -> {
              LocalDateTime now = LocalDateTime.now();
              LocalDateTime start = booking.getStart();
              LocalDateTime end = booking.getEnd();
              BookingStatus status = booking.getStatus();

              if (state == BookingState.ALL
                      || state == BookingState.CURRENT && now.isAfter(start) && now.isBefore(end)
                      || state == BookingState.FUTURE && now.isBefore(start)
                      || state == BookingState.PAST && now.isAfter(end)
                      || state == BookingState.WAITING && status.equals(BookingStatus.WAITING)
                      || state == BookingState.REJECTED && status.equals(BookingStatus.REJECTED)) {
                bookingResponseDtos.add(BookingMapper.toResponseDto(booking));
              }
            });

    Comparator<BookingResponseDto> comparator =
            Comparator.comparing(BookingResponseDto::getEnd, Comparator.reverseOrder());
    bookingResponseDtos.sort(comparator);

    return bookingResponseDtos;
  }

  public List<BookingResponseDto> getAllBookingsForBooker(Long userId, BookingState bookingState, Pageable pageable) {
    User user = getUser(userId);
    List<Booking> bookings = bookingRepository.findByBookerOrderByStartDesc(user, pageable);

    return getList(bookings, bookingState);
  }

  public List<BookingResponseDto> getAllBookingsForOwner(Long userId, BookingState bookingState, Pageable pageable) {
    List<Booking> bookings = bookingRepository.findAllByOwnerId(userId, pageable);

    return getList(bookings, bookingState);
  }
}
