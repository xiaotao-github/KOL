package com.kol_friends.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Component
@Data
public class Fans implements Serializable {
    String id;
    String user_id;
    String fans_id;
    Date fans_time;
}
