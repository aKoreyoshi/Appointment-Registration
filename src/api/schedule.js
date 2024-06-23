import request from '@/utils/request'

const API_URL = `/admin/hosp/schedule`

export const getScheduleList = (currentPage,pageSize,scheduleDto) => {
    return request({
        url: `${API_URL}/getScheduleList/${currentPage}/${pageSize}`,
        method: 'get',
        params: scheduleDto
    })
}

// 根据id删除
export const deleteShceduleById = (id) => {
    return request({
        url: `${API_URL}/deleteSchedule/${id}`,
        method: 'delete'
    })
}