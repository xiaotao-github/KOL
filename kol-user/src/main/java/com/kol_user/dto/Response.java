package com.kol_user.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class Response {
    String msg;
    Object data;
    int status;
    public Response(){

    }
    public Response(String msg, Object data, int status){
        this.msg=msg;
        this.data=data;
        this.status=status;
    }
}
