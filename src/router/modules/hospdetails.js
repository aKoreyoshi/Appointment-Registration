// 导入组件
const Layout = () => import('@/layout/index.vue')
const Hospital = () => import('@/views/hospdetails/hospital.vue')

// 导出该组件
export default [
    {
        path: '/hospdetails',
        component: Layout,
        meta: { title: '医院详情' },
        icon: 'Document',
        children: [
            {
                path: '/hospital',
                name: '',
                component: Hospital,
                // meta: { title: '医院详情' }
            }
        ]
    }
]