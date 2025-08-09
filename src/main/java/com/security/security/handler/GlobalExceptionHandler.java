package com.security.security.handler;

import com.security.security.core.exceptions.InvalidTokenException;
import com.security.security.core.exceptions.UnauthorizedException;
import com.security.security.core.exceptions.UserNotFoundException;
import com.security.security.core.model.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<ErrorResponseDTO> internalServerErrorExceptionHandler(Exception exception) {
        return ResponseEntity.internalServerError()
                .body(new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> notFoundExceptionHandler(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), exception.getMessage()));
    }

    @ExceptionHandler({InvalidTokenException.class, UnauthorizedException.class})
    public ResponseEntity<ErrorResponseDTO> notFoundExceptionHandler(Exception exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(), exception.getMessage()));
    }
}
