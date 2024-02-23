package ru.practicum.shareit.booking;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class BookingController {
  private final BookingService bookingService;

  @PostMapping
  @Validated({Group.OnInsert.class})
  public BookingResponseDto create(
      @RequestHeader("X-Sharer-User-Id") Long userId,
      @RequestBody @Valid BookingRequestDto bookingRequestDto) {
    return bookingService.create(userId, bookingRequestDto);
  }

  @PatchMapping(path = "/{bookingId}")
  public BookingResponseDto approveBooking(
      @PathVariable Long bookingId,
      @RequestParam Boolean approved,
      @RequestHeader("X-Sharer-User-Id") Long ownerId) {
    return bookingService.approveBooking(bookingId, approved, ownerId);
  }

  @GetMapping(path = "/{bookingId}")
  public BookingResponseDto getBookingByIdForOwnerOrBooker(
      @PathVariable Long bookingId, @RequestHeader("X-Sharer-User-Id") Long userId) {
    return bookingService.getBookingByIdForOwnerOrBooker(bookingId, userId);
  }

  @GetMapping
  public List<BookingResponseDto> getAllBookingsForBooker(
      @RequestHeader("X-Sharer-User-Id") Long userId,
      @RequestParam(required = false, defaultValue = "ALL") String state) {
    return bookingService.getAllBookingsForOwnerOrBooker(userId, state, "BOOKER");
  }

  @GetMapping(path = "/owner")
  public List<BookingResponseDto> getAllBookingsForOwner(
      @RequestHeader("X-Sharer-User-Id") Long userId,
      @RequestParam(required = false, defaultValue = "ALL") String state) {
    return bookingService.getAllBookingsForOwnerOrBooker(userId, state, "OWNER");
  }
}
