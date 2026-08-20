package com.rufus.bomtable.service;

import com.rufus.bomtable.dao.MaterialDao;
import com.rufus.bomtable.dao.MaterialSubstituteDao;
import com.rufus.bomtable.dto.MaterialSubstituteReqDTO;
import com.rufus.bomtable.dto.MaterialSubstituteRespDTO;
import com.rufus.bomtable.entity.Material;
import com.rufus.bomtable.entity.MaterialSubstitute;
import com.rufus.bomtable.exception.MaterialNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MaterialSubstituteService {

    private final MaterialSubstituteDao materialSubstituteDao;
    private final MaterialDao materialDao;

    public MaterialSubstituteService(MaterialSubstituteDao materialSubstituteDao, MaterialDao materialDao) {
        this.materialSubstituteDao = materialSubstituteDao;
        this.materialDao = materialDao;
    }

    @Transactional
    public void applySubstitute(MaterialSubstituteReqDTO reqDTO) {
        // 1. 替代物料若尚未存在於物料主檔，先新增
        if (!materialDao.existsByCode(reqDTO.getSubstituteMaterialCode())) {
            LocalDateTime now = LocalDateTime.now();
            Material substituteMaterial = new Material();
            substituteMaterial.setMaterialCode(reqDTO.getSubstituteMaterialCode());
            substituteMaterial.setMaterialName(reqDTO.getSubstituteMaterialName());
            substituteMaterial.setUnitPrice(reqDTO.getUnitPrice());
            substituteMaterial.setIsLeaf(true);
            substituteMaterial.setCreatedAt(now);
            substituteMaterial.setUpdatedAt(now);
            materialDao.save(substituteMaterial);
        }

        // 2. 寫入/更新替代關係，設定 is_active = true
        MaterialSubstitute substitute = materialSubstituteDao
                .findByProductVersionAndOriginalCode(reqDTO.getProductCode(), reqDTO.getBomVersion(), reqDTO.getOriginalMaterialCode())
                .orElseGet(MaterialSubstitute::new);

        LocalDateTime now = LocalDateTime.now();
        boolean isNew = substitute.getCreatedAt() == null;

        substitute.setBomVersion(reqDTO.getBomVersion());
        substitute.setProductCode(reqDTO.getProductCode());
        substitute.setOriginalMaterialCode(reqDTO.getOriginalMaterialCode());
        substitute.setSubstituteMaterialCode(reqDTO.getSubstituteMaterialCode());
        substitute.setSubstituteQuantity(reqDTO.getSubstituteQuantity());
        substitute.setReason(reqDTO.getReason());
        substitute.setIsActive(true);
        substitute.setUpdatedAt(now);
        if (isNew) {
            substitute.setCreatedAt(now);
        }

        materialSubstituteDao.save(substitute);
    }

    public List<MaterialSubstituteRespDTO> getSubstituteList() {
        return materialSubstituteDao.findAllWithNames();
    }

    @Transactional
    public void toggleActive(Long id, Boolean isActive) {
        MaterialSubstitute substitute = materialSubstituteDao.findById(id)
                .orElseThrow(() -> new MaterialNotFoundException("查無替代料設定：" + id));

        substitute.setIsActive(isActive);
        substitute.setUpdatedAt(LocalDateTime.now());
        materialSubstituteDao.save(substitute);
    }
}
