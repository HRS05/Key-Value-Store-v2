package com.hrs.key.value.common;

import lombok.Data;

@Data
public class OperationObject {
    private String type;
    private String directory;
    private String key;
    private String fileName;
    private String value;

    private Long ttl;
    private Long timestampValue;
}