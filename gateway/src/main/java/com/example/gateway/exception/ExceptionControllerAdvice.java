package com.example.gateway.exception;

import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> handleCustomException(RuntimeException runtimeException){
        String runtimeExceptionMessage = runtimeException.getMessage();
        return new ResponseEntity<>(runtimeExceptionMessage ,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({RuntimeException.class,Exception.class})
    public ResponseEntity<?> handleCustomException(Exception exception){
        String exceptionMessage = exception.getMessage();
        return new ResponseEntity<>(exceptionMessage,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
