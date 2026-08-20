# BOM 管理系統

製造業 BOM（Bill of Materials）管理系統，用物件導向思想處理樹狀結構的查詢、成本計算與料件替換。

## 功能

**核心功能**
1. 查詢 BOM 完整樹狀結構
2. BOM 總成本計算
3. 替代料更新（查詢與成本計算即時反映最新結果）

**延伸功能**
4. 物料管理（新增／編輯／刪除）
5. 替代料設定，並可查詢清單、開關生效狀態
6. BOM 組裝（建立新版本、逐筆新增結構）

## 技術棧

- **後端**：Spring Boot 4.1（Java 21）+ PostgreSQL 16（Docker）+ Flyway + Lombok
- **前端**：Vue 3（Composition API + TypeScript）+ Pinia + Element Plus

## 架構重點

- 分層架構：Controller → Service → DAO（介面 + EntityManager）→ Entity / DTO
- 樹狀查詢與成本計算共用同一套遞迴組裝邏輯，符合 Composite Pattern 精神
- 版本主檔採 `(product_code, bom_version)` 組合主鍵，每個產品可各自維護獨立的版本歷史
- 替代料採「關係表 + `is_active` 開關」設計：原始結構永遠保留，替代顯示為查詢當下動態套用的結果，保留完整異動歷史

## 快速開始

### 1. 啟動資料庫

```bash
cd backend
docker compose up -d
```

### 2. 啟動後端

```bash
cd backend
./mvnw spring-boot:run
```

啟動後於 `http://localhost:8080`，Flyway 會自動建表並寫入種子資料。

### 3. 啟動前端

```bash
cd frontend
npm install
npm run dev
```

啟動後於 `http://localhost:5173`。
