<script setup lang="ts">
import { reactive, ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { createMaterial, updateMaterial } from '@/api/material'
import type { MaterialReq, MaterialResp } from '@/types/material'

const props = withDefaults(
  defineProps<{
    mode?: 'create' | 'edit'
    material?: MaterialResp | null
  }>(),
  {
    mode: 'create',
    material: null,
  },
)

const emit = defineEmits<{
  success: []
}>()

const formRef = ref<FormInstance>()
const submitting = ref(false)

const form = reactive<MaterialReq>({
  materialCode: '',
  materialName: '',
  unit: '',
  unitPrice: null,
  isLeaf: true,
})

watch(
  () => props.material,
  (material) => {
    if (material) {
      form.materialCode = material.materialCode
      form.materialName = material.materialName
      form.unit = material.unit
      form.unitPrice = material.unitPrice
      form.isLeaf = material.isLeaf
    }
  },
  { immediate: true },
)

const rules: FormRules = {
  materialCode: [{ required: true, message: '物料編碼不可為空', trigger: 'blur' }],
  materialName: [{ required: true, message: '物料名稱不可為空', trigger: 'blur' }],
  unitPrice: [
    {
      validator: (_rule, value, callback) => {
        if (form.isLeaf && (value === null || value === undefined)) {
          callback(new Error('零件需要填單價'))
        } else {
          callback()
        }
      },
      trigger: 'blur',
    },
  ],
}

async function handleSubmit() {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitting.value = true
    try {
      if (props.mode === 'edit') {
        await updateMaterial(form.materialCode, form)
        ElMessage.success('更新物料成功')
      } else {
        await createMaterial(form)
        ElMessage.success('新增物料成功')
        formRef.value?.resetFields()
        form.unitPrice = null
      }
      emit('success')
    } finally {
      submitting.value = false
    }
  })
}
</script>

<template>
  <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
    <el-form-item label="物料類型">
      <el-radio-group v-model="form.isLeaf" :disabled="mode === 'edit'">
        <el-radio :value="false">主電路板/模組（裝配件）</el-radio>
        <el-radio :value="true">零件</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="物料編碼" prop="materialCode">
      <el-input v-model="form.materialCode" :disabled="mode === 'edit'" />
    </el-form-item>
    <el-form-item label="物料名稱" prop="materialName">
      <el-input v-model="form.materialName" />
    </el-form-item>
    <el-form-item label="單位" prop="unit">
      <el-input v-model="form.unit" />
    </el-form-item>
    <el-form-item v-if="form.isLeaf" label="單價" prop="unitPrice">
      <el-input-number v-model="form.unitPrice" :min="0.01" :precision="2" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">
        {{ mode === 'edit' ? '更新' : '新增' }}
      </el-button>
    </el-form-item>
  </el-form>
</template>
