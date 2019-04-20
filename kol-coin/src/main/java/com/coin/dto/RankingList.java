package com.coin.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Data
public class RankingList {
    String uid;
    String name;
    String avatar;
    String signature;
    int value;
}
