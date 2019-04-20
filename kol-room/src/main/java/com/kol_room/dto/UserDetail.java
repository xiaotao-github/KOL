package com.kol_room.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class UserDetail {
    String user_id;
    String user_image;
    String username;
//    position代表麦位，例如1代表第一个麦
    int position;
//    魅力值
    int charmValue;
}
