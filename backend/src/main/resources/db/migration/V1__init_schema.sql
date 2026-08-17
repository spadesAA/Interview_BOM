-- 物料主檔
CREATE TABLE material (
    material_code   VARCHAR(50) PRIMARY KEY,
    material_name   VARCHAR(100) NOT NULL,
    unit            VARCHAR(10),
    unit_price      DECIMAL(10,2),
    is_leaf         BOOLEAN NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 版本主檔
CREATE TABLE bom_version (
    bom_version     VARCHAR(20) PRIMARY KEY,
    product_code    VARCHAR(50) NOT NULL,
    description     VARCHAR(200),
    is_current      BOOLEAN DEFAULT FALSE,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- BOM結構關聯表（含版本）
CREATE TABLE bom_structure (
    id              BIGSERIAL PRIMARY KEY,
    bom_version     VARCHAR(20) NOT NULL DEFAULT 'V1',
    parent_code     VARCHAR(50),
    child_code      VARCHAR(50) NOT NULL,
    quantity        INT NOT NULL DEFAULT 1,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_bom_structure_parent FOREIGN KEY (parent_code) REFERENCES material(material_code),
    CONSTRAINT fk_bom_structure_child FOREIGN KEY (child_code) REFERENCES material(material_code)
);

CREATE INDEX idx_parent_version ON bom_structure(bom_version, parent_code);
CREATE INDEX idx_child_code ON bom_structure(child_code);

-- 替代料關係表
CREATE TABLE material_substitute (
    id                          BIGSERIAL PRIMARY KEY,
    bom_version                 VARCHAR(20) NOT NULL,
    original_material_code     VARCHAR(50) NOT NULL,
    substitute_material_code   VARCHAR(50) NOT NULL,
    substitute_quantity        INT NOT NULL DEFAULT 1,
    reason                      VARCHAR(200),
    is_active                   BOOLEAN DEFAULT FALSE,
    created_at                  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at                  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_material_substitute_original FOREIGN KEY (original_material_code) REFERENCES material(material_code),
    CONSTRAINT fk_material_substitute_bom_version FOREIGN KEY (bom_version) REFERENCES bom_version(bom_version)
);

CREATE INDEX idx_substitute_version_original ON material_substitute(bom_version, original_material_code, is_active);
