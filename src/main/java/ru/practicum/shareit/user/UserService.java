package ru.practicum.shareit.user;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.CustomException;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.storage.UserStorage;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
  private final UserMapper userMapper;
  private final UserStorage userStorage;

  private void checkEmail(String newEmail) {
    log.info("Проверяем строку email: {}", newEmail);
    if (newEmail == null) {
      throw new CustomException.EmailException("Не задан email");
    }

    Set<String> emails =
        userStorage.findAll().stream().map(User::getEmail).collect(Collectors.toSet());

    int oldEmailsSize = emails.size();
    emails.add(newEmail);
    int newEmailsSize = emails.size();

    if (oldEmailsSize == newEmailsSize) {
      throw new CustomException.UserException("Такой email уже занят");
    }
  }

  public UserResponseDto create(UserRequestDto userRequest) {
    log.info("Создаем нового пользователяя {}", userRequest);
    checkEmail(userRequest.getEmail());

    User user = userStorage.insert(userMapper.toModel(userRequest));

    return userMapper.toResponseDto(user);
  }

  public UserResponseDto update(UserRequestDto userRequest, Integer id) {
    log.info("Обновляем пользователя с id = {}", id);
    User user = userStorage.findById(id);
    String name = userRequest.getName();
    String email = userRequest.getEmail();

    if (name != null) {
      user.setName(name);
    }

    if (email != null) {
      if (!email.equals(user.getEmail())) {
        checkEmail(email);
      }
      user.setEmail(email);
    }

    return userMapper.toResponseDto(user);
  }

  public UserResponseDto findById(Integer id) {
    return userMapper.toResponseDto(userStorage.findById(id));
  }

  public void removeById(Integer id) {
    log.info("Удаляем пользователя с id = {}", id);
    userStorage.delete(id);
  }

  public List<UserResponseDto> findAll() {
    return userStorage.findAll().stream()
        .map(userMapper::toResponseDto)
        .collect(Collectors.toList());
  }
}
