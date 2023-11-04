package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

@Component
public class UserMapper {
  public User toModel(UserRequestDto userRequestDto) {
    return new User(null, userRequestDto.getName(), userRequestDto.getEmail());
  }

  public UserResponseDto toResponseDto(User user) {
    return new UserResponseDto(user.getId(), user.getName(), user.getEmail());
  }
}
