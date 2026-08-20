package com.rufus.bomtable.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "bom_version")
@Data
public class BomVersion {

    @Id
    @Column(name = "bom_version")
    private String bomVersion;

    @Column(name = "product_code")
    private String productCode;

    private String description;

    @Column(name = "is_current")
    private Boolean isCurrent;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
