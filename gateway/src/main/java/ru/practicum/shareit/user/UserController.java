package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.groups.Group;
import ru.practicum.shareit.user.dto.UserDto;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
  private final UserClient userClient;

  @PostMapping
  public ResponseEntity<Object> create(
      @Validated({Group.OnInsert.class}) @RequestBody UserDto userDto) {
    log.info("POST /users: userDto={}", userDto);
    return userClient.create(userDto);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Object> update(
      @Validated({Group.OnUpdate.class}) @RequestBody UserDto userDto, @PathVariable Long id) {
    log.info("PATCH /users/{id}: id={}, userDto={}", id, userDto);

    return userClient.update(userDto, id);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> findById(@PathVariable Long id) {
    log.info("GET /users/{id}: id={}", id);

    return userClient.findById(id);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> removeById(@PathVariable Long id) {
    log.info("DELETE /users/{id}: id={}", id);
    return userClient.removeById(id);
  }

  @GetMapping
  public ResponseEntity<Object> findAll() {
    log.info("GET /users");
    return userClient.findAll();
  }
}
