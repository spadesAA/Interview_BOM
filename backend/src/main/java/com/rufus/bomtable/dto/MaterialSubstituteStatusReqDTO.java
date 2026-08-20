package com.rufus.bomtable.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MaterialSubstituteStatusReqDTO {

    @NotNull(message = "isActive 不可為空")
    private Boolean isActive;
}
