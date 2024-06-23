/*
 * @Descripttion:
 * @version:
 * @Date: 2021-04-20 11:06:21
 * @LastEditors: huzhushan@126.com
 * @LastEditTime: 2022-09-27 14:57:06
 * @Author: huzhushan@126.com
 * @HomePage: https://huzhushan.gitee.io/vue3-element-admin
 * @Github: https://github.com/huzhushan/vue3-element-admin
 * @Donate: https://huzhushan.gitee.io/vue3-element-admin/donate/
 */
import { defineStore } from 'pinia'
import { GetUserinfo } from '@/api/user/user'
import { setUser, removeUser } from '@/utils/storage'

export const useAccount = defineStore('account', {
  state: () => ({
    // userinfo: {
    //   name: '',
    //   phone: '',
    //   certificatesType: '',
    //   certificatesNo: ''
    // },
    // userinfo: null,
    user: {
      name: '',
      phone: '',
      certificateType: '',
      certificateNo: '',
      status: '',
      authStatus: ''
    },
    permissionList: [],
  }),
  actions: {
    // 清除用户信息
    clearUserinfo() {
      // this.userinfo = null,
      this.user = {
        name: '',
        phone: '',
        certificateType: '',
        certificateNo: '',
        status: '',
        authStatus: ''
      },
        removeUser()
    },
    // 获取用户信息
    async getUserinfo() {
      const { code, data } = await GetUserinfo()
      if (code === 200) {
        console.log('userinfo', data)
        // if (data.name === '') {
        //   this.userinfo = null
        // } else {
        // this.userinfo = data.name
        this.user.name = data.name
        this.user.phone = data.phone
        this.user.certificateType = data.certificateType
        this.user.certificateNo = data.certificateNo
        this.user.status = data.status
        this.user.authStatus = data.authStatus
        setUser('userinfo', this.user)
        // }
        // return Promise.resolve(data)
        return data
      }
    },
  },
})
