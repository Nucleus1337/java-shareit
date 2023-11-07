package ru.practicum.shareit.exception;

import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ErrorHandler {

  @ExceptionHandler({
    CustomException.EmailException.class,
    MethodArgumentNotValidException.class,
    ConstraintViolationException.class
  })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  ErrorResponse getEmailExceptionResponse(Exception e) {
    log.error("Bad reqeust: {}", e.getMessage());
    return new ErrorResponse("Bad request", e.getMessage());
  }

  @ExceptionHandler({CustomException.UserNotFoundException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  ErrorResponse getUserNotFoundExceptionResponse(RuntimeException e) {
    log.error("Not found: {}", e.getMessage());
    return new ErrorResponse("Not found", e.getMessage());
  }

  @ExceptionHandler({Exception.class})
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  ErrorResponse getRuntimeExceptionResponse(Exception e) {
    log.error("Internal Server Error: {}", e.getMessage());
    return new ErrorResponse("Internal Server Error", e.getMessage());
  }
}
