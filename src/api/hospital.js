import request from '@/utils/request'

const API_URL = `/admin/hosp/hospital`

// 上传医院
export const uploadHospital = (hospital) => {
    return request({
        url: `${API_URL}/saveHosp`,
        method: 'post',
        data: hospital
    })
}

// 获取医院信息
export const getHospInfo = () => {
    return request({
        url: `${API_URL}/getHospital`,
        method: 'get',
    })
}

// 更新医院状态
export const updateHospStatus = (hospitalStatusDto) => {
    return request({
        url: `${API_URL}/updateHospStatus`,
        method: 'post',
        data: hospitalStatusDto
    })
}