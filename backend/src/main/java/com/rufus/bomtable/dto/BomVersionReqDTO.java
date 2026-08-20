package com.rufus.bomtable.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BomVersionReqDTO {

    @NotBlank(message = "版本編號不可為空")
    private String bomVersion;

    @NotBlank(message = "產品編碼不可為空")
    private String productCode;

    private String description;

    @NotNull(message = "isCurrent 不可為空")
    private Boolean isCurrent;
}
