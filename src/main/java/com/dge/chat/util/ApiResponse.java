package com.dge.chat.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String error;

    public static <T> ApiResponse<T> success(T data) { return new ApiResponse<>(true, data, null); }
    public static <T> ApiResponse<T> failure(String error) { return new ApiResponse<>(false, null, error); }
}
