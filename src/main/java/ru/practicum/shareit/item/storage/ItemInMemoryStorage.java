package ru.practicum.shareit.item.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemInMemoryStorage implements ItemStorage {
  private final Map<Integer, Item> idToItem = new HashMap<>();
  private Integer id = 1;

  private Integer getNextId() {
    return id++;
  }

  @Override
  public Item insert(Item item) {
    item.setId(getNextId());
    idToItem.put(item.getId(), item);
    return item;
  }

  @Override
  public Item update(Item item) {
    return idToItem.put(item.getId(), item);
  }

  @Override
  public void delete(Integer id) {
    idToItem.remove(id);
  }

  @Override
  public Item findById(Integer id) {
    return idToItem.get(id);
  }

  @Override
  public List<Item> findAll() {
    return new ArrayList<>(idToItem.values());
  }
}
