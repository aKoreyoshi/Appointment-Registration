import request from '@/utils/request';

const API_URL = `api/user`

// 获取登录用户信息
export const GetUserinfo = () => {
  return request({
    url: `${API_URL}/getUserInfo`,
    method: 'get',
  })
}

// 登录
export const Login = (loginVO) => {
  return request({
    url: `${API_URL}/loginUser`,
    method: 'post',
    data: loginVO,
  })
}

// 获取验证码
export const getCode = (phone) => {
  return request({
    url: `/api/sms/sendCode/${phone}`,
    method: 'get',
    // params: { phone }
  })
}

// 获取证件类型
export const getCertificateTypes = () => {
  return request({
    url: `${API_URL}/getCredentialsType`,
    method: 'get',
  })
}

// 认证
export const authentication = (authenticationVO) => {
  return request({
    url: `${API_URL}/auth/authUser`,
    method: 'post',
    data: authenticationVO,
  })

}
