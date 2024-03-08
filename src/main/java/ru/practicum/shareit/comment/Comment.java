package ru.practicum.shareit.comment;

import java.time.LocalDateTime;
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
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EqualsAndHashCode
@Table(name = "comments")
public class Comment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @EqualsAndHashCode.Exclude private String text;

  @ManyToOne(targetEntity = Item.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id")
  @EqualsAndHashCode.Exclude
  private Item item;

  @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id")
  @EqualsAndHashCode.Exclude
  private User author;

  @EqualsAndHashCode.Exclude private LocalDateTime created;

//  @Override
//  public boolean equals(Object o) {
//    if (this == o) return true;
//    if (o == null || getClass() != o.getClass()) return false;
//    Comment comment = (Comment) o;
//    return Objects.equals(id, comment.id);
//  }
//
//  @Override
//  public int hashCode() {
//    return Objects.hash(id);
//  }
}
