package ru.practicum.shareit.booking.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = DatesValidator.class)
public @interface ValidDates {
  String message() default "Неверные даты начала и окончания";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
