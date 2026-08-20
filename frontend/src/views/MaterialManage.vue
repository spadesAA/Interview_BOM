<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useMaterialStore } from '@/stores/material'
import { deleteMaterial } from '@/api/material'
import type { MaterialResp } from '@/types/material'
import MaterialForm from '@/components/MaterialForm.vue'

const materialStore = useMaterialStore()

const editDialogVisible = ref(false)
const editingMaterial = ref<MaterialResp | null>(null)

onMounted(() => {
  materialStore.fetchMaterialList()
})

function handleEdit(row: MaterialResp) {
  editingMaterial.value = row
  editDialogVisible.value = true
}

function handleEditSuccess() {
  editDialogVisible.value = false
  materialStore.fetchMaterialList()
}

async function handleDelete(row: MaterialResp) {
  try {
    await ElMessageBox.confirm(`確定要刪除物料「${row.materialCode}」嗎？`, '刪除確認', {
      type: 'warning',
    })
  } catch {
    return
  }

  try {
    await deleteMaterial(row.materialCode)
    ElMessage.success('刪除成功')
    materialStore.fetchMaterialList()
  } catch {
    // 錯誤訊息已由 axios 攔截器統一顯示
  }
}
</script>

<template>
  <el-row :gutter="24">
    <el-col :span="10">
      <el-card header="新增物料">
        <MaterialForm @success="materialStore.fetchMaterialList" />
      </el-card>
    </el-col>
    <el-col :span="14">
      <el-table :data="materialStore.materialList" v-loading="materialStore.loading">
        <el-table-column prop="materialCode" label="物料編碼" />
        <el-table-column prop="materialName" label="物料名稱" />
        <el-table-column prop="unit" label="單位" width="80" />
        <el-table-column prop="unitPrice" label="單價" width="100" />
        <el-table-column label="類型" width="100">
          <template #default="{ row }">
            {{ row.isLeaf ? '零件' : '裝配件' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="140">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">編輯</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">刪除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-col>
  </el-row>

  <el-dialog v-model="editDialogVisible" title="編輯物料" width="480px">
    <MaterialForm mode="edit" :material="editingMaterial" @success="handleEditSuccess" />
  </el-dialog>
</template>
