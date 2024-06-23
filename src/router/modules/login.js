export default [
  {
    path: '/login',
    name: 'login',
    component: () => import('@/views/login/login.vue') // 懒加载登录页面组件
  }
]
