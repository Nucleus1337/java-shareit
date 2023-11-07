package ru.practicum.shareit.user;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.CustomException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.storage.UserStorage;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
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

  public UserDto create(UserDto userDto) {
    log.info("Создаем нового пользователяя {}", userDto);
    checkEmail(userDto.getEmail());

    User user = userStorage.insert(UserMapper.toModel(userDto));

    return UserMapper.toDto(user);
  }

  public UserDto update(UserDto userRequest, Long id) {
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

    return UserMapper.toDto(user);
  }

  public UserDto findById(Long id) {
    return UserMapper.toDto(userStorage.findById(id));
  }

  public void removeById(Long id) {
    log.info("Удаляем пользователя с id = {}", id);
    userStorage.delete(id);
  }

  public List<UserDto> findAll() {
    return userStorage.findAll().stream().map(UserMapper::toDto).collect(Collectors.toList());
  }
}
