package ru.practicum.shareit.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.groups.Group;

@Data
@AllArgsConstructor
@Builder
public class UserDto {
  @Null(groups = Group.OnInsert.class)
  private Long id;

  @NotBlank(groups = Group.OnInsert.class)
  private String name;

  @Email(groups = {Group.OnInsert.class, Group.OnUpdate.class})
  @NotBlank(groups = Group.OnInsert.class)
  private String email;
}
