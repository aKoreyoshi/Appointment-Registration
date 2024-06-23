/*
 * @Descripttion:
 * @version:
 * @Date: 2021-04-20 11:06:21
 * @LastEditors: huzhushan@126.com
 * @LastEditTime: 2021-04-21 09:36:55
 * @Author: huzhushan@126.com
 * @HomePage: https://huzhushan.gitee.io/vue3-element-admin
 * @Github: https://github.com/huzhushan/vue3-element-admin
 * @Donate: https://huzhushan.gitee.io/vue3-element-admin/donate/
 */
import request from '@/utils/request'

const API_URL = `/admin/system/index`

// 登录接口
export const Login = data => {
  return request({
    url: `${API_URL}/login`,
    method: 'post',
    data,
  })
}

// 获取登录用户信息
export const GetUserinfo = () => {
  return request({
    url: `${API_URL}/getUserInfo`,
    method: 'get',
  })
}

// 退出登录
export const Logout = () => {
  return request({
    url: `${API_URL}/logout`,
    method: 'get',
  })
}

// 获取天气
export const getWeather = () => {
  return request({
    url: `${API_URL}/getWeather`,
    method: 'get',
  })
}


