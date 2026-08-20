import { ref } from 'vue'
import { defineStore } from 'pinia'
import { getMaterialList } from '@/api/material'
import type { MaterialResp } from '@/types/material'

export const useMaterialStore = defineStore('material', () => {
  const materialList = ref<MaterialResp[]>([])
  const loading = ref(false)

  async function fetchMaterialList() {
    loading.value = true
    try {
      materialList.value = await getMaterialList()
    } finally {
      loading.value = false
    }
  }

  return { materialList, loading, fetchMaterialList }
})
