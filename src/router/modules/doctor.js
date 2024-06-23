// 导入组件
const Layout = () => import('@/layout/index.vue')
const Doctor = () => import('@/views/doctor/doctor.vue')

export default [
    {
        path: '/doctor',
        component: Layout,
        meta: { title: '医生管理' },
        icon: 'User',
        children: [
            {
                path: '/doctor',
                component: Doctor,
                // meta: {title: '用户管理' }
            }
        ]

    }
]