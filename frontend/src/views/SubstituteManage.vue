<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { getSubstituteList, substituteMaterial, toggleSubstituteStatus } from '@/api/material'
import { useMaterialStore } from '@/stores/material'
import type { MaterialSubstituteReq, MaterialSubstituteResp } from '@/types/material'

const materialStore = useMaterialStore()
const formRef = ref<FormInstance>()
const submitting = ref(false)

const substituteList = ref<MaterialSubstituteResp[]>([])
const listLoading = ref(false)

const form = reactive<MaterialSubstituteReq>({
  bomVersion: '',
  originalMaterialCode: '',
  substituteMaterialCode: '',
  substituteMaterialName: '',
  reason: '',
  substituteQuantity: 1,
  unitPrice: 0,
})

const rules: FormRules = {
  bomVersion: [{ required: true, message: '版本不可為空', trigger: 'blur' }],
  originalMaterialCode: [{ required: true, message: '請選擇原始物料', trigger: 'change' }],
  substituteMaterialCode: [{ required: true, message: '替代物料編碼不可為空', trigger: 'blur' }],
  substituteQuantity: [
    { required: true, message: '替代數量不可為空', trigger: 'blur' },
    { type: 'number', min: 1, message: '替代數量必須大於 0', trigger: 'blur' },
  ],
  unitPrice: [
    { required: true, message: '單價不可為空', trigger: 'blur' },
    { type: 'number', min: 0.01, message: '單價必須大於 0', trigger: 'blur' },
  ],
}

onMounted(() => {
  materialStore.fetchMaterialList()
  fetchSubstituteList()
})

async function fetchSubstituteList() {
  listLoading.value = true
  try {
    substituteList.value = await getSubstituteList()
  } finally {
    listLoading.value = false
  }
}

async function handleSubmit() {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitting.value = true
    try {
      await substituteMaterial(form)
      ElMessage.success('替代料設定成功')
      fetchSubstituteList()
    } finally {
      submitting.value = false
    }
  })
}

async function handleToggle(row: MaterialSubstituteResp, isActive: boolean) {
  const previous = row.isActive
  row.isActive = isActive
  try {
    await toggleSubstituteStatus(row.id, isActive)
    ElMessage.success(isActive ? '已啟用替代料' : '已停用替代料')
  } catch {
    row.isActive = previous
  }
}
</script>

<template>
  <el-row :gutter="24">
    <el-col :span="10">
      <el-card header="替代料設定">
        <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">
          <el-form-item label="版本" prop="bomVersion">
            <el-input v-model="form.bomVersion" placeholder="如 V2" />
          </el-form-item>
          <el-form-item label="原始物料" prop="originalMaterialCode">
            <el-select v-model="form.originalMaterialCode" filterable style="width: 100%">
              <el-option
                v-for="m in materialStore.materialList.filter((m) => m.isLeaf)"
                :key="m.materialCode"
                :label="`${m.materialCode} ${m.materialName}`"
                :value="m.materialCode"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="替代物料編碼" prop="substituteMaterialCode">
            <el-input v-model="form.substituteMaterialCode" />
          </el-form-item>
          <el-form-item label="替代物料名稱" prop="substituteMaterialName">
            <el-input v-model="form.substituteMaterialName" />
          </el-form-item>
          <el-form-item label="替代原因" prop="reason">
            <el-input v-model="form.reason" />
          </el-form-item>
          <el-form-item label="替代數量比例" prop="substituteQuantity">
            <el-input-number v-model="form.substituteQuantity" :min="1" />
          </el-form-item>
          <el-form-item label="單價" prop="unitPrice">
            <el-input-number v-model="form.unitPrice" :min="0.01" :precision="2" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="submitting" @click="handleSubmit">設定替代料</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </el-col>
    <el-col :span="14">
      <el-table :data="substituteList" v-loading="listLoading">
        <el-table-column label="版本" prop="bomVersion" width="80" />
        <el-table-column label="原始物料">
          <template #default="{ row }">{{ row.originalMaterialCode }} {{ row.originalMaterialName }}</template>
        </el-table-column>
        <el-table-column label="替代物料">
          <template #default="{ row }">{{ row.substituteMaterialCode }} {{ row.substituteMaterialName }}</template>
        </el-table-column>
        <el-table-column label="替代原因" prop="reason" />
        <el-table-column label="數量比例" prop="substituteQuantity" width="90" />
        <el-table-column label="單價" prop="unitPrice" width="90" />
        <el-table-column label="是否生效" width="90">
          <template #default="{ row }">
            <el-switch :model-value="row.isActive" @change="(val) => handleToggle(row, val as boolean)" />
          </template>
        </el-table-column>
      </el-table>
    </el-col>
  </el-row>
</template>
