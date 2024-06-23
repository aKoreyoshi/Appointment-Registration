import request from '@/utils/request'

const API_URL = `/admin/hosp/dict`

export const getDict = () => {
    return request({
        url: `${API_URL}/buildTrees`,
        method: 'get'
    })
}

// 判断数据是否为空
export const isEmpty = () => {
    return request({
        url: `${API_URL}/isEmpty`,
        method: 'get'
    })
}