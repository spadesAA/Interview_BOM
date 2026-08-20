package com.rufus.bomtable.dao.impl;

import com.rufus.bomtable.dao.MaterialSubstituteDao;
import com.rufus.bomtable.dto.MaterialSubstituteRespDTO;
import com.rufus.bomtable.entity.MaterialSubstitute;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MaterialSubstituteDaoImpl implements MaterialSubstituteDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MaterialSubstitute> findActiveByProductAndVersion(String productCode, String bomVersion) {
        return entityManager
                .createQuery(
                        "SELECT s FROM MaterialSubstitute s " +
                                "WHERE s.productCode = :productCode AND s.bomVersion = :version AND s.isActive = true",
                        MaterialSubstitute.class)
                .setParameter("productCode", productCode)
                .setParameter("version", bomVersion)
                .getResultList();
    }

    @Override
    public Optional<MaterialSubstitute> findByProductVersionAndOriginalCode(String productCode, String bomVersion, String originalMaterialCode) {
        try {
            MaterialSubstitute result = entityManager
                    .createQuery(
                            "SELECT s FROM MaterialSubstitute s " +
                                    "WHERE s.productCode = :productCode AND s.bomVersion = :version AND s.originalMaterialCode = :code",
                            MaterialSubstitute.class)
                    .setParameter("productCode", productCode)
                    .setParameter("version", bomVersion)
                    .setParameter("code", originalMaterialCode)
                    .getSingleResult();
            return Optional.of(result);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<MaterialSubstitute> findById(Long id) {
        return Optional.ofNullable(entityManager.find(MaterialSubstitute.class, id));
    }

    @Override
    public List<MaterialSubstituteRespDTO> findAllWithNames() {
        return entityManager
                .createQuery(
                        "SELECT new com.rufus.bomtable.dto.MaterialSubstituteRespDTO(" +
                                "s.id, s.bomVersion, s.productCode, s.originalMaterialCode, om.materialName, " +
                                "s.substituteMaterialCode, sm.materialName, s.substituteQuantity, " +
                                "sm.unitPrice, s.reason, s.isActive) " +
                                "FROM MaterialSubstitute s " +
                                "JOIN Material om ON s.originalMaterialCode = om.materialCode " +
                                "JOIN Material sm ON s.substituteMaterialCode = sm.materialCode " +
                                "ORDER BY s.productCode, s.bomVersion, s.originalMaterialCode",
                        MaterialSubstituteRespDTO.class)
                .getResultList();
    }

    @Override
    public void save(MaterialSubstitute materialSubstitute) {
        entityManager.merge(materialSubstitute);
    }

    @Override
    public boolean existsByMaterialCode(String materialCode) {
        Long count = entityManager
                .createQuery(
                        "SELECT COUNT(s) FROM MaterialSubstitute s " +
                                "WHERE s.originalMaterialCode = :code OR s.substituteMaterialCode = :code",
                        Long.class)
                .setParameter("code", materialCode)
                .getSingleResult();
        return count > 0;
    }
}
