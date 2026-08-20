package com.rufus.bomtable.dao;

import com.rufus.bomtable.entity.BomVersion;

import java.util.List;
import java.util.Optional;

public interface BomVersionDao {
    Optional<BomVersion> findByProductAndVersion(String productCode, String bomVersion);
    Optional<BomVersion> findCurrentByProductCode(String productCode);
    List<String> findDistinctProductCodes();
    boolean existsByProductAndVersion(String productCode, String bomVersion);
    void save(BomVersion bomVersion);
    void clearCurrentByProductCode(String productCode);
}
