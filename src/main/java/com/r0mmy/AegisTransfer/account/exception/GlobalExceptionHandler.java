package com.r0mmy.AegisTransfer.account.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerAccountNotFound(AccountNotFoundException ex,
                                                                 HttpServletRequest request) {
          ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), 404,
                  "Аккаунт не найден", ex.getMessage(), request.getRequestURI());


          return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

    }
    @ExceptionHandler(AccountBlockedException.class)
    public ResponseEntity<ErrorResponse> handlerAccountBlocked(AccountBlockedException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), 404,
                "Аккаунт заблокирован", ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
