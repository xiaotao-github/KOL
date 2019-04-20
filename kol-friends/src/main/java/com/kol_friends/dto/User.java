package com.kol_friends.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Data
public class User implements Serializable {
    String uid;
    String name;
    String avatar;
    String signature;
}
