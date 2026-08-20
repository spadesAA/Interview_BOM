package com.rufus.bomtable.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MaterialRespDTO {
    private String materialCode;
    private String materialName;
    private String unit;
    private BigDecimal unitPrice;
    private Boolean isLeaf;
}
