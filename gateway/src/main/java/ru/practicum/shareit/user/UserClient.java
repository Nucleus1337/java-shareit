package ru.practicum.shareit.user;

import static ru.practicum.shareit.utils.UtilsClass.getRestTemplate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDto;

@Service
public class UserClient extends BaseClient {
  private static final String API_PREFIX = "/users";

  public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
    super(getRestTemplate(serverUrl, API_PREFIX, builder));
  }

  public ResponseEntity<Object> create(UserDto userDto) {
    return post("", userDto);
  }

  public ResponseEntity<Object> update(UserDto userDto, Long id) {
    return patch("/" + id, userDto);
  }

  public ResponseEntity<Object> findById(Long id) {
    return get("/" + id);
  }

  public ResponseEntity<Object> removeById(Long id) {
    return delete("/" + id);
  }

  public ResponseEntity<Object> findAll() {
    return get("");
  }
}
