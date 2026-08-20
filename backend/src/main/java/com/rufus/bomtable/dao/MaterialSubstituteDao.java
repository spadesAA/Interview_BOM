package com.rufus.bomtable.dao;

import com.rufus.bomtable.dto.MaterialSubstituteRespDTO;
import com.rufus.bomtable.entity.MaterialSubstitute;

import java.util.List;
import java.util.Optional;

public interface MaterialSubstituteDao {
    List<MaterialSubstitute> findActiveByProductAndVersion(String productCode, String bomVersion);
    Optional<MaterialSubstitute> findByProductVersionAndOriginalCode(String productCode, String bomVersion, String originalMaterialCode);
    Optional<MaterialSubstitute> findById(Long id);
    List<MaterialSubstituteRespDTO> findAllWithNames();
    void save(MaterialSubstitute materialSubstitute);
    boolean existsByMaterialCode(String materialCode);
}
