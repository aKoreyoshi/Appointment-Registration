import request from '@/utils/request'

const API_URL = `/admin/hosp/department`

// 条件分页查询
export const getDepartmentList = (currentPage, pageSize, departmentDto) => {
    return request({
        url: `${API_URL}/getDepartmentList/${currentPage}/${pageSize}`,
        method: 'get',
        params: departmentDto
    })
}

// 根据id‘删除
export const deleteDepartmentById = (id) => {
    return request({
        url: `${API_URL}/deleteDepartmentById/${id}`,
        method: 'delete'
    })
}

// 获取科室
export const getDepartment = () => {
    return request({
        url: `${API_URL}/getDepList`,
        method: 'get'
    })

}