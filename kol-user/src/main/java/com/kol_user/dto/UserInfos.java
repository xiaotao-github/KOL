package com.kol_user.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class UserInfos {
    String uid;
    String name;
    String avatar;
    String signature;
}
