package ru.practicum.shareit.booking;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.bookingStatus.BookingStatus;
import ru.practicum.shareit.exception.CustomException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
  @Mock private BookingRepository bookingRepository;
  @Mock private UserRepository userRepository;
  @Mock private ItemRepository itemRepository;

  @InjectMocks private BookingService bookingService;

  private final LocalDateTime now = LocalDateTime.now();
  private final LocalDateTime start = LocalDateTime.of(2025, 1, 1, 1, 1, 1);
  private final LocalDateTime end = LocalDateTime.of(2025, 1, 1, 2, 1, 1);

  private BookingResponseDto bookingResponseDto;

  private User user;
  private User otherUser;
  private ItemRequest itemRequest;
  private Item item;
  private Booking booking;
  private BookingRequestDto bookingRequestDto;

  @BeforeEach
  public void setUp() {
    user = User.builder().id(1L).name("Timmy").email("timmy@email.com").build();
    otherUser = User.builder().id(2L).name("Gimmy").email("gimmy@email.com").build();
    itemRequest =
        ItemRequest.builder()
            .id(1L)
            .description("Item request description")
            .requester(user)
            .created(now)
            .build();
    item =
        Item.builder()
            .id(1L)
            .name("Item name")
            .description("Item description")
            .available(true)
            .owner(user)
            .build();
    booking =
        Booking.builder()
            .id(1L)
            .booker(user)
            .status(BookingStatus.WAITING)
            .item(item)
            .start(start)
            .end(end)
            .build();
    bookingRequestDto = BookingRequestDto.builder().itemId(1L).start(start).end(end).build();
  }

  @Test
  public void createShouldReturnBookingResponseDto() {
    when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
    when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(otherUser));
    when(bookingRepository.saveAndFlush(any(Booking.class))).thenReturn(booking);

    long userId = 2L;

    BookingResponseDto createdBookingResponseDto = bookingService.create(userId, bookingRequestDto);

    assertThat(createdBookingResponseDto).isNotNull();
    assertThat(createdBookingResponseDto.getId()).isEqualTo(1);
    assertThat(createdBookingResponseDto.getStart()).isEqualTo(start);
  }

  @Test
  public void createShouldReturnItemNotFoundExceptionWrongBooking() {
    when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));

    long userId = 1L;

    assertThatExceptionOfType(CustomException.ItemNotFoundException.class)
        .isThrownBy(() -> bookingService.create(userId, bookingRequestDto));
  }

  @Test
  public void createShouldReturnItemNotFoundException() {
    when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

    long userId = 1L;

    assertThatExceptionOfType(CustomException.ItemNotFoundException.class)
        .isThrownBy(() -> bookingService.create(userId, bookingRequestDto));
  }

  @Test
  public void createShouldReturnItemNotAvailableException() {
    when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));

    item.setAvailable(false);

    long userId = 1L;

    assertThatExceptionOfType(CustomException.ItemNotAvailableException.class)
        .isThrownBy(() -> bookingService.create(userId, bookingRequestDto));
  }

  @Test
  public void createShouldReturnUserNotFoundException() {
    when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
    when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

    long userId = 2L;

    assertThatExceptionOfType(CustomException.UserNotFoundException.class)
        .isThrownBy(() -> bookingService.create(userId, bookingRequestDto));
  }

  @Test
  public void approveBookingShouldReturnBookingNotFoundException() {
    when(bookingRepository.findBookingByIdAndOwnerId(anyLong(), anyLong()))
        .thenReturn(Optional.empty());

    long bookingId = 1;
    long userId = 1;
    boolean status = true;

    assertThatExceptionOfType(CustomException.BookingNotFoundException.class)
        .isThrownBy(() -> bookingService.approveBooking(bookingId, status, userId));
  }

  @Test
  public void approveBookingShouldReturnBookingStatusException() {
    when(bookingRepository.findBookingByIdAndOwnerId(anyLong(), anyLong()))
        .thenReturn(Optional.ofNullable(booking));

    long bookingId = 1;
    long userId = 1;
    boolean status = true;

    booking.setStatus(BookingStatus.APPROVED);

    assertThatExceptionOfType(CustomException.BookingStatusException.class)
        .isThrownBy(() -> bookingService.approveBooking(bookingId, status, userId));
  }

  @Test
  public void approveBookingShouldReturnBookingResponseDto() {
    when(bookingRepository.findBookingByIdAndOwnerId(anyLong(), anyLong()))
        .thenReturn(Optional.ofNullable(booking));
    when(bookingRepository.saveAndFlush(any(Booking.class))).thenReturn(booking);

    long bookingId = 1;
    long userId = 1;
    boolean status = true;

    BookingResponseDto approvedBookingResponseDto =
        bookingService.approveBooking(bookingId, status, userId);

    assertThat(approvedBookingResponseDto).isNotNull();
    assertThat(approvedBookingResponseDto.getStatus()).isEqualTo(BookingStatus.APPROVED.toString());
  }

  @Test
  public void getBookingByIdForOwnerOrBookerShouldReturnBookingNotFoundException() {
    when(bookingRepository.findBookingByIdAndOwnerIdOrBookerId(anyLong(), anyLong()))
        .thenReturn(Optional.empty());

    long bookingId = 1;
    long userId = 1;

    assertThatExceptionOfType(CustomException.BookingNotFoundException.class)
        .isThrownBy(() -> bookingService.getBookingByIdForOwnerOrBooker(bookingId, userId));
  }

  @Test
  public void getBookingByIdForOwnerOrBookerShouldReturnBookingResponseDto() {
    when(bookingRepository.findBookingByIdAndOwnerIdOrBookerId(anyLong(), anyLong()))
        .thenReturn(Optional.ofNullable(booking));
    when(bookingRepository.saveAndFlush(any(Booking.class))).thenReturn(booking);

    long bookingId = 1;
    long userId = 1;

    BookingResponseDto foundBookingResponseDto =
        bookingService.getBookingByIdForOwnerOrBooker(bookingId, userId);

    assertThat(foundBookingResponseDto).isNotNull();
    assertThat(foundBookingResponseDto.getId()).isEqualTo(bookingId);
    assertThat(foundBookingResponseDto.getBooker().getId()).isEqualTo(userId);
  }

  @Test
  public void getAllBookingsForOwnerOrBookerShouldReturnBookingStateException() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));

    long userId = 1;
    String bookingState = "UNKNOWN";
    String userType = "OWNER";
    int from = 1;
    int size = 1;

    assertThatExceptionOfType(CustomException.BookingStateException.class)
        .isThrownBy(
            () ->
                bookingService.getAllBookingsForOwnerOrBooker(
                    userId, bookingState, userType, from, size));
  }

  @Test
  public void getAllBookingsForOwnerOrBookerShouldReturnListOfResponseDto() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
    when(bookingRepository.findAllByOwnerId(anyLong(), any(Pageable.class)))
        .thenReturn(Collections.singletonList(booking));
    when(bookingRepository.findByBookerOrderByStartDesc(any(User.class), any(Pageable.class)))
        .thenReturn(Collections.singletonList(booking));

    long userId = 1;
    String bookingState = "ALL";
    String userType = "OWNER";
    int from = 1;
    int size = 1;

    List<BookingResponseDto> ownerFoundList =
        bookingService.getAllBookingsForOwnerOrBooker(userId, bookingState, userType, from, size);

    assertThat(ownerFoundList.size()).isGreaterThan(0);
    assertThat(ownerFoundList.get(0).getBooker().getId()).isEqualTo(userId);

    userType = "BOOKER";
    List<BookingResponseDto> bookerFoundList =
        bookingService.getAllBookingsForOwnerOrBooker(userId, bookingState, userType, from, size);

    assertThat(bookerFoundList.size()).isGreaterThan(0);
    assertThat(bookerFoundList.get(0).getBooker().getId()).isEqualTo(userId);
  }

  @Test
  public void getAllBookingsForOwnerOrBookerShouldReturnListOfCurrent() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
    when(bookingRepository.findAllByOwnerId(anyLong(), any(Pageable.class)))
        .thenReturn(Collections.singletonList(booking));

    long userId = 1;
    String bookingState = "CURRENT";
    String userType = "OWNER";
    int from = 1;
    int size = 1;

    booking.setStart(LocalDateTime.of(2020, 1, 1, 1, 1, 1));

    List<BookingResponseDto> ownerFoundList =
        bookingService.getAllBookingsForOwnerOrBooker(userId, bookingState, userType, from, size);

    assertThat(ownerFoundList.size()).isGreaterThan(0);
    assertThat(ownerFoundList.get(0).getBooker().getId()).isEqualTo(userId);
  }

  @Test
  public void getAllBookingsForOwnerOrBookerShouldReturnListOfCurrentButWrongStart() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
    when(bookingRepository.findAllByOwnerId(anyLong(), any(Pageable.class)))
            .thenReturn(Collections.singletonList(booking));

    long userId = 1;
    String bookingState = "CURRENT";
    String userType = "OWNER";
    int from = 1;
    int size = 1;

    booking.setStart(LocalDateTime.of(2025, 1, 1, 1, 1, 1));

    List<BookingResponseDto> ownerFoundList =
            bookingService.getAllBookingsForOwnerOrBooker(userId, bookingState, userType, from, size);

    assertThat(ownerFoundList.size()).isEqualTo(0);
  }

  @Test
  public void getAllBookingsForOwnerOrBookerShouldReturnListOfFuture() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
    when(bookingRepository.findAllByOwnerId(anyLong(), any(Pageable.class)))
        .thenReturn(Collections.singletonList(booking));

    long userId = 1;
    String bookingState = "FUTURE";
    String userType = "OWNER";
    int from = 1;
    int size = 1;

    List<BookingResponseDto> ownerFoundList =
        bookingService.getAllBookingsForOwnerOrBooker(userId, bookingState, userType, from, size);

    assertThat(ownerFoundList.size()).isGreaterThan(0);
    assertThat(ownerFoundList.get(0).getBooker().getId()).isEqualTo(userId);
  }

  @Test
  public void getAllBookingsForOwnerOrBookerShouldReturnListOfPast() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
    when(bookingRepository.findAllByOwnerId(anyLong(), any(Pageable.class)))
        .thenReturn(Collections.singletonList(booking));

    long userId = 1;
    String bookingState = "PAST";
    String userType = "OWNER";
    int from = 1;
    int size = 1;

    booking.setStart(LocalDateTime.of(2020, 1, 1, 1, 1));
    booking.setEnd(LocalDateTime.of(2020, 1, 2, 1, 1));

    List<BookingResponseDto> ownerFoundList =
        bookingService.getAllBookingsForOwnerOrBooker(userId, bookingState, userType, from, size);

    assertThat(ownerFoundList.size()).isGreaterThan(0);
    assertThat(ownerFoundList.get(0).getBooker().getId()).isEqualTo(userId);
  }

  @Test
  public void getAllBookingsForOwnerOrBookerShouldReturnListOfWaiting() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
    when(bookingRepository.findAllByOwnerId(anyLong(), any(Pageable.class)))
        .thenReturn(Collections.singletonList(booking));

    long userId = 1;
    String bookingState = "WAITING";
    String userType = "OWNER";
    int from = 1;
    int size = 1;
    ;

    List<BookingResponseDto> ownerFoundList =
        bookingService.getAllBookingsForOwnerOrBooker(userId, bookingState, userType, from, size);

    assertThat(ownerFoundList.size()).isGreaterThan(0);
    assertThat(ownerFoundList.get(0).getBooker().getId()).isEqualTo(userId);
  }

  @Test
  public void getAllBookingsForOwnerOrBookerShouldReturnListOfRejected() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
    when(bookingRepository.findAllByOwnerId(anyLong(), any(Pageable.class)))
        .thenReturn(Collections.singletonList(booking));

    long userId = 1;
    String bookingState = "REJECTED";
    String userType = "OWNER";
    int from = 1;
    int size = 1;

    booking.setStatus(BookingStatus.REJECTED);

    List<BookingResponseDto> ownerFoundList =
        bookingService.getAllBookingsForOwnerOrBooker(userId, bookingState, userType, from, size);

    assertThat(ownerFoundList.size()).isGreaterThan(0);
    assertThat(ownerFoundList.get(0).getBooker().getId()).isEqualTo(userId);
  }

  @Test
  public void getAllBookingsForOwnerOrBookerNoListException() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
    when(bookingRepository.findAllByOwnerId(anyLong(), any(Pageable.class)))
        .thenReturn(Collections.emptyList());

    long userId = 1;
    String bookingState = "REJECTED";
    String userType = "OWNER";
    int from = 1;
    int size = 1;

    booking.setStatus(BookingStatus.REJECTED);

    assertThatExceptionOfType(CustomException.BookingNotFoundException.class)
        .isThrownBy(
            () ->
                bookingService.getAllBookingsForOwnerOrBooker(
                    userId, bookingState, userType, from, size));
  }
}
