import axios from 'axios';
import { ElMessage } from 'element-plus'
import { useApp } from '@/store/modules/app';
import router from '@/router';

// 创建请求实例
const service = axios.create({
  baseURL: 'http://111.229.164.96:8888',
  // 指定请求超时的毫秒数
  timeout: 10000,
  // 表示跨域请求时是否需要使用凭证
  withCredentials: false,
});

// 前置拦截器（发起请求之前的拦截）
service.interceptors.request.use(
  (config) => {
    const { authorization } = useApp()
    // 打印authorization
    // console.log('authorization', authorization)
    if (authorization) {
      // 添加一个请求头Authorization ， 该请求头所对应的值为：Bearer token数据
      //config.headers.Authorization = `Bearer ${authorization.token}`
      // 上传传递方式后端解析太麻烦，因此可以更改传递token方式为如下方式
      config.headers.token = `${authorization}`
      // config.headers.Authorization = `Bearer ${authorization.token}`
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

// 后置拦截器（获取到响应时的拦截）
service.interceptors.response.use(
  (response) => {
    /**
     * 根据你的项目实际情况来对 response 和 error 做处理
     * 这里对 response 和 error 不做任何处理，直接返回
     */
    // console.log('拦截的response', response)
    return response.data;
  },
  async (error) => {
    const { response } = error;
    console.log('拦截的error', error)
    // if (response && response.data) {
    //   // 校验是否有 refresh_token
    //   const { authorization, clearToken, setToken } = useApp()
    //   if (!authorization || !authorization.refresh_token) {
    //     if (router.currentRoute.value.name === 'login') {
    //       return Promise.reject(error)
    //     }
    //     const redirect = encodeURIComponent(window.location.href)
    //     router.push(`/login?redirect=${redirect}`)
    //     // 清除token
    //     clearToken()
    //     setTimeout(() => {
    //       ElMessage.closeAll()
    //       try {
    //         ElMessage.error(error.response.data.msg)
    //       } catch (err) {
    //         ElMessage.error(error.message)
    //       }
    //     })
    //     return Promise.reject(error);
    //   }
    //   // 如果有refresh_token，则请求获取新的 token
    //   try {
    //     const res = await axios({
    //       method: 'PUT',
    //       url: '/api/authorizations',
    //       timeout: 10000,
    //       headers: {
    //         Authorization: `Bearer ${authorization.refresh_token}`,
    //       },
    //     })
    //     // 如果获取成功，则把新的 token 更新到容器中
    //     // console.log('刷新 token  成功', res)
    //     setToken({
    //       token: res.data.data.token, // 最新获取的可用 token
    //       refresh_token: authorization.refresh_token, // 还是原来的 refresh_token
    //     })
    //     // 把之前失败的用户请求继续发出去
    //     // config 是一个对象，其中包含本次失败请求相关的那些配置信息，例如 url、method 都有
    //     // return 把 request 的请求结果继续返回给发请求的具体位置
    //     return service(error.config)
    //   } catch (err) {
    //     // 如果获取失败，直接跳转 登录页
    //     // console.log('请求刷新 token 失败', err)
    //     const redirect = encodeURIComponent(window.location.href)
    //     router.push(`/login?redirect=${redirect}`)
    //     // 清除token
    //     clearToken()
    //     return Promise.reject(error)
    //   }
    // }
    // console.dir(error) // 可在此进行错误上报
    ElMessage.closeAll()
    try {
      ElMessage.error(error.response.data.msg)
    } catch (err) {
      ElMessage.error(error.message)
    }

    return Promise.reject(error)
  }

);


export default service;
