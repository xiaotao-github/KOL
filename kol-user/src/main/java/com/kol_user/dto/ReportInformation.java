package com.kol_user.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
public class ReportInformation {
    int id;
    String uid;
    String report_id;
    String type;
    String describe;
    String image1;
    String image2;
    String image3;
    String telephone;
    String QQ;
    Date time;
}
