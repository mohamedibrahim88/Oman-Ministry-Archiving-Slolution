package com.example.gateway.exception;

import com.example.gateway.DTOs.GeneralResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException customException){
        return new ResponseEntity<>(new GeneralResponse<>("failed","500",customException.getMessage()) ,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<?> handleCustomException(RuntimeException runtimeException){
        String runtimeExceptionMessage = runtimeException.getMessage();
        return new ResponseEntity<>(new GeneralResponse<>("failed","500",runtimeExceptionMessage) ,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleCustomException(Exception exception){
        String exceptionMessage = exception.getMessage();
        return new ResponseEntity<>(new GeneralResponse<>("failed","500",exceptionMessage),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
