package az.turingacademy.msauth.exception.handler;

import az.turingacademy.msauth.exception.UserAlreadyExistsException;
import az.turingacademy.msauth.exception.UserNotFoundException;
import az.turingacademy.msauth.exception.UsernameAlreadyExistsException;
import az.turingacademy.msauth.model.dto.ErrorDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<ErrorDto<UserNotFoundException>> handleUserNotFoundException(UserNotFoundException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorDto<>(404,
                        ex.getMessage(),
                        UserNotFoundException.class,
                        LocalDateTime.now()));
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public ResponseEntity<ErrorDto<UserAlreadyExistsException>> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        log.error(ex.getMessage());

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ErrorDto<>(409,
                        ex.getMessage(),
                        UserAlreadyExistsException.class,
                        LocalDateTime.now()));
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorDto<IllegalArgumentException>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error(ex.getMessage());

        return ResponseEntity.status(BAD_REQUEST)
                .body(new ErrorDto<>(400,
                        ex.getMessage(),
                        IllegalArgumentException.class,
                        LocalDateTime.now()));
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(value = UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorDto<UsernameAlreadyExistsException>> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex) {
        log.error(ex.getMessage());

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ErrorDto<>(409,
                        ex.getMessage(),
                        UsernameAlreadyExistsException.class,
                        LocalDateTime.now()));
    }

}
