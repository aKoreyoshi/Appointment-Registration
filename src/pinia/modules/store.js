 // 定义store.js来获取用户信息

import Vue from 'vue'
import Vuex from 'vuex'
 
Vue.use(Vuex)
 
const store = new Vuex.Store({
  state: {
    userInfo: null // 用户信息
  },
  mutations: {
    setUserInfo(state, userInfo) {
      state.userInfo = userInfo
    }
  },
  actions: {
    getUserInfo({ commit }) {
      // 模拟异步请求获取用户信息
      setTimeout(() => {
        const userInfo = { name: 'John', age: 25 } // 假设从后端获取的用户信息
        commit('setUserInfo', userInfo)
      }, 1000)
    }
  }
})
 
export default store