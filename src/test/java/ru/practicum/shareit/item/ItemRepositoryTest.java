package ru.practicum.shareit.item;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

@DataJpaTest
class ItemRepositoryTest {

  @Autowired private ItemRepository itemRepository;
  @Autowired private UserRepository userRepository;

  private User user;

  //  @BeforeEach
  //  void setUp() {
  //    user = user
  //    }

  @BeforeAll
  static void beforeAll() {}

  @Test
  void search() {}

  @Test
  void findByIdAndBookerIdAndFinishedBooking() {}

  @Test
  void findByRequest() {}

  @Test
  void findByOwner() {
    User user = User.builder().name("Timmy").email("Timmy@owner.com").build();
    Item item =
        Item.builder().name("item1").owner(user).available(true).description("item1 desc").build();

    //        itemRepository.save()
  }
}
