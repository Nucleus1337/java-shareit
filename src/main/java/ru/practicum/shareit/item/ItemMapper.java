package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

@Component
public class ItemMapper {
  public ItemDto toDto(Item item) {
    return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
  }

  public Item toModel(ItemDto itemDto, User user) {
    return new Item(
        null, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(), user, null);
  }
}
