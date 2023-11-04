package ru.practicum.shareit.item.storage;

import java.util.List;
import ru.practicum.shareit.item.model.Item;

public interface ItemStorage {
  Item insert(Item item);

  Item update(Item item);

  void delete(Integer id);

  Item findById(Integer id);

  List<Item> findAll();
}
