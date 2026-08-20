package com.rufus.bomtable.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class BomNodeRespDTO {
    private String materialCode;
    private String materialName;
    private String unit;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private Boolean isSubstituted;
    private List<BomNodeRespDTO> children;
}
