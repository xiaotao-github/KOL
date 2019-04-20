package com.kol_friends.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Data
public class Response implements Serializable {
    String msg;
    String data;
    int status;
    public Response(){

    }
    public Response(String msg, String data, int status){
        this.msg=msg;
        this.data=data;
        this.status=status;
    }
}
