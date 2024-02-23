package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
  @Query(
      "SELECT i from Item i "
          + "WHERE (upper(i.name) like upper(concat('%', ?1, '%')) "
          + "OR upper(i.description) like upper(concat('%', ?1, '%'))) "
          + "AND i.available = true")
  List<Item> search(String text);

  @Query(
      nativeQuery = true,
      value =
          "select i.* "
              + "from items i, "
              + "     bookings b "
              + "where i.id = b.item_id "
              + "and b.booker_id = ?1 "
              + "and b.item_id = ?2 "
              + "and b.end_date < now() "
              + "and b.status = 'APPROVED' "
              + "limit 1")
  Optional<Item> findByIdAndBookerIdAndFinishedBooking(Long bookerId, Long itemId);
}
