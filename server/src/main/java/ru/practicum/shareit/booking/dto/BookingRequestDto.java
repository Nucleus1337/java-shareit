package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/** TODO Sprint add-bookings. */
@Data
@Builder
@AllArgsConstructor
public class BookingRequestDto {
  private LocalDateTime start;

  private LocalDateTime end;

  private Long itemId;
}
