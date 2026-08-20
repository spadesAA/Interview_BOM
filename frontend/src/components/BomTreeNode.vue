<script setup lang="ts">
import type { BomNodeResp } from '@/types/bom'

defineOptions({ name: 'BomTreeNode' })

defineProps<{
  node: BomNodeResp
}>()
</script>

<template>
  <div class="bom-tree-node">
    <div class="node-row">
      <span class="material-code">{{ node.materialCode }}</span>
      <span class="material-name">{{ node.materialName }}</span>
      <el-tag v-if="node.isSubstituted" type="warning" size="small">替代料</el-tag>
      <span class="quantity">數量 x{{ node.quantity }}</span>
      <span v-if="node.unitPrice !== null" class="unit-price">單價 ${{ node.unitPrice }}</span>
      <span class="subtotal">小計 ${{ node.subtotal }}</span>
    </div>
    <div v-if="node.children.length" class="children">
      <BomTreeNode v-for="child in node.children" :key="child.materialCode" :node="child" />
    </div>
  </div>
</template>

<style scoped>
.bom-tree-node {
  margin-left: 8px;
}
.node-row {
  display: flex;
  gap: 12px;
  align-items: center;
  padding: 4px 0;
}
.material-code {
  font-weight: bold;
}
.subtotal {
  color: var(--el-color-primary);
}
.children {
  border-left: 1px dashed var(--el-border-color);
  margin-left: 8px;
  padding-left: 8px;
}
</style>
