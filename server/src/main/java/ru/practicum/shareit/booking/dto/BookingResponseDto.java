package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BookingResponseDto {
  private Long id;
  private LocalDateTime start;
  private LocalDateTime end;
  private String status;
  private Booker booker;
  private Item item;

  @Data
  @AllArgsConstructor
  public static class Booker {
    Long id;
  }

  @Data
  @AllArgsConstructor
  public static class Item {
    Long id;
    String name;
  }
}
