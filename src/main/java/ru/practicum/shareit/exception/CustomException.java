package ru.practicum.shareit.exception;

public class CustomException {
  public static class UserException extends RuntimeException {
    public UserException(String message) {
      super(message);
    }
  }

  public static class EmailException extends UserException {
    public EmailException(String message) {
      super(message);
    }
  }

  public static class UserNotFoundException extends UserException {
    public UserNotFoundException(String message) {
      super(message);
    }
  }

  public static class ItemException extends RuntimeException {
    public ItemException(String message) {
      super(message);
    }
  }

  public static class ItemNotFoundException extends ItemException {
    public ItemNotFoundException(String message) {
      super(message);
    }
  }

  public static class ItemNotAvailableException extends ItemException {
    public ItemNotAvailableException(String message) {
      super(message);
    }
  }

  public static class BookingDateTimeException extends RuntimeException {
    public BookingDateTimeException(String message) {
      super(message);
    }
  }

  public static class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(String message) {
      super(message);
    }
  }

  public static class BookingStatusException extends RuntimeException {
    public BookingStatusException(String message) {
      super(message);
    }
  }

  public static class BookingStateException extends RuntimeException {
    public BookingStateException(String message) {
      super(message);
    }
  }

  public static class ItemRequestNotFoundException extends RuntimeException {
    public ItemRequestNotFoundException(String message) {
      super(message);
    }
  }
}
