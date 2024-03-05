package ru.practicum.shareit.request;

import static ru.practicum.shareit.request.ItemRequestMapper.toDto;
import static ru.practicum.shareit.request.ItemRequestMapper.toModel;
import static ru.practicum.shareit.utils.UtilsClass.getPageable;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.CustomException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

@Service
@RequiredArgsConstructor
public class ItemRequestService {
  private final UserRepository userRepository;
  private final ItemRepository itemRepository;
  private final ItemRequestRepository itemRequestRepository;

  private User getUser(Long userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(
            () ->
                new CustomException.UserNotFoundException(
                    String.format("Пользователь с id = %s не найден", userId)));
  }

  public ItemRequestDto addRequest(ItemRequestDto itemRequestDto, Long userId) {
    User user = getUser(userId);

    ItemRequest itemRequest = toModel(itemRequestDto, user);

    return toDto(itemRequestRepository.saveAndFlush(itemRequest));
  }

  public List<ItemRequestDto> findAllByOwnerRequestId(Long userId) {
    User user = getUser(userId);

    return itemRequestRepository.findByRequester(user).stream()
        .map(request -> toDto(request, getItems(request)))
        .collect(Collectors.toList());
  }

  public List<ItemRequestDto> findAll(Long userId, Integer from, Integer size) {
    User user = getUser(userId);

    Pageable pageable = getPageable(from, size, Sort.by("created"));

    return itemRequestRepository.findAllByRequesterNot(user, pageable).stream()
        .map(request -> toDto(request, getItems(request)))
        .collect(Collectors.toList());
  }

  public ItemRequestDto findByRequestId(Long userId, Long requestId) {
    getUser(userId);

    ItemRequest itemRequest =
        itemRequestRepository
            .findById(requestId)
            .orElseThrow(
                () ->
                    new CustomException.ItemRequestNotFoundException(
                        String.format("Запрос с id=%s не найден", requestId)));

    ItemRequestDto itemRequestDto = toDto(itemRequest);
    itemRequestDto.setItems(getItems(itemRequest));

    return itemRequestDto;
  }

  private List<ItemDto> getItems(ItemRequest request) {
    return itemRepository.findByRequest(request).stream()
        .map(ItemMapper::toDto)
        .collect(Collectors.toList());
  }
}
