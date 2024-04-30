package com.example.gateway.exception;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data

public class CustomException extends RuntimeException{

    private String message;
    private String code;
    CustomException(String message,String code){
        super(message);
        this.code=code;
    }

    CustomException(String message){
        super(message);
    }
}
