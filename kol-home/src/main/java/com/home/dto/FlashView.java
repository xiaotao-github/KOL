package com.home.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class FlashView {
    String id;
    String imageUrl;
    String link;
}
