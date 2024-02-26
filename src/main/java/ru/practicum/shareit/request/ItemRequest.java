package ru.practicum.shareit.request;

import java.time.LocalDateTime;
import lombok.Data;
import ru.practicum.shareit.user.User;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/** TODO Sprint add-item-requests. */
@Data
@Entity
@Table(name = "requests")
public class ItemRequest {
  @Id private long id;
  private String description;

  @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "requester_id")
  private User requester;

  private LocalDateTime created;
}
