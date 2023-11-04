package ru.practicum.shareit.user.storage;

import java.util.List;
import ru.practicum.shareit.user.User;

public interface UserStorage {
  User insert(User user);

  User update(User user);

  void delete(Integer id);

  User findById(Integer id);

  List<User> findAll();
}
