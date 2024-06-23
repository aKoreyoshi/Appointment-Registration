// 导入组件
const Layout = () => import('@/layout/index.vue')
const User = () => import('@/views/user/user.vue')

export default [
    {
        path: '/user',
        component: Layout,
        meta: { title: '用户管理' },
        icon: 'User',
        children: [
            {
                path: '/user',
                component: User,
                // meta: {title: '用户管理' }
            }
        ]

    }
]