package ru.practicum.shareit.user.dto;

import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRequestDto {
  private String name;

  @Email(message = "Неверный формат email")
  private String email;
}
