package ru.practicum.shareit.user;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.dto.UserDto;

@UtilityClass
public class UserMapper {
  public static User toModel(UserDto userRequestDto) {
    return User.builder().name(userRequestDto.getName()).email(userRequestDto.getEmail()).build();
  }

  public static UserDto toDto(User user) {
    return UserDto.builder().id(user.getId()).name(user.getName()).email(user.getEmail()).build();
  }
}
