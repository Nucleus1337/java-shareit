package ru.practicum.shareit.item;

import static ru.practicum.shareit.utils.UtilsClass.getPageable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.exception.CustomException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPlusResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {
  private final UserRepository userRepository;
  private final ItemRepository itemRepository;
  private final BookingRepository bookingRepository;
  private final CommentRepository commentRepository;
  private final ItemRequestRepository itemRequestRepository;

  private User findUser(Long userId) {
    log.info("Найдем пользователя с id = {}", userId);
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new CustomException.UserNotFoundException("Пользователь не существует"));
  }

  private Item findItem(Long itemId) {
    log.info("Найдем вещь с id = {}", itemId);
    return itemRepository
        .findById(itemId)
        .orElseThrow(() -> new CustomException.ItemNotFoundException("Вещь не найдена"));
  }

  public ItemDto create(Long userId, ItemDto itemDto) {
    log.info("Создадим новую вещь: {}; для пользователя с id = {}", itemDto, userId);
    User user = findUser(userId);
    Item item;

    if (itemDto.getRequestId() != null) {
      ItemRequest itemRequest = itemRequestRepository.findById(itemDto.getRequestId()).orElse(null);
      item = ItemMapper.toModel(itemDto, user, itemRequest);
    } else {
      item = ItemMapper.toModel(itemDto, user);
    }

    Item itemToSave = itemRepository.saveAndFlush(item);

    return ItemMapper.toDto(itemToSave);
  }

  public ItemDto updateFields(Long userId, Long itemId, Map<String, Object> fields) {
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
            ReflectionUtils.setField(
                field, item, value instanceof Integer ? ((Integer) value).longValue() : value);
          }
        });

    itemRepository.saveAndFlush(item);
    return ItemMapper.toDto(item);
  }

  public ItemPlusResponseDto findById(Long itemId, Long userId) {
    log.info("Найдем вещь с id = {}", itemId);
    Booking lastBooking = bookingRepository.findLastBookingBeforeNow(itemId, userId);
    Booking nextBooking = bookingRepository.findNextBookingAfterNow(itemId, userId);
    List<Comment> comments = commentRepository.findAllByItemId(itemId);
    return ItemMapper.toResponsePlusDto(findItem(itemId), lastBooking, nextBooking, comments);
  }

  public List<ItemPlusResponseDto> findAllByUserId(Long userId, Integer from, Integer size) {
    log.info("Найдем все вещи пользователя с id = {}", userId);
    List<ItemPlusResponseDto> itemsDto = new ArrayList<>();
    User user = findUser(userId);

    Pageable pageable = getPageable(from, size);
    itemRepository
        .findByOwner(user, pageable)
        .forEach(
            item -> {
              Booking next = bookingRepository.findNextBookingAfterNow(item.getId(), userId);
              Booking last = bookingRepository.findLastBookingBeforeNow(item.getId(), userId);
              List<Comment> comments = commentRepository.findAllByItemId(item.getId());
              itemsDto.add(ItemMapper.toResponsePlusDto(item, last, next, comments));
            });

    Comparator<ItemPlusResponseDto> comparator = Comparator.comparing(ItemPlusResponseDto::getId);
    itemsDto.sort(comparator);

    log.info("Всего найдено вещей: {}", itemsDto.size());
    return itemsDto;
  }

  public List<ItemDto> search(String text, Integer from, Integer size) {
    log.info("Найдем все вещи по строке запроса: {}", text);
    if (text.isEmpty() || text.isBlank()) {
      log.error("Пустой запрос поиска");
      return new ArrayList<>();
    }
    Pageable pageable = getPageable(from, size);
    List<ItemDto> itemsDto =
        itemRepository.search(text, pageable).stream()
            .map(ItemMapper::toDto)
            .collect(Collectors.toList());

    log.info("Всего найдено вещей: {}", itemsDto.size());
    return itemsDto;
  }

  public CommentResponseDto addComment(
      Long itemId, Long userId, CommentRequestDto commentRequestDto) {
    Item item =
        itemRepository
            .findByIdAndBookerIdAndFinishedBooking(userId, itemId)
            .orElseThrow(
                () ->
                    new CustomException.ItemNotAvailableException(
                        "Вещь не доступна для комментария"));
    User user = findUser(userId);

    Comment comment = CommentMapper.toModel(commentRequestDto, user, item);
    commentRepository.saveAndFlush(comment);

    log.info("Пользователь id={} добавил комментарий к вещи id={}", userId, itemId);

    return CommentMapper.toResponseDto(comment);
  }
}
