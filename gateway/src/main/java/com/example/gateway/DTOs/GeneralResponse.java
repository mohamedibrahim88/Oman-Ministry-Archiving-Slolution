package com.example.gateway.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralResponse<T> {

    private String message;
    private String code;
    private T data;

    public GeneralResponse(String message , String code , T data){
        this.message=message;
        this.code=code;
        this.data=data;
    }

}
