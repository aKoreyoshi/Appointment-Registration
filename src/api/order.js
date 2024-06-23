import request from '@/utils/request'

const API_URL = `/admin/hosp/order`


// 获取订单列表
export const getOrderList = (currentPage, pageSize, orderQueryVo) => {
    return request({
        url: `${API_URL}/getPageList/${currentPage}/${pageSize}`,
        method: 'get',
        params: orderQueryVo
    })
}

// 获取订单状态
export const getOrderStatus = () => {
    return request({
        url: `${API_URL}/getStatusList`,
        method: 'get'
    })
}

// 获取订单详情
export const getOrderDetail = (orderNo) => {
    return request({
        url: `${API_URL}/getOrderByNo/${orderNo}`,
        method: 'get'
    })
}

// 获取每日预约数
export const getOrderCount = () => {
    return request({
        url: `${API_URL}/getOrderCount`,
        method: 'get'
    })
}

