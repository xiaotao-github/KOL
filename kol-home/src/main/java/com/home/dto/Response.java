package com.home.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class Response {
    String msg;
    String data;
    int status;
    public Response(){

    }

    public Response(String msg, int status){
        this.msg=msg;
        this.status=status;
    }

    public Response(String msg, String data, int status){
        this.msg=msg;
        this.data=data;
        this.status=status;
    }
}
