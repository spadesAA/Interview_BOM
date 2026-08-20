package com.rufus.bomtable.dao.impl;

import com.rufus.bomtable.dao.BomVersionDao;
import com.rufus.bomtable.entity.BomVersion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BomVersionDaoImpl implements BomVersionDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<BomVersion> findByVersion(String bomVersion) {
        return Optional.ofNullable(entityManager.find(BomVersion.class, bomVersion));
    }

    @Override
    public Optional<BomVersion> findCurrentByProductCode(String productCode) {
        try {
            BomVersion result = entityManager
                    .createQuery(
                            "SELECT v FROM BomVersion v WHERE v.productCode = :productCode AND v.isCurrent = true",
                            BomVersion.class)
                    .setParameter("productCode", productCode)
                    .getSingleResult();
            return Optional.of(result);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<String> findDistinctProductCodes() {
        return entityManager
                .createQuery("SELECT DISTINCT v.productCode FROM BomVersion v ORDER BY v.productCode", String.class)
                .getResultList();
    }

    @Override
    public boolean existsByVersion(String bomVersion) {
        return entityManager.find(BomVersion.class, bomVersion) != null;
    }

    @Override
    public void save(BomVersion bomVersion) {
        entityManager.merge(bomVersion);
    }

    @Override
    public void clearCurrentByProductCode(String productCode) {
        entityManager
                .createQuery("UPDATE BomVersion v SET v.isCurrent = false WHERE v.productCode = :productCode")
                .setParameter("productCode", productCode)
                .executeUpdate();
    }
}
