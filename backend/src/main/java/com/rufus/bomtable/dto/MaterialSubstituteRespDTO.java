package com.rufus.bomtable.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MaterialSubstituteRespDTO {

    private Long id;
    private String bomVersion;
    private String productCode;
    private String originalMaterialCode;
    private String originalMaterialName;
    private String substituteMaterialCode;
    private String substituteMaterialName;
    private Integer substituteQuantity;
    private BigDecimal unitPrice;
    private String reason;
    private Boolean isActive;
}
