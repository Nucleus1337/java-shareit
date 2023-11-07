package ru.practicum.shareit.user;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class UserController {
  private final UserService userService;

  @PostMapping
  @Validated({Group.OnInsert.class})
  public UserDto create(@Valid @RequestBody UserDto userDto) {
    return userService.create(userDto);
  }

  @PatchMapping("/{id}")
  public UserDto update(@Valid @RequestBody UserDto userDto, @PathVariable Long id) {
    return userService.update(userDto, id);
  }

  @GetMapping("/{id}")
  public UserDto findById(@PathVariable Long id) {
    return userService.findById(id);
  }

  @DeleteMapping("/{id}")
  public void removeById(@PathVariable Long id) {
    userService.removeById(id);
  }

  @GetMapping
  public List<UserDto> findAll() {
    return userService.findAll();
  }
}
