package com.rufus.bomtable.controller;

import com.rufus.bomtable.dto.MaterialReqDTO;
import com.rufus.bomtable.dto.MaterialRespDTO;
import com.rufus.bomtable.dto.MaterialSubstituteReqDTO;
import com.rufus.bomtable.dto.MaterialSubstituteRespDTO;
import com.rufus.bomtable.dto.MaterialSubstituteStatusReqDTO;
import com.rufus.bomtable.service.MaterialService;
import com.rufus.bomtable.service.MaterialSubstituteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    private final MaterialService materialService;
    private final MaterialSubstituteService materialSubstituteService;

    public MaterialController(MaterialService materialService, MaterialSubstituteService materialSubstituteService) {
        this.materialService = materialService;
        this.materialSubstituteService = materialSubstituteService;
    }

    @GetMapping
    public ResponseEntity<List<MaterialRespDTO>> getMaterialList() {
        return ResponseEntity.ok(materialService.getMaterialList());
    }

    @GetMapping("/{materialCode}")
    public ResponseEntity<MaterialRespDTO> getMaterial(@PathVariable String materialCode) {
        return ResponseEntity.ok(materialService.getMaterialByCode(materialCode));
    }

    @PostMapping
    public ResponseEntity<MaterialRespDTO> createMaterial(@Valid @RequestBody MaterialReqDTO reqDTO) {
        MaterialRespDTO created = materialService.createMaterial(reqDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{materialCode}")
    public ResponseEntity<MaterialRespDTO> updateMaterial(@PathVariable String materialCode,
                                                            @Valid @RequestBody MaterialReqDTO reqDTO) {
        return ResponseEntity.ok(materialService.updateMaterial(materialCode, reqDTO));
    }

    @DeleteMapping("/{materialCode}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable String materialCode) {
        materialService.deleteMaterial(materialCode);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/substitute")
    public ResponseEntity<Void> substituteMaterial(@Valid @RequestBody MaterialSubstituteReqDTO reqDTO) {
        materialSubstituteService.applySubstitute(reqDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/substitute")
    public ResponseEntity<List<MaterialSubstituteRespDTO>> getSubstituteList() {
        return ResponseEntity.ok(materialSubstituteService.getSubstituteList());
    }

    @PatchMapping("/substitute/{id}")
    public ResponseEntity<Void> toggleSubstituteActive(@PathVariable Long id,
                                                         @Valid @RequestBody MaterialSubstituteStatusReqDTO reqDTO) {
        materialSubstituteService.toggleActive(id, reqDTO.getIsActive());
        return ResponseEntity.ok().build();
    }
}
