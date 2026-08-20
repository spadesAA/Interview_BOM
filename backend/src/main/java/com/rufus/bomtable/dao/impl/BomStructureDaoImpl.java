package com.rufus.bomtable.dao.impl;

import com.rufus.bomtable.dao.BomStructureDao;
import com.rufus.bomtable.dto.BomStructureRespDTO;
import com.rufus.bomtable.entity.BomStructure;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BomStructureDaoImpl implements BomStructureDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<BomStructure> findByProductAndVersion(String productCode, String bomVersion) {
        return entityManager
                .createQuery(
                        "SELECT b FROM BomStructure b WHERE b.productCode = :productCode AND b.bomVersion = :version",
                        BomStructure.class)
                .setParameter("productCode", productCode)
                .setParameter("version", bomVersion)
                .getResultList();
    }

    @Override
    public boolean existsByMaterialCode(String materialCode) {
        Long count = entityManager
                .createQuery(
                        "SELECT COUNT(b) FROM BomStructure b WHERE b.parentCode = :code OR b.childCode = :code",
                        Long.class)
                .setParameter("code", materialCode)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public void save(BomStructure bomStructure) {
        entityManager.merge(bomStructure);
    }

    @Override
    public List<BomStructureRespDTO> findByProductAndVersionWithNames(String productCode, String bomVersion) {
        return entityManager
                .createQuery(
                        "SELECT new com.rufus.bomtable.dto.BomStructureRespDTO(" +
                                "s.id, s.bomVersion, s.productCode, s.parentCode, pm.materialName, " +
                                "s.childCode, cm.materialName, s.quantity) " +
                                "FROM BomStructure s " +
                                "JOIN Material pm ON s.parentCode = pm.materialCode " +
                                "JOIN Material cm ON s.childCode = cm.materialCode " +
                                "WHERE s.productCode = :productCode AND s.bomVersion = :version " +
                                "ORDER BY s.parentCode, s.childCode",
                        BomStructureRespDTO.class)
                .setParameter("productCode", productCode)
                .setParameter("version", bomVersion)
                .getResultList();
    }
}
