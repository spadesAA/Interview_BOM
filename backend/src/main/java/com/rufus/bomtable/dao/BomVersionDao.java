package com.rufus.bomtable.dao;

import com.rufus.bomtable.entity.BomVersion;

import java.util.List;
import java.util.Optional;

public interface BomVersionDao {
    Optional<BomVersion> findByVersion(String bomVersion);
    Optional<BomVersion> findCurrentByProductCode(String productCode);
    List<String> findDistinctProductCodes();
    boolean existsByVersion(String bomVersion);
    void save(BomVersion bomVersion);
    void clearCurrentByProductCode(String productCode);
}
