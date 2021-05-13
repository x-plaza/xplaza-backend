package com.backend.xplaza.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.time.LocalDateTime;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ApiResponse {
    public long responseTime;
    public String responseType;
    public int status;
    public String response;
    public String message;
    public String data;

    public ApiResponse(long responseTime, String responseType, int status, String response, String message, String data) {
        this.responseTime = responseTime;
        this.responseType = responseType;
        this.status = status;
        this.response = response;
        this.message = message;
        this.data = data;
    }

    public String getTimestamp() {
        return LocalDateTime.now().toString();
    }
}
