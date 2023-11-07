package ru.practicum.shareit.item.storage;

import java.util.List;
import ru.practicum.shareit.item.model.Item;

public interface ItemStorage {
  Item insert(Item item);

  Item update(Item item);

  void delete(Long id);

  Item findById(Long id);

  List<Item> findAll();
}
