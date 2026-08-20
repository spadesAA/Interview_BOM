package com.rufus.bomtable.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MaterialReqDTO {

    @NotBlank(message = "物料編碼不可為空")
    private String materialCode;

    @NotBlank(message = "物料名稱不可為空")
    private String materialName;

    private String unit;

    private BigDecimal unitPrice;

    @NotNull(message = "isLeaf 不可為空")
    private Boolean isLeaf;
}
