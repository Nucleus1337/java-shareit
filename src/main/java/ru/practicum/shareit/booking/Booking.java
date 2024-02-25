package ru.practicum.shareit.booking;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.bookingStatus.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

/** TODO Sprint add-bookings. */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "bookings")
public class Booking {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "start_date")
  private LocalDateTime start;

  @Column(name = "end_date")
  private LocalDateTime end;

  @ManyToOne(targetEntity = Item.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id")
  private Item item;

  @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "booker_id")
  private User booker;

  @Enumerated(EnumType.STRING)
  private BookingStatus status;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Booking booking = (Booking) o;
    return id == booking.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
