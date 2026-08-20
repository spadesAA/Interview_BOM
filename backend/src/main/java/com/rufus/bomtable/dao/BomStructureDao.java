package com.rufus.bomtable.dao;

import com.rufus.bomtable.dto.BomStructureRespDTO;
import com.rufus.bomtable.entity.BomStructure;

import java.util.List;

public interface BomStructureDao {
    List<BomStructure> findByProductAndVersion(String productCode, String bomVersion);
    boolean existsByMaterialCode(String materialCode);
    void save(BomStructure bomStructure);
    List<BomStructureRespDTO> findByProductAndVersionWithNames(String productCode, String bomVersion);
}
