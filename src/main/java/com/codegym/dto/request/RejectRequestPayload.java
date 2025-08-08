package com.codegym.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RejectRequestPayload {
    @NotBlank(message = "{validation.reject.reason.notblank}")
    private String reason;
}
