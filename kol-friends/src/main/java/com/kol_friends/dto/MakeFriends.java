package com.kol_friends.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
public class MakeFriends {
    String id;
    String user_id;
    String friends_id;
    Date friends_time;
    int intimacy_value;
}
