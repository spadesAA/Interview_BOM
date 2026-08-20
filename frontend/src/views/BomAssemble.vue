<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { createBomVersion, addBomStructure, getBomStructureList } from '@/api/bom'
import { useMaterialStore } from '@/stores/material'
import type { BomVersionReq, BomStructureReq, BomStructureResp } from '@/types/bom'

const materialStore = useMaterialStore()

const versionFormRef = ref<FormInstance>()
const versionSubmitting = ref(false)
const versionForm = reactive<BomVersionReq>({
  bomVersion: '',
  productCode: '',
  description: '',
  isCurrent: true,
})
const versionRules: FormRules = {
  bomVersion: [{ required: true, message: '版本編號不可為空', trigger: 'blur' }],
  productCode: [{ required: true, message: '產品編碼不可為空', trigger: 'blur' }],
}

const structureFormRef = ref<FormInstance>()
const structureSubmitting = ref(false)
const structureForm = reactive<BomStructureReq>({
  bomVersion: '',
  productCode: '',
  parentCode: '',
  childCode: '',
  quantity: 1,
})
const structureRules: FormRules = {
  bomVersion: [{ required: true, message: '版本不可為空', trigger: 'blur' }],
  productCode: [{ required: true, message: '產品編碼不可為空', trigger: 'blur' }],
  parentCode: [{ required: true, message: '請選擇父物料', trigger: 'change' }],
  childCode: [{ required: true, message: '請選擇子物料', trigger: 'change' }],
  quantity: [
    { required: true, message: '數量不可為空', trigger: 'blur' },
    { type: 'number', min: 1, message: '數量必須大於 0', trigger: 'blur' },
  ],
}

const structureList = ref<BomStructureResp[]>([])
const listLoading = ref(false)

onMounted(() => {
  materialStore.fetchMaterialList()
})

async function handleCreateVersion() {
  if (!versionFormRef.value) return
  await versionFormRef.value.validate(async (valid) => {
    if (!valid) return
    versionSubmitting.value = true
    try {
      await createBomVersion(versionForm)
      ElMessage.success('版本建立成功')
      structureForm.bomVersion = versionForm.bomVersion
      structureForm.productCode = versionForm.productCode
      fetchStructureList()
    } finally {
      versionSubmitting.value = false
    }
  })
}

async function fetchStructureList() {
  if (!structureForm.bomVersion || !structureForm.productCode) return
  listLoading.value = true
  try {
    structureList.value = await getBomStructureList(structureForm.productCode, structureForm.bomVersion)
  } finally {
    listLoading.value = false
  }
}

async function handleAddStructure() {
  if (!structureFormRef.value) return
  await structureFormRef.value.validate(async (valid) => {
    if (!valid) return
    structureSubmitting.value = true
    try {
      await addBomStructure(structureForm)
      ElMessage.success('新增結構成功')
      structureForm.parentCode = ''
      structureForm.childCode = ''
      structureForm.quantity = 1
      fetchStructureList()
    } finally {
      structureSubmitting.value = false
    }
  })
}
</script>

<template>
  <el-row :gutter="24">
    <el-col :span="8">
      <el-card header="建立版本">
        <el-form ref="versionFormRef" :model="versionForm" :rules="versionRules" label-width="90px">
          <el-form-item label="版本編號" prop="bomVersion">
            <el-input v-model="versionForm.bomVersion" placeholder="如 V4" />
          </el-form-item>
          <el-form-item label="產品編碼" prop="productCode">
            <el-input v-model="versionForm.productCode" placeholder="新主板的物料編碼" />
          </el-form-item>
          <el-form-item label="版本說明" prop="description">
            <el-input v-model="versionForm.description" />
          </el-form-item>
          <el-form-item label="設為生效版">
            <el-switch v-model="versionForm.isCurrent" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="versionSubmitting" @click="handleCreateVersion">
              建立版本
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <el-card header="加入結構" style="margin-top: 24px">
        <el-form ref="structureFormRef" :model="structureForm" :rules="structureRules" label-width="90px">
          <el-form-item label="產品編碼" prop="productCode">
            <el-input v-model="structureForm.productCode" placeholder="如 PCB-DEMO" @blur="fetchStructureList" />
          </el-form-item>
          <el-form-item label="版本" prop="bomVersion">
            <el-input v-model="structureForm.bomVersion" placeholder="如 V4" @blur="fetchStructureList" />
          </el-form-item>
          <el-form-item label="父物料" prop="parentCode">
            <el-select v-model="structureForm.parentCode" filterable style="width: 100%">
              <el-option
                v-for="m in materialStore.materialList.filter((m) => !m.isLeaf)"
                :key="m.materialCode"
                :label="`${m.materialCode} ${m.materialName}`"
                :value="m.materialCode"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="子物料" prop="childCode">
            <el-select v-model="structureForm.childCode" filterable style="width: 100%">
              <el-option
                v-for="m in materialStore.materialList"
                :key="m.materialCode"
                :label="`${m.materialCode} ${m.materialName}`"
                :value="m.materialCode"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="數量" prop="quantity">
            <el-input-number v-model="structureForm.quantity" :min="1" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="structureSubmitting" @click="handleAddStructure">
              加入結構
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </el-col>
    <el-col :span="16">
      <el-card header="目前結構">
        <el-table :data="structureList" v-loading="listLoading">
          <el-table-column label="父物料">
            <template #default="{ row }">{{ row.parentCode }} {{ row.parentName }}</template>
          </el-table-column>
          <el-table-column label="子物料">
            <template #default="{ row }">{{ row.childCode }} {{ row.childName }}</template>
          </el-table-column>
          <el-table-column label="數量" prop="quantity" width="80" />
        </el-table>
      </el-card>
    </el-col>
  </el-row>
</template>
