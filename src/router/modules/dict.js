const Layout = () => import('@/layout/index.vue')
const Dict = () => import('@/views/dict/dict.vue')

export default [
    {
        path: '/dict',
        component: Layout,
        name: 'dict',
        meta: { title: '数据字典' },
        icon: 'Menu',
        children: [
            {
                path: '/dict',
                component: Dict,
                name: '',
            }
        ]
    }
]