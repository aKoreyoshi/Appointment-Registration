import request from '@/utils/request';

const API_URL = `/admin/hosp/user`

// 获取用户列表
export const getUserList = () => {
    return request({
        url: `${API_URL}/getUserList`,
        method: 'get'
    })
}


// 更新用户状态
export const updateStatus = (userStatusDto) => {
    return request({
        url: `${API_URL}/updateStatus`,
        method: 'put',
        data: userStatusDto
    })
}

// 完成用户认证
export const authUser = (phone) => {
    return request({
        url: `${API_URL}/authUser/${phone}`,
        method: 'put'
    })
}