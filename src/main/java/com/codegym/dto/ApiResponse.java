package com.codegym.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Standard API response wrapper")
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;
}
