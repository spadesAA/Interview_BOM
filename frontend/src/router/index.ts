import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/bom',
    },
    {
      path: '/bom',
      name: 'bom-viewer',
      component: () => import('../views/BomViewer.vue'),
    },
    {
      path: '/materials',
      name: 'material-manage',
      component: () => import('../views/MaterialManage.vue'),
    },
    {
      path: '/materials/substitute',
      name: 'substitute-manage',
      component: () => import('../views/SubstituteManage.vue'),
    },
    {
      path: '/bom/assemble',
      name: 'bom-assemble',
      component: () => import('../views/BomAssemble.vue'),
    },
  ],
})

export default router
