<script setup lang="ts">
import { useRoute } from 'vue-router'

const route = useRoute()
</script>

<template>
  <el-menu mode="horizontal" router :default-active="route.path">
    <el-menu-item index="/bom">BOM 查詢 + 成本計算</el-menu-item>
    <el-menu-item index="/materials">物料管理</el-menu-item>
    <el-menu-item index="/materials/substitute">替代料設定</el-menu-item>
    <el-menu-item index="/bom/assemble">BOM組裝</el-menu-item>
  </el-menu>
  <div class="main-content">
    <RouterView />
  </div>
</template>

<style scoped>
.main-content {
  padding: 24px;
}
</style>
