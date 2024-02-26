package ru.practicum.shareit.user;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

/** TODO Sprint add-controllers. */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
  private final UserService userService;

  @PostMapping
  @Validated({Group.OnInsert.class})
  public UserDto create(@Valid @RequestBody UserDto userDto) {
    log.info("POST /users: userDto={}", userDto);
    return userService.create(userDto);
  }

  @PatchMapping("/{id}")
  public UserDto update(@Valid @RequestBody UserDto userDto, @PathVariable Long id) {
    log.info("PATCH /users/{id}: id={}, userDto={}", id, userDto);

    return userService.update(userDto, id);
  }

  @GetMapping("/{id}")
  public UserDto findById(@PathVariable Long id) {
    log.info("GET /users/{id}: id={}", id);

    return userService.findById(id);
  }

  @DeleteMapping("/{id}")
  public void removeById(@PathVariable Long id) {
    log.info("DELETE /users/{id}: id={}", id);
    userService.removeById(id);
  }

  @GetMapping
  public List<UserDto> findAll() {
    log.info("GET /users");
    return userService.findAll();
  }
}
