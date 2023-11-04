package ru.practicum.shareit.user.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

@Component
public class UserInMemoryStorage implements UserStorage {
  private final Map<Integer, User> idToUser = new HashMap<>();
  private Integer id = 1;

  private Integer getNextId() {
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
  public void delete(Integer id) {
    idToUser.remove(id);
  }

  @Override
  public User findById(Integer id) {
    return idToUser.get(id);
  }

  @Override
  public List<User> findAll() {
    return new ArrayList<>(idToUser.values());
  }
}
