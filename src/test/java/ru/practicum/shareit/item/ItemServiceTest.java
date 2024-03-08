package ru.practicum.shareit.item;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.bookingStatus.BookingStatus;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.exception.CustomException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPlusResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
  @Mock private ItemRepository itemRepository;
  @Mock private UserRepository userRepository;
  @Mock private BookingRepository bookingRepository;
  @Mock private CommentRepository commentRepository;
  @Mock private ItemRequestRepository itemRequestRepository;

  @InjectMocks private ItemService itemService;

  private User user;
  private Item item;
  private ItemRequest itemRequest;
  private final LocalDateTime now = LocalDateTime.now();

  private Booking booking;
  private Comment comment;

  private ItemDto itemDtoIn;
  private ItemDto itemDtoInWithRequest;
  private CommentRequestDto commentRequestDto;

  @BeforeEach
  public void setUp() {
    user = User.builder().id(1L).name("Timmy").email("timmy@email.com").build();
    itemRequest =
        ItemRequest.builder()
            .id(1L)
            .description("Item request description")
            .requester(user)
            .created(now)
            .build();
    item =
        Item.builder()
            .id(1L)
            .name("Item")
            .description("Item description")
            .owner(user)
            .available(true)
            .request(itemRequest)
            .build();

    itemDtoIn =
        ItemDto.builder()
            .id(1L)
            .name("Item")
            .description("Item desctiption")
            .available(true)
            .build();
    itemDtoInWithRequest =
        ItemDto.builder()
            .id(1L)
            .name("Item")
            .description("Item desctiption")
            .available(true)
            .requestId(1L)
            .build();
    booking =
        Booking.builder()
            .id(1L)
            .item(item)
            .booker(user)
            .status(BookingStatus.APPROVED)
            .start(now)
            .end(now)
            .build();

    comment = Comment.builder().id(1L).item(item).text("text").author(user).created(now).build();
    commentRequestDto = new CommentRequestDto("text");
  }

  @Test
  public void createShouldReturnItemDto() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
    when(itemRepository.saveAndFlush(any(Item.class))).thenReturn(item);
    when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.ofNullable(itemRequest));

    ItemDto createdItem = itemService.create(1L, itemDtoIn);

    assertThat(createdItem).isNotNull();
    assertThat(createdItem.getId()).isEqualTo(1L);
    assertThat(createdItem.getName()).isEqualTo(itemDtoIn.getName());

    ItemDto createdItemWithRequest = itemService.create(1L, itemDtoInWithRequest);

    assertThat(createdItemWithRequest).isNotNull();
    assertThat(createdItemWithRequest.getId()).isEqualTo(1L);
    assertThat(createdItemWithRequest.getName()).isEqualTo(itemDtoInWithRequest.getName());
    assertThat(createdItemWithRequest.getRequestId())
        .isEqualTo(itemDtoInWithRequest.getRequestId());
  }

  @Test
  public void findByIdShouldReturnItemPlusResponseDto() {
    when(bookingRepository.findLastBookingBeforeNow(anyLong(), anyLong())).thenReturn(booking);
    when(bookingRepository.findNextBookingAfterNow(anyLong(), anyLong())).thenReturn(booking);
    when(commentRepository.findAllByItemId(anyLong()))
        .thenReturn(Collections.singletonList(comment));
    when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));

    ItemPlusResponseDto itemPlusResponseDto = itemService.findById(1L, 1L);

    assertThat(itemPlusResponseDto.getId()).isEqualTo(item.getId());
    assertThat(itemPlusResponseDto.getComments().size()).isEqualTo(1);
  }

  @Test
  public void findAllByUserIdShouldReturnListOfItemPlusResponseDto() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
    when(bookingRepository.findLastBookingBeforeNow(anyLong(), anyLong())).thenReturn(booking);
    when(bookingRepository.findNextBookingAfterNow(anyLong(), anyLong())).thenReturn(booking);
    when(commentRepository.findAllByItemId(anyLong()))
        .thenReturn(Collections.singletonList(comment));
    when(itemRepository.findByOwner(any(User.class), any(Pageable.class)))
        .thenReturn(Collections.singletonList(item));

    List<ItemPlusResponseDto> dtosWithPagination = itemService.findAllByUserId(1L, 1, 1);

    assertThat(dtosWithPagination.size()).isEqualTo(1);

    List<ItemPlusResponseDto> dtosWithoutPagination = itemService.findAllByUserId(1L, null, null);

    assertThat(dtosWithoutPagination.size()).isEqualTo(1);
  }

  @Test
  public void searchShouldReturnListOfItemDto() {
    when(itemRepository.search(anyString(), any(Pageable.class)))
        .thenReturn(Collections.singletonList(item));

    List<ItemDto> dtosWithPagination = itemService.search("text", 1, 1);

    assertThat(dtosWithPagination.size()).isEqualTo(1);

    List<ItemDto> dtosWithoutPagination = itemService.search("text", null, null);

    assertThat(dtosWithoutPagination.size()).isEqualTo(1);
  }

  @Test
  public void searchWithEmptySearchStringShouldReturnReturnEmptyList() {
    List<ItemDto> dtosWithoutPagination = itemService.search("", null, null);

    assertThat(dtosWithoutPagination.size()).isEqualTo(0);
  }

  @Test
  public void addCommentShouldReturnCommentResponseDto() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
    when(itemRepository.findByIdAndBookerIdAndFinishedBooking(anyLong(), anyLong()))
        .thenReturn(Optional.ofNullable(item));
    when(commentRepository.saveAndFlush(any(Comment.class))).thenReturn(comment);

    CommentResponseDto commentResponseDto = itemService.addComment(1L, 1L, commentRequestDto);

    assertThat(commentResponseDto).isNotNull();
    assertThat(commentResponseDto.getAuthorName()).isEqualTo(comment.getAuthor().getName());
    assertThat(commentResponseDto.getText()).isEqualTo(commentRequestDto.getText());
  }

  @Test
  public void addCommentShouldReturnItemNotAvailableException() {
    when(itemRepository.findByIdAndBookerIdAndFinishedBooking(anyLong(), anyLong()))
        .thenReturn(Optional.empty());

    Assertions.assertThatExceptionOfType(CustomException.ItemNotAvailableException.class)
        .isThrownBy(() -> itemService.addComment(1L, 1L, commentRequestDto));
  }

  @Test
  public void updateFieldsShouldReturnItemDto() {
    when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
    when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
    when(itemRepository.saveAndFlush(any(Item.class))).thenReturn(item);

    Map<String, Object> params =
        new HashMap<>() {
          {
            put("description", "text2");
          }
        };

    ItemDto itemDto = itemService.updateFields(1L, 1L, params);

    assertThat(itemDto).isNotNull();
    assertThat(itemDto.getDescription()).isEqualTo(params.get("description"));
  }

  @Test
  public void updateFieldsThrowsUserNotFoundException() {
    User otherUser = User.builder().id(2L).name("Gimmy").email("gimmy@email.com").build();

    when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
    when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(otherUser));

    Map<String, Object> params =
            new HashMap<>() {
              {
                put("description", "text2");
              }
            };

    Assertions.assertThatExceptionOfType(CustomException.UserNotFoundException.class)
            .isThrownBy(() -> itemService.updateFields(2L, 1L, params));
  }
}
