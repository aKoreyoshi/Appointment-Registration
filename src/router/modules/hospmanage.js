// 导入组件
const Layout = () => import('@/layout/index.vue')
const Schedule = () => import('@/views/hospmanage/schedule.vue')
const Department = () => import('@/views/hospmanage/department.vue')

// 导出组件
export default [
    {
        path: '/hospital',
        component: Layout,
        meta: { title: '医院管理' },
        icon: 'Grid',
        children: [
            {
                path: '/schedule',
                component: Schedule,
                name: 'schedule',
                meta: { title: '排班管理'},
                hidden: false
            },
            {
                path: '/department',
                component: Department,
                name: 'department',
                meta: { title: '科室管理'},
                hidden: false
            },
        ]
    }
]