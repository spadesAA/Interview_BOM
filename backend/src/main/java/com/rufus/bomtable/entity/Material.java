package com.rufus.bomtable.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "material")
@Data
public class Material {

    @Id
    @Column(name = "material_code")
    private String materialCode;

    @Column(name = "material_name")
    private String materialName;

    private String unit;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "is_leaf")
    private Boolean isLeaf;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
