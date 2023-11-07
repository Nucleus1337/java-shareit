package ru.practicum.shareit.item;

import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemDto;

/** TODO Sprint add-controllers. */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
  private final ItemService itemService;

  @PostMapping
  public ItemDto create(
      @RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody @Valid ItemDto itemDto) {
    return itemService.create(userId, itemDto);
  }

  @PatchMapping("/{id}")
  public ItemDto updateFields(
      @RequestHeader("X-Sharer-User-Id") Long userId,
      @PathVariable Long id,
      @RequestBody Map<String, Object> fields) {
    return itemService.updateFields(userId, id, fields);
  }

  @GetMapping("/{id}")
  public ItemDto findItemById(@PathVariable Long id) {
    return itemService.findById(id);
  }

  @GetMapping
  public List<ItemDto> findAllByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
    return itemService.findAllByUserId(userId);
  }

  @GetMapping("/search")
  public List<ItemDto> search(@RequestParam String text) {
    return itemService.search(text);
  }
}
