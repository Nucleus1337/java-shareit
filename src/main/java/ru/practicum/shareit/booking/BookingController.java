package ru.practicum.shareit.booking;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.groups.Group;

/** TODO Sprint add-bookings. */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
  private static final String USER_ID_HEADER = "X-Sharer-User-Id";
  private final BookingService bookingService;

  @PostMapping
  @Validated({Group.OnInsert.class})
  public BookingResponseDto create(
      @RequestHeader(USER_ID_HEADER) Long userId,
      @RequestBody @Valid BookingRequestDto bookingRequestDto) {
    log.info("POST /bookings: userId={}, bookingRequestDto={}", userId, bookingRequestDto);
    return bookingService.create(userId, bookingRequestDto);
  }

  @PatchMapping(path = "/{bookingId}")
  public BookingResponseDto approveBooking(
      @PathVariable Long bookingId,
      @RequestParam Boolean approved,
      @RequestHeader(USER_ID_HEADER) Long ownerId) {
    log.info(
        "PATCH /bookings/{bookingId}: bookingId={}, approved={}, ownerId={}",
        bookingId,
        approved,
        ownerId);
    return bookingService.approveBooking(bookingId, approved, ownerId);
  }

  @GetMapping(path = "/{bookingId}")
  public BookingResponseDto getBookingByIdForOwnerOrBooker(
      @PathVariable Long bookingId, @RequestHeader(USER_ID_HEADER) Long userId) {
    log.info("GET /bookings/{bookingId}: bookingId={}, userId={}", bookingId, userId);
    return bookingService.getBookingByIdForOwnerOrBooker(bookingId, userId);
  }

  @GetMapping
  public List<BookingResponseDto> getAllBookingsForBooker(
      @RequestHeader(USER_ID_HEADER) Long userId,
      @RequestParam(required = false, defaultValue = "ALL") String state) {
    log.info("GET /bookings: userId={}, state={}", userId, state);
    return bookingService.getAllBookingsForOwnerOrBooker(userId, state, "BOOKER");
  }

  @GetMapping(path = "/owner")
  public List<BookingResponseDto> getAllBookingsForOwner(
      @RequestHeader(USER_ID_HEADER) Long userId,
      @RequestParam(required = false, defaultValue = "ALL") String state) {
    log.info("GET /bookings/owner: userId={}, state={}", userId, state);
    return bookingService.getAllBookingsForOwnerOrBooker(userId, state, "OWNER");
  }
}
