package ru.practicum.shareit.item.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.groups.Group;

/** TODO Sprint add-controllers. */
@Data
@AllArgsConstructor
@Builder
public class ItemDto {
  @Null(groups = Group.OnInsert.class)
  @NotNull(groups = Group.OnUpdate.class)
  private Long id;

  @NotBlank private String name;
  @NotBlank private String description;
  @NotNull private Boolean available;
  private Long requestId;
}
