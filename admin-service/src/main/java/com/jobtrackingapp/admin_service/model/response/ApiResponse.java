package com.jobtrackingapp.admin_service.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private String timestamp;

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, new Date().toString());
    }

        public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null, new Date().toString());
    }
}
