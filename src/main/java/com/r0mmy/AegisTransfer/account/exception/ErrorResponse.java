package com.r0mmy.AegisTransfer.account.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;  // когда ошибка произошла
    private int status;               // HTTP статус код (400, 404, 500)
    private String error;             // текст ошибки ("Bad Request", "Not Found")
    private String message;           // человеческое описание ошибки
    private String path;              // какой URL запрашивали


}
