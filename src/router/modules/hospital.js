export default [
  {
    path: '/hospdetail',
    name: 'HospitalDetail',
    component: () => import('@/views/hospital/hospdetail.vue')
  },

  {
    path: '/instructions',
    name: 'Instruction',
    component: () => import('@/views/hospital/instructions.vue')
  },

  // 预约挂号
  {
    path: '/schedule',
    name: 'Schedule',
    component: () => import('@/views/hospital/schedule.vue')
  },

  // 下单预约
  {
    path: '/booking',
    name: 'Booking',
    component: () => import('@/views/hospital/booking.vue'),
    meta: {
      requiresAuth: true
    }
  }
]
