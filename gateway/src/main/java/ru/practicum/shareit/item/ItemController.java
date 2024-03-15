package ru.practicum.shareit.item;

import java.util.Collections;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import ru.practicum.shareit.item.dto.ItemDto;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
  private static final String USER_ID_HEADER = "X-Sharer-User-Id";
  private final ItemClient itemClient;

  @PostMapping
  public ResponseEntity<Object> create(
      @RequestHeader(USER_ID_HEADER) Long userId, @RequestBody @Valid ItemDto itemDto) {
    log.info("POST /items: userId={}, itemDto={}", userId, itemDto);
    return itemClient.create(userId, itemDto);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Object> updateFields(
      @RequestHeader(USER_ID_HEADER) Long userId,
      @PathVariable Long id,
      @RequestBody Map<String, Object> fields) {
    log.info("PATCH /items/{id}: userId={}, id={}, fields={}", userId, id, fields);
    return itemClient.updateFields(userId, id, fields);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> findItemById(
      @PathVariable Long id, @RequestHeader(USER_ID_HEADER) Long userId) {
    log.info("GET /items/{id}: id={}, userId={}", id, userId);
    return itemClient.findById(id, userId);
  }

  @GetMapping
  public ResponseEntity<Object> findAllByUserId(
      @RequestHeader(USER_ID_HEADER) Long userId,
      @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
      @RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size) {
    log.info("GET /items: userId={}, from={}, size={}", userId, from, size);
    return itemClient.findAllByUserId(userId, from, size);
  }

  @GetMapping("/search")
  public ResponseEntity<Object> search(
      @RequestParam String text,
      @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
      @RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size) {
    log.info("GET /items/search: test={}, from={}, size={}", text, from, size);

    if (text.isEmpty() || text.isBlank()) {
      log.error("Пустой запрос поиска");
      return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
    }

    return itemClient.search(text, from, size);
  }

  @PostMapping("/{itemId}/comment")
  public ResponseEntity<Object> addComment(
      @PathVariable Long itemId,
      @RequestHeader(USER_ID_HEADER) Long userId,
      @RequestBody @Valid CommentRequestDto commentRequestDto) {
    log.info(
        "POST /items/{itemId}/comment: itemId={}, userId={}, commentRequestDto={}",
        itemId,
        userId,
        commentRequestDto);
    return itemClient.addComment(itemId, userId, commentRequestDto);
  }
}
