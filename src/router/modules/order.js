const Layout = () => import('@/layout/index.vue')
const Order = () => import('@/views/order/orderlist.vue')
const Statistics = () => import('@/views/order/statistics.vue')

export default [
    {
        path: '/order',
        component: Layout,
        name: 'order',
        meta: { title: '订单管理' },
        icon: 'List',
        children: [
            {
                path: '/orderlist',
                component: Order,
                name: 'orderlist',
                meta: { title: '订单列表' },
            },
            {
                path: '/statistics',
                component: Statistics,
                name: 'statistics',
                meta: { title: '订单统计' },
            }
        ]
    }
]