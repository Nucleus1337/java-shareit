package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Map;

import static ru.practicum.shareit.utils.UtilsClass.getRestTemplate;

@Service
public class ItemRequestClient extends BaseClient {
  private static final String API_PREFIX = "/requests";

  public ItemRequestClient(
      @Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
    super(getRestTemplate(serverUrl, API_PREFIX, builder));
  }

  public ResponseEntity<Object> addRequest(Long userId, ItemRequestDto itemRequestDto) {
    return post("", userId, itemRequestDto);
  }

  public ResponseEntity<Object> findAllByOwnerRequestId(Long userId) {
    return get("", userId);
  }

  public ResponseEntity<Object> findAll(Long userId, Integer from, Integer size) {
    Map<String, Object> parameters = Map.of("from", from, "size", size);
    return get("/all?from={from}&size={size}", userId, parameters);
  }

  public ResponseEntity<Object> findByRequestId(Long userId, Long requestId) {
    return get("/" + requestId, userId);
  }
}
