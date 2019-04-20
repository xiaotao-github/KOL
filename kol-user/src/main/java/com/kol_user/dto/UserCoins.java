package com.kol_user.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class UserCoins {
    String user_id;
    String user_coins;
}
