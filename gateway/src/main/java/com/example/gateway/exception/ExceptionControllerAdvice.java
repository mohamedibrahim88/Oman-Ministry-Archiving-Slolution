package com.example.gateway.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException customException){
      return null;
    }

    @ExceptionHandler({RuntimeException.class,Exception.class})
    public ResponseEntity<?> handleCustomException(CustomException customException,Exception exception){
        return null;
    }
}
