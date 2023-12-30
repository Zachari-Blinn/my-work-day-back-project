package com.blinnproject.myworkdayback.exception;

import com.blinnproject.myworkdayback.payload.response.ErrorDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  // global exceptions
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception, WebRequest webRequest) {
    ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false));
    return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest webRequest) {
    ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
      webRequest.getDescription(false));
    return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({EmailAlreadyTakenException.class})
  public ResponseEntity<Object> handleEmailAlreadyTakenException(EmailAlreadyTakenException exception) {
    return ResponseEntity
      .status(HttpStatus.CONFLICT)
      .body(exception.getMessage());
  }

  @ExceptionHandler({UsernameAlreadyTakenException.class})
  public ResponseEntity<Object> handleUsernameAlreadyTakenException(UsernameAlreadyTakenException exception) {
    return ResponseEntity
      .status(HttpStatus.CONFLICT)
      .body(exception.getMessage());
  }

  @ExceptionHandler({TokenRefreshException.class})
  public ResponseEntity<Object> handleTokenRefreshException(TokenRefreshException exception) {
    return ResponseEntity
      .status(HttpStatus.FORBIDDEN)
      .body(exception.getMessage());
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error ->{
      String fieldName = ((FieldError)error).getField();
      String message = error.getDefaultMessage();
      errors.put(fieldName, message);
    });

    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorDetails> handleAccessDeniedException(AccessDeniedException exception, WebRequest webRequest) {
    ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
      webRequest.getDescription(false));
    return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler({CompressImageException.class})
  public ResponseEntity<Object> handleCompressImageException(CompressImageException exception) {
    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(exception.getMessage());
  }

  @ExceptionHandler({DecompressImageException.class})
  public ResponseEntity<Object> handleDecompressImageException(DecompressImageException exception) {
    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(exception.getMessage());
  }

  @ExceptionHandler({ProfilePictureIsEmptyException.class})
  public ResponseEntity<Object> handleProfilePictureIsEmptyException() {
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }

  @ExceptionHandler({TrainingWithCurrentUserNotFound.class})
  public ResponseEntity<Object> handleTrainingWithCurrentUserNotFound(TrainingWithCurrentUserNotFound exception) {
    return ResponseEntity
      .status(HttpStatus.NOT_FOUND)
      .body(exception.getMessage());
  }

  @ExceptionHandler({TrainingAlreadyPerformedException.class})
  public ResponseEntity<Object> handleTrainingAlreadyPerformedException(TrainingAlreadyPerformedException exception) {
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(exception.getMessage());
  }

  @ExceptionHandler({AuthenticationException.class})
  public ResponseEntity<Object> handleAuthenticationException(AuthenticationException exception) {
    return ResponseEntity
      .status(HttpStatus.UNAUTHORIZED)
      .body(exception.getMessage());
  }

  @ExceptionHandler({UserNotFoundException.class})
  public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException exception) {
    return ResponseEntity
      .status(HttpStatus.NOT_FOUND)
      .body(exception.getMessage());
  }

  @ExceptionHandler({TokenNotFoundException.class})
  public ResponseEntity<Object> handleTokenNotFoundException(TokenNotFoundException exception) {
    return ResponseEntity
      .status(HttpStatus.NOT_FOUND)
      .body(exception.getMessage());
  }

  @ExceptionHandler({ResetPasswordTokenAttemptsExceededException.class})
  public ResponseEntity<Object> handleResetPasswordTokenAttemptsExceededException(ResetPasswordTokenAttemptsExceededException exception) {
    return ResponseEntity
      .status(HttpStatus.UNAUTHORIZED)
      .body(exception.getMessage());
  }

  @ExceptionHandler({ResetPasswordTokenExpiredException.class})
  public ResponseEntity<Object> handleResetPasswordTokenExpiredException(ResetPasswordTokenExpiredException exception) {
    return ResponseEntity
      .status(HttpStatus.UNAUTHORIZED)
      .body(exception.getMessage());
  }

  @ExceptionHandler({ResetPasswordTokenInvalidException.class})
  public ResponseEntity<Object> handleResetPasswordTokenInvalidException(ResetPasswordTokenInvalidException exception) {
    return ResponseEntity
      .status(HttpStatus.UNAUTHORIZED)
      .body(exception.getMessage());
  }
}
