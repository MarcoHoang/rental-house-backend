package com.codegym.dto.request;

import lombok.Data;

@Data
public class UpdateHostStatusRequest {
    private boolean active;
    
    // Thêm getter/setter thủ công để đảm bảo
    public boolean getActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
}
