package ru.practicum.shareit.item.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

/** TODO Sprint add-controllers. */
@Data
@AllArgsConstructor
public class ItemDto {
  private Integer id;
  @NotEmpty private String name;
  @NotEmpty private String description;
  @NotNull private Boolean available;
}
