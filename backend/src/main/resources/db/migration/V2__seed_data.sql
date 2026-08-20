-- 1. 物料主檔（所有下游表都依賴它）
INSERT INTO material (material_code, material_name, unit, unit_price, is_leaf) VALUES
('PCB-CONTROL', '控制器主電路板', '塊', NULL, false),
('POWER-MODULE', '電源模組', '個', NULL, false),
('MCU-MODULE', '主控模組', '個', NULL, false),
('COMM-MODULE', '通訊模組', '個', NULL, false),
('DC-DC-CHIP', 'DC-DC轉換晶片', '個', 12.00, true),
('CAPACITOR-FILTER', '濾波電容', '個', 0.80, true),
('IC-MCU', '微控制器', '個', 65.00, true),
('CRYSTAL', '晶振', '個', 8.00, true),
('RESET-CAP', '復位電容', '個', 0.50, true),
('RS485-IC', 'RS-485通訊晶片', '個', 6.00, true),
('CAN-BUS-IC', 'CAN匯流排控制晶片', '個', 7.00, true),
('IC-MCU-GD32', '微控制器(GD32)', '個', 57.00, true),
('DC-DC-CHIP-PRO', 'DC-DC轉換晶片(強化版)', '個', 15.00, true),
('DC-DC-CHIP-PRO-ALT', 'DC-DC轉換晶片(替代料)', '個', 13.50, true);

-- 2. 版本主檔
INSERT INTO bom_version (bom_version, product_code, description, is_current) VALUES
('V2', 'PCB-CONTROL', '原始設計版本', true),
('V3', 'PCB-CONTROL', 'POWER-MODULE 升級版本', false);

-- 3. BOM結構（依賴 material 已存在）
INSERT INTO bom_structure (bom_version, parent_code, child_code, quantity) VALUES
('V2', 'PCB-CONTROL', 'POWER-MODULE', 1),
('V2', 'PCB-CONTROL', 'MCU-MODULE', 1),
('V2', 'PCB-CONTROL', 'COMM-MODULE', 2),
('V2', 'POWER-MODULE', 'DC-DC-CHIP', 1),
('V2', 'POWER-MODULE', 'CAPACITOR-FILTER', 3),
('V2', 'MCU-MODULE', 'IC-MCU', 1),
('V2', 'MCU-MODULE', 'CRYSTAL', 1),
('V2', 'MCU-MODULE', 'RESET-CAP', 1),
('V2', 'COMM-MODULE', 'RS485-IC', 4),
('V2', 'COMM-MODULE', 'CAN-BUS-IC', 1),

-- V3：結構與 V2 相同，僅 POWER-MODULE 底下的 DC-DC-CHIP 換成 DC-DC-CHIP-PRO
('V3', 'PCB-CONTROL', 'POWER-MODULE', 1),
('V3', 'PCB-CONTROL', 'MCU-MODULE', 1),
('V3', 'PCB-CONTROL', 'COMM-MODULE', 2),
('V3', 'POWER-MODULE', 'DC-DC-CHIP-PRO', 1),
('V3', 'POWER-MODULE', 'CAPACITOR-FILTER', 3),
('V3', 'MCU-MODULE', 'IC-MCU', 1),
('V3', 'MCU-MODULE', 'CRYSTAL', 1),
('V3', 'MCU-MODULE', 'RESET-CAP', 1),
('V3', 'COMM-MODULE', 'RS485-IC', 4),
('V3', 'COMM-MODULE', 'CAN-BUS-IC', 1);

-- 4. 替代料關係（依賴 material、bom_version 已存在）
-- substitute_quantity：替換 1 顆原始料需要幾顆替代料，兩筆目前都是 1:1
INSERT INTO material_substitute (bom_version, original_material_code, substitute_material_code, substitute_quantity, reason, is_active) VALUES
('V2', 'IC-MCU', 'IC-MCU-GD32', 1, '多供應商策略', false),  -- 預設未生效，透過API觸發生效
('V3', 'DC-DC-CHIP-PRO', 'DC-DC-CHIP-PRO-ALT', 1, '供應商缺貨', true);  -- V3 一開始即生效
