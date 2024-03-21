package ru.practicum.shareit.item;

import static ru.practicum.shareit.utils.UtilsClass.getPageable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPlusResponseDto;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
  private static final String USER_ID_HEADER = "X-Sharer-User-Id";
  private final ItemService itemService;

  @PostMapping
  public ItemDto create(
      @RequestHeader(USER_ID_HEADER) Long userId, @RequestBody ItemDto itemDto) {
    log.info("POST /items: userId={}, itemDto={}", userId, itemDto);
    return itemService.create(userId, itemDto);
  }

  @PatchMapping("/{id}")
  public ItemDto updateFields(
      @RequestHeader(USER_ID_HEADER) Long userId,
      @PathVariable Long id,
      @RequestBody Map<String, Object> fields) {
    log.info("PATCH /items/{id}: userId={}, id={}, fields={}", userId, id, fields);
    return itemService.updateFields(userId, id, fields);
  }

  @GetMapping("/{id}")
  public ItemPlusResponseDto findItemById(
      @PathVariable Long id, @RequestHeader(USER_ID_HEADER) Long userId) {
    log.info("GET /items/{id}: id={}, userId={}", id, userId);
    return itemService.findById(id, userId);
  }

  @GetMapping
  public List<ItemPlusResponseDto> findAllByUserId(
      @RequestHeader(USER_ID_HEADER) Long userId,
      @RequestParam(name = "from", defaultValue = "0") Integer from,
      @RequestParam(name = "size", defaultValue = "10") Integer size) {
    log.info("GET /items: userId={}, from={}, size={}", userId, from, size);
    Pageable pageable = getPageable(from, size);
    return itemService.findAllByUserId(userId, pageable);
  }

  @GetMapping("/search")
  public List<ItemDto> search(
      @RequestParam String text,
      @RequestParam(name = "from", defaultValue = "0") Integer from,
      @RequestParam(name = "size", defaultValue = "10") Integer size) {
    log.info("GET /items/search: test={}, from={}, size={}", text, from, size);

    if (text.isEmpty() || text.isBlank()) {
      log.error("Пустой запрос поиска");
      return Collections.emptyList();
    }

    Pageable pageable = getPageable(from, size);

    return itemService.search(text, pageable);
  }

  @PostMapping("/{itemId}/comment")
  public CommentResponseDto addComment(
      @PathVariable Long itemId,
      @RequestHeader(USER_ID_HEADER) Long userId,
      @RequestBody CommentRequestDto commentRequestDto) {
    log.info(
        "POST /items/{itemId}/comment: itemId={}, userId={}, commentRequestDto={}",
        itemId,
        userId,
        commentRequestDto);
    return itemService.addComment(itemId, userId, commentRequestDto);
  }
}
