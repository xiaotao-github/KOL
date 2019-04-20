package com.kol_friends.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
public class MessageList {
    String title;
    String subhead;
    String image;
    String url;
    Date time;
}
