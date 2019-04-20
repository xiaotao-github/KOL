package com.kol_user.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class UserRole {
    String id;
    String username;
    String password;
    String role_id;
}
