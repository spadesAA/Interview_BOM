package com.rufus.bomtable.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BomStructureRespDTO {

    private Long id;
    private String bomVersion;
    private String productCode;
    private String parentCode;
    private String parentName;
    private String childCode;
    private String childName;
    private Integer quantity;
}
