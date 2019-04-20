package com.kol_room.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Component
public class ResponseSignal {
    private String cmd;
    private List<Map<String,Object>> data;

    public ResponseSignal() {
    }

    public ResponseSignal(String cmd, List<Map<String, Object>> data) {
        this.cmd = cmd;
        this.data = data;
    }
}
