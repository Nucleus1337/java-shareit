package ru.practicum.shareit.user;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

/** TODO Sprint add-controllers. */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
  private final UserService userService;

  @PostMapping
  public UserResponseDto create(@Valid @RequestBody UserRequestDto userRequestDto) {
    return userService.create(userRequestDto);
  }

  @PatchMapping("/{id}")
  public UserResponseDto update(
      @Valid @RequestBody UserRequestDto userRequestDto, @PathVariable Integer id) {
    return userService.update(userRequestDto, id);
  }

  @GetMapping("/{id}")
  public UserResponseDto findById(@PathVariable Integer id) {
    return userService.findById(id);
  }

  @DeleteMapping("/{id}")
  public void removeById(@PathVariable Integer id) {
    userService.removeById(id);
  }

  @GetMapping
  public List<UserResponseDto> findAll() {
    return userService.findAll();
  }
}
