import request from "@/utils/request";

const API_URL = `/api/user/patient`

// 添加就诊人
export const savePatient = (patientDto) => {
  return request({
    url: `${API_URL}/auth/savePatient`,
    method: 'post',
    data: patientDto
  })
}

// 修改就诊人
export const updatePatient = (patient) => {
  return request({
    url: `${API_URL}/auth/updatePatient`,
    method: 'put',
    data: patient
  })
}

// 删除就诊人
export const removePatient = (id) => {
  return request({
    url: `${API_URL}/auth/removePatient/${id}`,
    method: 'delete'
  })
}

// 查询就诊人列表
export const getPatientList = () => {
  return request({
    url: `${API_URL}/auth/getPatientList`,
    method: 'get',
  })
}

// 根据id查询就诊人
export const getPatientById = (id) => {
  return request({
    url: `${API_URL}/auth/getPatientById/${id}`,
    method: 'get',
  })
}


// 获取地区和民族数据
export const getAreaAndNation = () => {
  return request({
    url: `${API_URL}/getDictData`,
    method: 'get',
  })

}

