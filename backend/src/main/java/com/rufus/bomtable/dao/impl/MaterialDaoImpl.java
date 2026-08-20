package com.rufus.bomtable.dao.impl;

import com.rufus.bomtable.dao.MaterialDao;
import com.rufus.bomtable.entity.Material;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MaterialDaoImpl implements MaterialDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Material> findByCode(String materialCode) {
        return Optional.ofNullable(entityManager.find(Material.class, materialCode));
    }

    @Override
    public List<Material> findAll() {
        return entityManager
                .createQuery("SELECT m FROM Material m ORDER BY m.materialCode", Material.class)
                .getResultList();
    }

    @Override
    public boolean existsByCode(String materialCode) {
        Long count = entityManager
                .createQuery("SELECT COUNT(m) FROM Material m WHERE m.materialCode = :code", Long.class)
                .setParameter("code", materialCode)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public void save(Material material) {
        entityManager.merge(material);
    }

    @Override
    public void deleteByCode(String materialCode) {
        Material material = entityManager.find(Material.class, materialCode);
        if (material != null) {
            entityManager.remove(material);
        }
    }
}
