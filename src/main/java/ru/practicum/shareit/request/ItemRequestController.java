package ru.practicum.shareit.request;

import static ru.practicum.shareit.utils.UtilsClass.getPageable;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;

/** TODO Sprint add-item-requests. */
@RestController
@AllArgsConstructor
@Slf4j
@Validated
@RequestMapping(path = "/requests")
public class ItemRequestController {
  private static final String USER_ID_HEADER = "X-Sharer-User-Id";
  private final Sort newIsFirst = Sort.by("created");
  private final ItemRequestService itemRequestService;

  @PostMapping
  public ItemRequestDto addRequest(
      @RequestHeader(USER_ID_HEADER) Long userId,
      @RequestBody @Valid ItemRequestDto itemRequestDto) {
    log.info("POST /requests: userId={}, itemRequestDto={}", userId, itemRequestDto);
    return itemRequestService.addRequest(itemRequestDto, userId);
  }

  @GetMapping
  public List<ItemRequestDto> findAllByOwnerRequestId(@RequestHeader(USER_ID_HEADER) Long userId) {
    log.info("GET /requests: userId={}", userId);
    return itemRequestService.findAllByOwnerRequestId(userId);
  }

  @GetMapping("/all")
  public List<ItemRequestDto> findAll(
      @RequestHeader(USER_ID_HEADER) Long userId,
      @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
      @RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size) {
    log.info("GET /requests/all: from={}, size={}", from, size);

    Pageable pageable = getPageable(from, size, newIsFirst);

    return itemRequestService.findAll(userId, pageable);
  }

  @GetMapping("/{requestId}")
  public ItemRequestDto findByRequestId(
      @RequestHeader(USER_ID_HEADER) Long userId, @PathVariable Long requestId) {
    log.info("GET /requests/{requestId}: userId={}, requestId={}", userId, requestId);
    return itemRequestService.findByRequestId(userId, requestId);
  }
}
