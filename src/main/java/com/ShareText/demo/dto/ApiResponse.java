package com.ShareText.demo.dto;


public class ApiResponse<T> {

    private int status;
    private T data;
    private String message;

    public ApiResponse(int status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}