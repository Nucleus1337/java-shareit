package ru.practicum.shareit.booking;

import java.time.LocalDateTime;
import lombok.Data;
import ru.practicum.shareit.bookingStatus.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

/** TODO Sprint add-bookings. */
@Data
public class Booking {
  private long id;
  private LocalDateTime start;
  private LocalDateTime end;
  private Item item;
  private User booker;
  // TODO: 29.10.2023 replace String to ENUM
  private BookingStatus status;
}
