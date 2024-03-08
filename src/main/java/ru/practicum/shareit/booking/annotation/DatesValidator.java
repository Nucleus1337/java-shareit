package ru.practicum.shareit.booking.annotation;

import ru.practicum.shareit.booking.dto.BookingRequestDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DatesValidator implements ConstraintValidator<ValidDates, BookingRequestDto> {
  @Override
  public void initialize(ValidDates constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(
      BookingRequestDto bookingRequestDto, ConstraintValidatorContext constraintValidatorContext) {

    if (bookingRequestDto.getStart() == null || bookingRequestDto.getEnd() == null) {
      return true;
    }

    if (bookingRequestDto.getStart().isEqual(bookingRequestDto.getEnd())){
      return false;
    }

    return !bookingRequestDto.getStart().isAfter(bookingRequestDto.getEnd())
            && !bookingRequestDto.getEnd().isBefore(bookingRequestDto.getStart());
  }
}
