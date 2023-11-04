package ru.practicum.shareit.item;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import ru.practicum.shareit.exception.CustomException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.UserStorage;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {
  private final ItemStorage itemStorage;
  private final ItemMapper itemMapper;
  private final UserStorage userStorage;

  private User findUser(Integer userId) {
    log.info("Найдем пользователя с id = {}", userId);
    User user = userStorage.findById(userId);
    if (user == null) {
      throw new CustomException.UserNotFoundException("Пользователь не найден");
    }
    return user;
  }

  private Item findItem(Integer itemId) {
    log.info("Найдем вещь с id = {}", itemId);
    Item item = itemStorage.findById(itemId);
    if (item == null) {
      throw new CustomException.ItemException("Вещь не найдена");
    }
    return item;
  }

  public ItemDto create(Integer userId, ItemDto itemDto) {
    log.info("Создадим новую вещь: {}; для пользователя с id = {}", itemDto, userId);
    User user = findUser(userId);
    Item item = itemMapper.toModel(itemDto, user);

    return itemMapper.toDto(itemStorage.insert(item));
  }

  public ItemDto updateFields(Integer userId, Integer itemId, Map<String, Object> fields) {
    log.info("Обновим вещь с id = {} у пользователя с id = {}", userId, itemId);
    User user = findUser(userId);
    Item item = findItem(itemId);

    if (!user.equals(item.getOwner())) {
      throw new CustomException.UserNotFoundException(
          String.format("Пользователь %s не являвется владельцем", user.getName()));
    }

    fields.forEach(
        (key, value) -> {
          Field field = ReflectionUtils.findField(Item.class, key);
          if (field != null) {
            field.setAccessible(true);
            ReflectionUtils.setField(field, item, value);
          }
        });

    itemStorage.update(item);
    return itemMapper.toDto(item);
  }

  public ItemDto findById(Integer itemId) {
    log.info("Найдем вещь с id = {}", itemId);
    return itemMapper.toDto(findItem(itemId));
  }

  public List<ItemDto> findAllByUserId(Integer userId) {
    log.info("Найдем все вещи пользователя с id = {}", userId);
    List<ItemDto> itemsDto = new ArrayList<>();
    itemStorage.findAll().stream()
        .filter(item -> item.getOwner().getId().equals(userId))
        .forEach(
            item -> {
              itemsDto.add(itemMapper.toDto(item));
            });

    log.info("Всего найдено вещей: {}", itemsDto.size());
    return itemsDto;
  }

  public List<ItemDto> search(String text) {
    log.info("Найдем все вещи по строке запроса: {}", text);
    if (text.isEmpty() || text.isBlank()) {
      log.error("Пустой запрос поиска");
      return new ArrayList<>();
    }

    List<ItemDto> itemsDto = new ArrayList<>();
    String modifiedText = text.replaceAll(" ", "").toLowerCase();
    itemStorage.findAll().stream()
        .filter(
            item ->
                (item.getName().replaceAll(" ", "").toLowerCase().split(modifiedText).length > 1
                        || item.getDescription()
                                .replaceAll(" ", "")
                                .toLowerCase()
                                .split(modifiedText)
                                .length
                            > 1)
                    && item.getAvailable() == Boolean.TRUE)
        .forEach(
            item -> {
              itemsDto.add(itemMapper.toDto(item));
            });

    log.info("Всего найдено вещей: {}", itemsDto.size());
    return itemsDto;
  }
}
