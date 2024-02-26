package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.annotation.ValidDates;
import ru.practicum.shareit.groups.Group;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/** TODO Sprint add-bookings. */
@Data
@Builder
@AllArgsConstructor
@ValidDates
public class BookingRequestDto {
  @NotNull
  @FutureOrPresent(message = "Дата и время начала в прошлом")
  private LocalDateTime start;

  @NotNull
  @FutureOrPresent(message = "Дата и время окончания в прошлом")
  private LocalDateTime end;

  @NotNull(groups = Group.OnInsert.class)
  private Long itemId;
}
