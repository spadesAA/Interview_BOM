package com.rufus.bomtable.service;

import com.rufus.bomtable.dao.BomStructureDao;
import com.rufus.bomtable.dao.MaterialDao;
import com.rufus.bomtable.dao.MaterialSubstituteDao;
import com.rufus.bomtable.dto.MaterialReqDTO;
import com.rufus.bomtable.dto.MaterialRespDTO;
import com.rufus.bomtable.entity.Material;
import com.rufus.bomtable.exception.MaterialAlreadyExistsException;
import com.rufus.bomtable.exception.MaterialInUseException;
import com.rufus.bomtable.exception.MaterialNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MaterialService {

    private final MaterialDao materialDao;
    private final BomStructureDao bomStructureDao;
    private final MaterialSubstituteDao materialSubstituteDao;

    public MaterialService(MaterialDao materialDao, BomStructureDao bomStructureDao,
                            MaterialSubstituteDao materialSubstituteDao) {
        this.materialDao = materialDao;
        this.bomStructureDao = bomStructureDao;
        this.materialSubstituteDao = materialSubstituteDao;
    }

    public List<MaterialRespDTO> getMaterialList() {
        return materialDao.findAll().stream()
                .map(this::toRespDTO)
                .toList();
    }

    public MaterialRespDTO getMaterialByCode(String materialCode) {
        Material material = materialDao.findByCode(materialCode)
                .orElseThrow(() -> new MaterialNotFoundException("查無物料：" + materialCode));
        return toRespDTO(material);
    }

    @Transactional
    public MaterialRespDTO createMaterial(MaterialReqDTO reqDTO) {
        if (materialDao.existsByCode(reqDTO.getMaterialCode())) {
            throw new MaterialAlreadyExistsException("物料編碼已存在：" + reqDTO.getMaterialCode());
        }

        LocalDateTime now = LocalDateTime.now();
        Material material = new Material();
        material.setMaterialCode(reqDTO.getMaterialCode());
        material.setMaterialName(reqDTO.getMaterialName());
        material.setUnit(reqDTO.getUnit());
        material.setUnitPrice(Boolean.TRUE.equals(reqDTO.getIsLeaf()) ? reqDTO.getUnitPrice() : null);
        material.setIsLeaf(reqDTO.getIsLeaf());
        material.setCreatedAt(now);
        material.setUpdatedAt(now);
        materialDao.save(material);

        return toRespDTO(material);
    }

    @Transactional
    public MaterialRespDTO updateMaterial(String materialCode, MaterialReqDTO reqDTO) {
        Material material = materialDao.findByCode(materialCode)
                .orElseThrow(() -> new MaterialNotFoundException("查無物料：" + materialCode));

        // isLeaf 為既有BOM結構/替代料關係的判斷依據，編輯時不允許變更，只能改名稱/單位/單價
        material.setMaterialName(reqDTO.getMaterialName());
        material.setUnit(reqDTO.getUnit());
        material.setUnitPrice(Boolean.TRUE.equals(material.getIsLeaf()) ? reqDTO.getUnitPrice() : null);
        material.setUpdatedAt(LocalDateTime.now());
        materialDao.save(material);

        return toRespDTO(material);
    }

    @Transactional
    public void deleteMaterial(String materialCode) {
        materialDao.findByCode(materialCode)
                .orElseThrow(() -> new MaterialNotFoundException("查無物料：" + materialCode));

        if (bomStructureDao.existsByMaterialCode(materialCode) || materialSubstituteDao.existsByMaterialCode(materialCode)) {
            throw new MaterialInUseException("此物料已被BOM結構或替代料關係引用，無法刪除：" + materialCode);
        }

        materialDao.deleteByCode(materialCode);
    }

    private MaterialRespDTO toRespDTO(Material material) {
        return new MaterialRespDTO(
                material.getMaterialCode(),
                material.getMaterialName(),
                material.getUnit(),
                material.getUnitPrice(),
                material.getIsLeaf()
        );
    }
}
