import request from '@/utils/request'

const API_URL = `/api/order`

export const generateOrder = (scheduleId, patientId) => {
  return request({
    url: `${API_URL}/auth/generateOrder/${scheduleId}/${patientId}`,
    method: 'post'
  })
}

// 根据订单编号获取订单详情
export const getOrderByNo = (orderNo) => {
  return request({
    url: `${API_URL}/auth/getOrderByOrderNo/${orderNo}`,
    method: 'get'
  })
}

// 条件分页查询订单
export const getOrderList = (currentPage, pageSize, orderQuertVo) => {
  return request({
    url: `${API_URL}/auth/getOrderList/${currentPage}/${pageSize}`,
    method: 'get',
    params: orderQuertVo
  })
}

// 获取订单状态
export const getOrderStatus = () => {
  return request({
    url: `${API_URL}/auth/getStatusList`,
    method: 'get'
  })
}

// 取消订单
export const cancelOrder = (orderNo) => {
  return request({
    url: `${API_URL}/auth/cancelOrder/${orderNo}`,
    method: 'get'
  })
}

// 更新订单状态为已支付
export const isPay = (orderNo) => {
  return request({
    url: `${API_URL}/auth/updateOrderStatus/${orderNo}`,
    method: 'put'
  })
}
