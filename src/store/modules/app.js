/*
 * @Descripttion:
 * @Author: macong
 */

import { defineStore } from 'pinia'
import { getItem, setItem, removeItem } from '@/utils/storage' //getItem和setItem是封装的操作localStorage的方法
import { useAccount } from './account'
export const TOKEN = 'token'

export const useApp = defineStore('app', {
  state: () => ({
    title: 'Vue3 Element Admin',
    authorization: getItem(TOKEN),
    device: 'desktop',
  }),
  actions: {
    setDevice(device) {
      this.device = device
    },
    setToken(data) {
      this.authorization = data
      // 保存到localStorage
      setItem(TOKEN, data)
    },
    initToken(data) {
      this.clearToken()
      this.setToken(data)
      useAccount().getUserinfo()
    },
    clearToken() {
      // 清除token
      this.authorization = ''
      removeItem(TOKEN)
      // 清除用户信息
      useAccount().clearUserinfo()
    },
  }
})
