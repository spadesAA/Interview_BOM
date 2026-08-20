-- 版本編號原本是全系統唯一（bom_version 單一主鍵），導致不同產品無法各自從 V1 開始編號。
-- 改成 (product_code, bom_version) 組合主鍵，讓版本編號只需要在「同一產品底下」唯一即可。

-- 1. bom_structure / material_substitute 各加 product_code 欄位，從 bom_version 表回填既有資料
ALTER TABLE bom_structure ADD COLUMN product_code VARCHAR(50);
UPDATE bom_structure bs SET product_code = bv.product_code
    FROM bom_version bv WHERE bs.bom_version = bv.bom_version;
ALTER TABLE bom_structure ALTER COLUMN product_code SET NOT NULL;

ALTER TABLE material_substitute ADD COLUMN product_code VARCHAR(50);
UPDATE material_substitute ms SET product_code = bv.product_code
    FROM bom_version bv WHERE ms.bom_version = bv.bom_version;
ALTER TABLE material_substitute ALTER COLUMN product_code SET NOT NULL;

-- 2. 移除舊的外鍵、舊的單一主鍵
ALTER TABLE material_substitute DROP CONSTRAINT fk_material_substitute_bom_version;
ALTER TABLE bom_version DROP CONSTRAINT bom_version_pkey;

-- 3. bom_version 改成組合主鍵
ALTER TABLE bom_version ADD CONSTRAINT pk_bom_version PRIMARY KEY (product_code, bom_version);

-- 4. 重建外鍵，改成組合欄位參照
ALTER TABLE bom_structure ADD CONSTRAINT fk_bom_structure_version
    FOREIGN KEY (product_code, bom_version) REFERENCES bom_version(product_code, bom_version);
ALTER TABLE material_substitute ADD CONSTRAINT fk_material_substitute_version
    FOREIGN KEY (product_code, bom_version) REFERENCES bom_version(product_code, bom_version);

-- 5. 索引補上 product_code
DROP INDEX IF EXISTS idx_parent_version;
CREATE INDEX idx_parent_version ON bom_structure(product_code, bom_version, parent_code);

DROP INDEX IF EXISTS idx_substitute_version_original;
CREATE INDEX idx_substitute_version_original ON material_substitute(product_code, bom_version, original_material_code, is_active);
