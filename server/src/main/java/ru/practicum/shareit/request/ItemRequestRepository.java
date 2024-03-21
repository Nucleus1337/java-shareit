package ru.practicum.shareit.request;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
  List<ItemRequest> findByRequester(User requester);

  List<ItemRequest> findAllByRequesterNot(User requester, Pageable pageable);

  List<ItemRequest> findAllByRequesterNot(User requester);
}
