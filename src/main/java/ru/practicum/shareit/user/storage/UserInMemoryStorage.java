package ru.practicum.shareit.user.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

@Component
public class UserInMemoryStorage implements UserStorage {
  private final Map<Long, User> idToUser = new HashMap<>();
  private Long id = 1L;

  private Long getNextId() {
    return id++;
  }

  @Override
  public User insert(User user) {
    user.setId(getNextId());
    idToUser.put(user.getId(), user);
    return user;
  }

  @Override
  public User update(User user) {
    return idToUser.put(user.getId(), user);
  }

  @Override
  public void delete(Long id) {
    idToUser.remove(id);
  }

  @Override
  public User findById(Long id) {
    return idToUser.get(id);
  }

  @Override
  public List<User> findAll() {
    return new ArrayList<>(idToUser.values());
  }
}
