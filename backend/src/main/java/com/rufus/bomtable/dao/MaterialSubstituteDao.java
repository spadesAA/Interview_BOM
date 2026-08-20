package com.rufus.bomtable.dao;

import com.rufus.bomtable.dto.MaterialSubstituteRespDTO;
import com.rufus.bomtable.entity.MaterialSubstitute;

import java.util.List;
import java.util.Optional;

public interface MaterialSubstituteDao {
    List<MaterialSubstitute> findActiveByVersion(String bomVersion);
    Optional<MaterialSubstitute> findByVersionAndOriginalCode(String bomVersion, String originalMaterialCode);
    Optional<MaterialSubstitute> findById(Long id);
    List<MaterialSubstituteRespDTO> findAllWithNames();
    void save(MaterialSubstitute materialSubstitute);
    boolean existsByMaterialCode(String materialCode);
}
