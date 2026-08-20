package com.rufus.bomtable.service;

import com.rufus.bomtable.dao.BomStructureDao;
import com.rufus.bomtable.dao.BomVersionDao;
import com.rufus.bomtable.dao.MaterialDao;
import com.rufus.bomtable.dao.MaterialSubstituteDao;
import com.rufus.bomtable.dto.BomCostRespDTO;
import com.rufus.bomtable.dto.BomNodeRespDTO;
import com.rufus.bomtable.dto.BomStructureReqDTO;
import com.rufus.bomtable.dto.BomStructureRespDTO;
import com.rufus.bomtable.dto.BomVersionReqDTO;
import com.rufus.bomtable.entity.BomStructure;
import com.rufus.bomtable.entity.BomVersion;
import com.rufus.bomtable.entity.Material;
import com.rufus.bomtable.entity.MaterialSubstitute;
import com.rufus.bomtable.exception.BomVersionAlreadyExistsException;
import com.rufus.bomtable.exception.InvalidBomStructureException;
import com.rufus.bomtable.exception.MaterialNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BomService {

    private final BomStructureDao bomStructureDao;
    private final MaterialDao materialDao;
    private final MaterialSubstituteDao materialSubstituteDao;
    private final BomVersionDao bomVersionDao;

    public BomService(BomStructureDao bomStructureDao, MaterialDao materialDao,
                       MaterialSubstituteDao materialSubstituteDao, BomVersionDao bomVersionDao) {
        this.bomStructureDao = bomStructureDao;
        this.materialDao = materialDao;
        this.materialSubstituteDao = materialSubstituteDao;
        this.bomVersionDao = bomVersionDao;
    }

    /**
     * 查詢完整BOM樹狀結構。一次撈出該版本所有 bom_structure 列，在記憶體中組裝成樹，
     * 不需要遞迴CTE——因為每個版本的父子關係本來就是攤平存的。
     */
    public BomNodeRespDTO getBomTree(String productCode, String version) {
        String resolvedVersion = resolveVersion(productCode, version);

        Map<String, Material> materialMap = materialDao.findAll().stream()
                .collect(Collectors.toMap(Material::getMaterialCode, m -> m));

        if (!materialMap.containsKey(productCode)) {
            throw new MaterialNotFoundException("查無物料：" + productCode);
        }

        Map<String, List<BomStructure>> childrenMap = bomStructureDao.findByVersion(resolvedVersion).stream()
                .collect(Collectors.groupingBy(BomStructure::getParentCode));

        Map<String, MaterialSubstitute> substituteMap = materialSubstituteDao.findActiveByVersion(resolvedVersion).stream()
                .collect(Collectors.toMap(MaterialSubstitute::getOriginalMaterialCode, s -> s));

        return buildNode(productCode, 1, BigDecimal.ONE, materialMap, childrenMap, substituteMap);
    }

    /**
     * 計算總成本：直接組出樹再取根節點 subtotal，因為成本本來就是從樹狀結構彙總出來的。
     */
    public BomCostRespDTO calculateCost(String productCode, String version) {
        String resolvedVersion = resolveVersion(productCode, version);
        BomNodeRespDTO tree = getBomTree(productCode, resolvedVersion);
        return new BomCostRespDTO(productCode, resolvedVersion, tree.getSubtotal());
    }

    /**
     * 列出目前有哪些產品編碼（給前端下拉選單用）。
     */
    public List<String> getProductCodes() {
        return bomVersionDao.findDistinctProductCodes();
    }

    /**
     * 建立新版本。若設為目前生效版本，先把同產品底下其他版本的 is_current 清成 false，
     * 確保同一產品同時間只有一個生效版本。
     */
    @Transactional
    public void createVersion(BomVersionReqDTO reqDTO) {
        if (bomVersionDao.existsByVersion(reqDTO.getBomVersion())) {
            throw new BomVersionAlreadyExistsException("版本編號已存在：" + reqDTO.getBomVersion());
        }

        if (Boolean.TRUE.equals(reqDTO.getIsCurrent())) {
            bomVersionDao.clearCurrentByProductCode(reqDTO.getProductCode());
        }

        BomVersion version = new BomVersion();
        version.setBomVersion(reqDTO.getBomVersion());
        version.setProductCode(reqDTO.getProductCode());
        version.setDescription(reqDTO.getDescription());
        version.setIsCurrent(reqDTO.getIsCurrent());
        version.setCreatedAt(LocalDateTime.now());
        bomVersionDao.save(version);
    }

    /**
     * 新增一筆BOM結構（父子關係）。只做最基本的驗證：版本/物料必須存在、父物料必須是裝配件、父子不可相同。
     */
    @Transactional
    public void addStructure(BomStructureReqDTO reqDTO) {
        if (reqDTO.getParentCode().equals(reqDTO.getChildCode())) {
            throw new InvalidBomStructureException("父物料與子物料不可相同：" + reqDTO.getParentCode());
        }

        if (!bomVersionDao.existsByVersion(reqDTO.getBomVersion())) {
            throw new InvalidBomStructureException("查無版本：" + reqDTO.getBomVersion());
        }

        Material parent = materialDao.findByCode(reqDTO.getParentCode())
                .orElseThrow(() -> new InvalidBomStructureException("查無父物料：" + reqDTO.getParentCode()));
        if (Boolean.TRUE.equals(parent.getIsLeaf())) {
            throw new InvalidBomStructureException("父物料必須是裝配件（isLeaf=false）：" + reqDTO.getParentCode());
        }

        if (!materialDao.existsByCode(reqDTO.getChildCode())) {
            throw new InvalidBomStructureException("查無子物料：" + reqDTO.getChildCode());
        }

        LocalDateTime now = LocalDateTime.now();
        BomStructure structure = new BomStructure();
        structure.setBomVersion(reqDTO.getBomVersion());
        structure.setParentCode(reqDTO.getParentCode());
        structure.setChildCode(reqDTO.getChildCode());
        structure.setQuantity(reqDTO.getQuantity());
        structure.setCreatedAt(now);
        structure.setUpdatedAt(now);
        bomStructureDao.save(structure);
    }

    public List<BomStructureRespDTO> getStructureList(String bomVersion) {
        return bomStructureDao.findByVersionWithNames(bomVersion);
    }

    private String resolveVersion(String productCode, String version) {
        if (version != null && !version.isBlank()) {
            return version;
        }
        return bomVersionDao.findCurrentByProductCode(productCode)
                .map(BomVersion::getBomVersion)
                .orElseThrow(() -> new MaterialNotFoundException("查無 " + productCode + " 目前生效中的版本"));
    }

    /**
     * 遞迴組裝單一節點。cumulativeQuantity 是沿路徑從根節點累乘下來的數量（含祖先節點的替代比例），
     * 用來讓葉子節點算出正確的實際用量與小計，而不是只看自己相對父節點的 quantity。
     */
    private BomNodeRespDTO buildNode(String materialCode, int edgeQuantity, BigDecimal cumulativeQuantity,
                                      Map<String, Material> materialMap,
                                      Map<String, List<BomStructure>> childrenMap,
                                      Map<String, MaterialSubstitute> substituteMap) {
        Material material = materialMap.get(materialCode);
        MaterialSubstitute substitute = substituteMap.get(materialCode);

        Material displayMaterial = material;
        boolean substituted = false;
        BigDecimal substituteMultiplier = BigDecimal.ONE;

        if (substitute != null) {
            Material substituteMaterial = materialMap.get(substitute.getSubstituteMaterialCode());
            if (substituteMaterial != null) {
                displayMaterial = substituteMaterial;
                substituted = true;
                substituteMultiplier = BigDecimal.valueOf(substitute.getSubstituteQuantity());
            }
        }

        BigDecimal nodeCumulativeQuantity = cumulativeQuantity
                .multiply(BigDecimal.valueOf(edgeQuantity))
                .multiply(substituteMultiplier);

        List<BomStructure> childEdges = childrenMap.getOrDefault(materialCode, List.of());

        List<BomNodeRespDTO> children = childEdges.stream()
                .map(edge -> buildNode(edge.getChildCode(), edge.getQuantity(), nodeCumulativeQuantity,
                        materialMap, childrenMap, substituteMap))
                .toList();

        boolean isLeaf = Boolean.TRUE.equals(material.getIsLeaf());

        BigDecimal subtotal;
        if (isLeaf) {
            subtotal = displayMaterial.getUnitPrice() != null
                    ? displayMaterial.getUnitPrice().multiply(nodeCumulativeQuantity)
                    : BigDecimal.ZERO;
        } else {
            subtotal = children.stream()
                    .map(BomNodeRespDTO::getSubtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        return BomNodeRespDTO.builder()
                .materialCode(displayMaterial.getMaterialCode())
                .materialName(displayMaterial.getMaterialName())
                .unit(displayMaterial.getUnit())
                .quantity(edgeQuantity)
                .unitPrice(isLeaf ? displayMaterial.getUnitPrice() : null)
                .subtotal(subtotal)
                .isSubstituted(substituted)
                .children(children)
                .build();
    }
}
