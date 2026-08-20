package com.rufus.bomtable.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private int code;
    private String message;
    private String path;
    private String timestamp;
    private String traceId;
}
