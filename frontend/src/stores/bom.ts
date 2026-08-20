import { ref } from 'vue'
import { defineStore } from 'pinia'
import { getProductCodes, getBomTree, getBomCost } from '@/api/bom'
import type { BomNodeResp, BomCostResp } from '@/types/bom'

export const useBomStore = defineStore('bom', () => {
  const productCode = ref('PCB-CONTROL')
  const productCodeOptions = ref<string[]>([])
  const bomVersion = ref('')
  const tree = ref<BomNodeResp | null>(null)
  const cost = ref<BomCostResp | null>(null)
  const loading = ref(false)

  async function fetchProductCodes() {
    productCodeOptions.value = await getProductCodes()
  }

  async function fetchBom() {
    loading.value = true
    try {
      const version = bomVersion.value || undefined
      const [treeResult, costResult] = await Promise.all([
        getBomTree(productCode.value, version),
        getBomCost(productCode.value, version),
      ])
      tree.value = treeResult
      cost.value = costResult
    } finally {
      loading.value = false
    }
  }

  return { productCode, productCodeOptions, bomVersion, tree, cost, loading, fetchProductCodes, fetchBom }
})
