package com.rufus.bomtable.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BomCostRespDTO {
    private String productCode;
    private String bomVersion;
    private BigDecimal totalCost;
}
