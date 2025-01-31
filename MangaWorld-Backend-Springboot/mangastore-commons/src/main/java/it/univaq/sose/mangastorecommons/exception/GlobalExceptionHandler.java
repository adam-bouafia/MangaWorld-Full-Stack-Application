package it.univaq.sose.mangastorecommons.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.UUID;

/**
 * @author: Adam Bouafia, Date : 07-01-2024
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(RunTimeExceptionPlaceHolder.class)
  public ResponseEntity<ErrorResponse> handleCustomException(RunTimeExceptionPlaceHolder ex) {

    /**
     * Creates an ErrorResponse object with the provided error code and message.
     * 
     * @param errorCode The error code to be set in the ErrorResponse object.
     * @param errorMessage The error message to be set in the ErrorResponse object.
     * @return An ErrorResponse object populated with the provided error code and message.
     */
    ErrorResponse errorResponse = populateErrorResponse("400", ex.getMessage());
    log.error("Something went wrong, Exception : " + ex.getMessage());
    ex.printStackTrace();
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

  }

  @ExceptionHandler(InvalidFormatException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(InvalidFormatException ex) {

    ErrorResponse errorResponse = populateErrorResponse("400", ex.getMessage());
    log.error("Something went wrong, Exception : " + ex.getMessage());
    ex.printStackTrace();
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleCustomException(Exception ex) {

    ErrorResponse errorResponse = populateErrorResponse("500",
            ex.getMessage());
    log.error("Something went wrong, Exception : " + ex.getMessage());
    ex.printStackTrace();
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(AccessDeniedException ex) {

    ErrorResponse errorResponse = populateErrorResponse("401",
            ex.getMessage());
    log.error("Something went wrong, Exception : " + ex.getMessage());
    ex.printStackTrace();
    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);

  }

  public ErrorResponse populateErrorResponse(String code, String message) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setUuid(UUID.randomUUID());

    Error error = new Error();
    error.setCode(code);
    error.setMessage(message);

    errorResponse.setErrors(Collections.singletonList(error));

    return errorResponse;
  }
}
