package com.rufus.bomtable.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "bom_version")
@Data
@IdClass(BomVersion.BomVersionId.class)
public class BomVersion {

    @Id
    @Column(name = "product_code")
    private String productCode;

    @Id
    @Column(name = "bom_version")
    private String bomVersion;

    private String description;

    @Column(name = "is_current")
    private Boolean isCurrent;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BomVersionId implements Serializable {
        private String productCode;
        private String bomVersion;
    }
}
