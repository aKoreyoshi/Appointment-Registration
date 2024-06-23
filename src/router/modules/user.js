export default [
  {
    path: '/userauth',
    name: 'userauth',
    component: () => import('@/views/user/userauth.vue'), // 动态导入组件，懒加载方式
    meta: {
      // 这里可以添加一些元数据，例如是否需要登录权限等
      requiresAuth: true, // 需要登录权限
    }
  },
  {
    path: '/patient',
    name: 'patient',
    component: () => import('@/views/patient/patient.vue'),
    meta: {
      // 这里可以添加一些元数据，例如是否需要登录权限等
      requiresAuth: true, // 需要登录权限
    }
  },
  {
    path: '/add',
    name: 'add',
    component: () => import('@/views/patient/add.vue'),
    meta: {
      // 这里可以添加一些元数据，例如是否需要登录权限等
      requiresAuth: true, // 需要登录权限
    }
  },
  {
    path: '/order',
    name: 'order',
    component: () => import('@/views/user/order.vue'),
    meta: {
      // 这里可以添加一些元数据，例如是否需要登录权限等
      requiresAuth: true, // 需要登录权限
    }
  },
  {
    path: '/detail',
    name: 'detail',
    component: () => import('@/views/patient/detail.vue'),
    meta: {
      // 这里可以添加一些元数据，例如是否需要登录权限等
      requiresAuth: true, // 需要登录权限
    }
  },
  {
    path: '/orderdetail',
    name: 'orderdetail',
    component: () => import('@/views/user/orderdetail.vue'),
    meta: {
      // 这里可以添加一些元数据，例如是否需要登录权限等
      requiresAuth: true, // 需要登录权限
    }
  }
]
