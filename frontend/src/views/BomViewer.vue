<script setup lang="ts">
import { onMounted } from 'vue'
import { useBomStore } from '@/stores/bom'
import BomTreeNode from '@/components/BomTreeNode.vue'

const bomStore = useBomStore()

onMounted(() => {
  bomStore.fetchProductCodes()
  bomStore.fetchBom()
})
</script>

<template>
  <div v-loading="bomStore.loading">
    <el-form inline>
      <el-form-item label="產品編碼">
        <el-select v-model="bomStore.productCode" filterable style="width: 200px">
          <el-option v-for="code in bomStore.productCodeOptions" :key="code" :label="code" :value="code" />
        </el-select>
      </el-form-item>
      <el-form-item label="版本">
        <el-input v-model="bomStore.bomVersion" placeholder="留空=目前版本" style="width: 160px" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="bomStore.fetchBom">查詢</el-button>
      </el-form-item>
    </el-form>

    <el-card v-if="bomStore.tree" style="margin-bottom: 16px">
      <BomTreeNode :node="bomStore.tree" />
    </el-card>

    <el-alert
      v-if="bomStore.cost"
      :title="`總成本：$${bomStore.cost.totalCost}（版本 ${bomStore.cost.bomVersion}）`"
      type="success"
      :closable="false"
    />
  </div>
</template>
