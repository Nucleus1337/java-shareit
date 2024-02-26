package ru.practicum.shareit.item;

import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

/** TODO Sprint add-controllers. */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
  private static final String USER_ID_HEADER = "X-Sharer-User-Id";
  private final ItemService itemService;

  @PostMapping
  public ItemDto create(
      @RequestHeader(USER_ID_HEADER) Long userId, @RequestBody @Valid ItemDto itemDto) {
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
  public List<ItemPlusResponseDto> findAllByUserId(@RequestHeader(USER_ID_HEADER) Long userId) {
    log.info("GET /items: userId={}", userId);
    return itemService.findAllByUserId(userId);
  }

  @GetMapping("/search")
  public List<ItemDto> search(@RequestParam String text) {
    log.info("GET /items/search: test={}", text);
    return itemService.search(text);
  }

  @PostMapping("/{itemId}/comment")
  public CommentResponseDto addComment(
      @PathVariable Long itemId,
      @RequestHeader(USER_ID_HEADER) Long userId,
      @RequestBody @Valid CommentRequestDto commentRequestDto) {
    log.info(
        "POST /items/{itemId}/comment: itemId={}, userId={}, commentRequestDto={}",
        itemId,
        userId,
        commentRequestDto);
    return itemService.addComment(itemId, userId, commentRequestDto);
  }
}
