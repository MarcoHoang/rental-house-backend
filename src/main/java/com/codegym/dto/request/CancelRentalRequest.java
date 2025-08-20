package com.codegym.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CancelRentalRequest {
    @NotBlank(message = "{validation.cancel.reason.notBlank}")
    private String reason;
}
