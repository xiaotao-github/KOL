package com.kol_friends.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Component
@Data
public class Focus implements Serializable {
    String id;
    String user_id;
    String focus_id;
    Date focus_time;
}
