package com.rufus.bomtable.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class BomStructureReqDTO {

    @NotBlank(message = "版本不可為空")
    private String bomVersion;

    @NotBlank(message = "產品編碼不可為空")
    private String productCode;

    @NotBlank(message = "父物料編碼不可為空")
    private String parentCode;

    @NotBlank(message = "子物料編碼不可為空")
    private String childCode;

    @NotNull
    @Positive(message = "數量必須大於 0")
    private Integer quantity;
}
