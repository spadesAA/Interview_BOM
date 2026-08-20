package com.rufus.bomtable.dao;

import com.rufus.bomtable.entity.Material;

import java.util.List;
import java.util.Optional;

public interface MaterialDao {
    Optional<Material> findByCode(String materialCode);
    List<Material> findAll();
    boolean existsByCode(String materialCode);
    void save(Material material);
    void deleteByCode(String materialCode);
}
