package com.codegym.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RejectRequestPayload {
    @NotBlank(message = "Lý do từ chối không được để trống")
    private String reason;
}
