package com.codegym.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class UpdateImageOrderRequest {
    private List<Long> imageIds;
}
