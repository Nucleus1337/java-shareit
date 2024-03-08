package ru.practicum.shareit.item.model;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

/** TODO Sprint add-controllers. */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EqualsAndHashCode
@Table(name = "items")
public class Item {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @EqualsAndHashCode.Exclude private String name;
  @EqualsAndHashCode.Exclude private String description;
  @EqualsAndHashCode.Exclude private Boolean available;

  @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_id")
  @EqualsAndHashCode.Exclude
  private User owner;

  @ManyToOne(targetEntity = ItemRequest.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "request_id")
  @EqualsAndHashCode.Exclude
  private ItemRequest request;

  //  @Override
  //  public boolean equals(Object o) {
  //    if (this == o) return true;
  //    if (o == null || getClass() != o.getClass()) return false;
  //    Item item = (Item) o;
  //    return Objects.equals(id, item.id);
  //  }
  //
  //  @Override
  //  public int hashCode() {
  //    return Objects.hash(id);
  //  }
}
