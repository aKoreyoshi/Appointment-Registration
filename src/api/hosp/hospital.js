import request from '@/utils/request'

const API_URL = `/api/hosp`

//获取医院信息
export const getHospitalInfo = () => {
  return request({
    url: `${API_URL}/getHospInfo`,
    method: 'get'
  })
}

//获取医院科室列表
export const getDeptList = () => {
  return request({
    url: `${API_URL}/department/getDepartmentList`,
    method: 'get'
  })
}

//获取可预约排班数据
export const getBookingScheduleRule = (currentPage, pageSize, departmentCode) => {
  return request({
    url: `${API_URL}/schedule/getBookingScheduleRule/${currentPage}/${pageSize}/${departmentCode}`,
    method: 'get',
  })
}

//获取排班数据
export const findScheduleList = (departmentCode, workDate) => {
  return request({
    url: `${API_URL}/schedule/findScheduleList/${departmentCode}/${workDate}`,
    method: 'get',
  })
}

// 根据id获取schedule
export const getScheduleById = (id) => {
  return request({
    url: `${API_URL}/schedule/getScheduleById/${id}`,
    method: 'get',
  })
}

// 获取天气信息
export const getWeatherInfo = () => {
  return request({
    url: `${API_URL}/getWeather`,
    method: 'get',
  })
}
