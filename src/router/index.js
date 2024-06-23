import { createRouter, createWebHistory } from 'vue-router'
import { useApp } from '@/store/modules/app'

import login from './modules/login'
import user from './modules/user'
import hospital from './modules/hospital'
import pay from './modules/pay'

const router = createRouter({
  // history: createWebHistory(import.meta.env.BASE_URL),
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('@/views/index.vue')
    },
    ...login, ...user, ...hospital, ...pay
  ],
});

// 添加前置理由守卫
router.beforeEach((to, from, next) => {
  const userStore = useApp()
  const token = userStore.authorization // token是用户登录状态的标识
  // 检查用户是否已登录，如果未登录则跳转到登录页面
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else {
    // 放行
    next()
  }
})

export default router;
