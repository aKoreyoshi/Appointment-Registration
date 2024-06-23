import request from '@/utils/request'

const API_URL = `/admin/hosp/doctor`

// 条件分页查询
export const getDoctorList = (currentPage, pageSize, doctorDto) => {
    return request({
        url: `${API_URL}/getDoctorList/${currentPage}/${pageSize}`,
        method: 'get',
        params: doctorDto
    })
}

// 根据id‘删除
export const deleteDoctorById = (id) => {
    return request({
        url: `${API_URL}/deleteDoctorById/${id}`,
        method: 'delete'
    })
}