package com.rufus.bomtable.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "material_substitute")
@Data
public class MaterialSubstitute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bom_version")
    private String bomVersion;

    @Column(name = "original_material_code")
    private String originalMaterialCode;

    @Column(name = "substitute_material_code")
    private String substituteMaterialCode;

    @Column(name = "substitute_quantity")
    private Integer substituteQuantity;

    private String reason;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
