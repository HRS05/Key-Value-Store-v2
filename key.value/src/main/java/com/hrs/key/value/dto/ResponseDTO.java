package com.hrs.key.value.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Data
public class ResponseDTO {
    private Map<String, Object> data;
    private String message;

    public ResponseDTO() {
    }
    public  void addData(String key, Object data) {
        if (this.data == null) this.data = new HashMap<>();
        this.data.put(key,data);
    }

}
