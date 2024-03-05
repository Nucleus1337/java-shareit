package ru.practicum.shareit.request.dto;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

@Builder
@Data
@AllArgsConstructor
public class ItemRequestDto {
  private Long id;
  @NotBlank private String description;
  private LocalDateTime created;
  private List<ItemDto> items;
}
