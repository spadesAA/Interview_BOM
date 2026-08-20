package com.rufus.bomtable.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MaterialSubstituteReqDTO {

    @NotBlank(message = "版本不可為空")
    private String bomVersion;

    @NotBlank(message = "原始物料編碼不可為空")
    private String originalMaterialCode;

    @NotBlank(message = "替代物料編碼不可為空")
    private String substituteMaterialCode;

    private String substituteMaterialName;

    private String reason;

    @NotNull
    @Positive(message = "替代數量必須大於 0")
    private Integer substituteQuantity;

    @NotNull
    @Positive(message = "單價必須大於 0")
    private BigDecimal unitPrice;
}
