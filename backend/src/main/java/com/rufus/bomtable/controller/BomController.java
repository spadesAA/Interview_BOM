package com.rufus.bomtable.controller;

import com.rufus.bomtable.dto.BomCostRespDTO;
import com.rufus.bomtable.dto.BomNodeRespDTO;
import com.rufus.bomtable.dto.BomStructureReqDTO;
import com.rufus.bomtable.dto.BomStructureRespDTO;
import com.rufus.bomtable.dto.BomVersionReqDTO;
import com.rufus.bomtable.service.BomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bom")
public class BomController {

    private final BomService bomService;

    public BomController(BomService bomService) {
        this.bomService = bomService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<String>> getProductCodes() {
        return ResponseEntity.ok(bomService.getProductCodes());
    }

    @GetMapping("/{productCode}")
    public ResponseEntity<BomNodeRespDTO> getBomTree(
            @PathVariable String productCode,
            @RequestParam(required = false) String version) {
        return ResponseEntity.ok(bomService.getBomTree(productCode, version));
    }

    @GetMapping("/{productCode}/cost")
    public ResponseEntity<BomCostRespDTO> getBomCost(
            @PathVariable String productCode,
            @RequestParam(required = false) String version) {
        return ResponseEntity.ok(bomService.calculateCost(productCode, version));
    }

    @PostMapping("/versions")
    public ResponseEntity<Void> createVersion(@Valid @RequestBody BomVersionReqDTO reqDTO) {
        bomService.createVersion(reqDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/structure")
    public ResponseEntity<Void> addStructure(@Valid @RequestBody BomStructureReqDTO reqDTO) {
        bomService.addStructure(reqDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/structure")
    public ResponseEntity<List<BomStructureRespDTO>> getStructureList(@RequestParam String version) {
        return ResponseEntity.ok(bomService.getStructureList(version));
    }
}
