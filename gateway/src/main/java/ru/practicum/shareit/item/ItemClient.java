package ru.practicum.shareit.item;

import static ru.practicum.shareit.utils.UtilsClass.getRestTemplate;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Service
public class ItemClient extends BaseClient {
  private static final String API_PREFIX = "/items";

  public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
    super(getRestTemplate(serverUrl, API_PREFIX, builder));
  }

  public ResponseEntity<Object> create(Long userId, ItemDto itemDto) {
    return post("", userId, itemDto);
  }

  public ResponseEntity<Object> updateFields(Long userId, Long itemId, Map<String, Object> fields) {
    return patch("/" + itemId, userId, fields);
  }

  public ResponseEntity<Object> findById(Long itemId, Long userId) {
    return get("/" + itemId, userId);
  }

  public ResponseEntity<Object> findAllByUserId(Long userId, Integer from, Integer size) {
    Map<String, Object> parameters = Map.of("from", from, "size", size);

    return get("?from={from}&size={size}", userId, parameters);
  }

  public ResponseEntity<Object> search(String text, Integer from, Integer size) {
    Map<String, Object> patameters = Map.of("text", text, "from", from, "size", size);

    return get("/search?text={text}&from={from}&size={size}", null, patameters);
  }

  public ResponseEntity<Object> addComment(
      Long itemId, Long userId, CommentRequestDto commentRequestDto) {
    return post(String.format("/%s/comment", itemId), userId, commentRequestDto);
  }
}
