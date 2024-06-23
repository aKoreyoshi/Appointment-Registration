export default [
  {
    path: '/pay',
    name: 'pay',
    component: () => import('@/views/pay/pay.vue'), // 动态导入组件，懒加载方式
    meta: {
      // 这里可以添加一些元数据，例如是否需要登录权限等
      requiresAuth: true, // 需要登录权限
    }
  }
]
