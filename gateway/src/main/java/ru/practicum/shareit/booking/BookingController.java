package ru.practicum.shareit.booking;


import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.groups.Group;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
  private static final String USER_ID_HEADER = "X-Sharer-User-Id";
  private final BookingClient bookingClient;

  @PostMapping
  @Validated({Group.OnInsert.class})
  public ResponseEntity<Object> create(
      @RequestHeader(USER_ID_HEADER) Long userId,
      @RequestBody @Valid BookingRequestDto bookingRequestDto) {
    log.info("POST /bookings: userId={}, bookingRequestDto={}", userId, bookingRequestDto);
    return bookingClient.create(userId, bookingRequestDto);
  }

  @PatchMapping(path = "/{bookingId}")
  public ResponseEntity<Object> approveBooking(
      @PathVariable Long bookingId,
      @RequestParam Boolean approved,
      @RequestHeader(USER_ID_HEADER) Long ownerId) {
    log.info(
        "PATCH /bookings/{bookingId}: bookingId={}, approved={}, ownerId={}",
        bookingId,
        approved,
        ownerId);
    return bookingClient.approveBooking(bookingId, approved, ownerId);
  }

  @GetMapping(path = "/{bookingId}")
  public ResponseEntity<Object> getBookingByIdForOwnerOrBooker(
      @PathVariable Long bookingId, @RequestHeader(USER_ID_HEADER) Long userId) {
    log.info("GET /bookings/{bookingId}: bookingId={}, userId={}", bookingId, userId);
    return bookingClient.getBookingByIdForOwnerOrBooker(bookingId, userId);
  }

  @GetMapping
  public ResponseEntity<Object> getAllBookingsForBooker(
      @RequestHeader(USER_ID_HEADER) Long userId,
      @RequestParam(name = "state", defaultValue = "ALL") String state,
      @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
      @RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size) {
    log.info("GET /bookings: userId={}, state={}, from={}, size={}", userId, state, from, size);

    BookingState bookingState =
            BookingState.from(state)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));

    return bookingClient.getAllBookingsForBooker(userId, bookingState.toString(), from, size);
  }

  @GetMapping(path = "/owner")
  public ResponseEntity<Object> getAllBookingsForOwner(
      @RequestHeader(USER_ID_HEADER) Long userId,
      @RequestParam(name = "state", defaultValue = "ALL") String state,
      @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
      @RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size) {
    log.info(
        "GET /bookings/owner: userId={}, state={}, from={}, size={}", userId, state, from, size);

    BookingState bookingState =
            BookingState.from(state)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));

    return bookingClient.getAllBookingsForOwner(userId, bookingState.toString(), from, size);
  }
}
