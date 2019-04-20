package com.kol_user.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class Remark {
    String user_id;
    //需要备注的用户id
    String uid;
    //备注名
    String remark;
}
