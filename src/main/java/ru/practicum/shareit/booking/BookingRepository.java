package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
  @Query("select b from Booking b join b.item i where i.owner.id = ?1 and b.id = ?2")
  Optional<Booking> findBookingByIdAndOwnerId(Long ownerId, Long bookingId);

  @Query(
      "select b "
          + "from Booking b join b.item i "
          + "where b.id = ?1 "
          + "and (i.owner.id = ?2 or b.booker.id = ?2)")
  Optional<Booking> findBookingByIdAndOwnerIdOrBookerId(Long bookingId, Long userId);

  @Query("select b from Booking b join fetch b.item i where i.owner.id = ?1 order by b.start desc")
  List<Booking> findAllByOwnerId(Long userId, Pageable pageable);

  @Query("select b from Booking b where b.booker.id = ?1 order by b.start desc")
  List<Booking> findAllByBookerId(Long userId, Pageable pageable);

  List<Booking> findByBookerOrderByStartDesc(User booker, Pageable pageable);

  @Query(
      nativeQuery = true,
      value =
          "select b.* "
              + "from bookings b "
              + "right join items i on i.id = b.item_id "
              + "                   and i.owner_id = ?2 "
              + "                   and i.id = ?1 "
              + "where b.status = 'APPROVED' "
              + "and b.start_date < now() "
              + "order by b.end_date desc "
              + "limit 1")
  Booking findLastBookingBeforeNow(Long itemId, Long userId);

  @Query(
      nativeQuery = true,
      value =
          "select b.* "
              + "from bookings b "
              + "right join items i on i.id = b.item_id "
              + "                   and i.owner_id = ?2 "
              + "                   and i.id = ?1 "
              + "where b.status = 'APPROVED' "
              + "and b.start_date > now() "
              + "order by b.start_date "
              + "limit 1")
  Booking findNextBookingAfterNow(Long itemId, Long userId);
}
