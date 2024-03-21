package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.annotation.ValidDates;
import ru.practicum.shareit.groups.Group;

@Data
@Builder
@AllArgsConstructor
@ValidDates
public class BookingRequestDto {
  @NotNull
  @FutureOrPresent(message = "Дата и время начала в прошлом")
  private LocalDateTime start;

  @NotNull
  @Future(message = "Дата и время окончания в прошлом")
  private LocalDateTime end;

  @NotNull(groups = Group.OnInsert.class)
  private Long itemId;
}
