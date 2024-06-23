<script setup>
import { getUser, removeUser } from '@/utils/storage'
import { Phone } from '@element-plus/icons-vue';
import { ref, onMounted } from 'vue'
import { useApp } from '@/store/modules/app'
import { ElMessage } from 'element-plus'
import router from '@/router/index'
const userInfo = ref(null)

onMounted(() => {
  fetchData()
})
// 退出登录
const handleLogout = () => {
  // 清除token
  useApp().clearToken()
  // 删除用户信息
  removeUser('userinfo')
  fetchData()
  // 跳转到首页
  // window.location.href = '/login'
  router.push('/')
}

// 跳转
const toDetail = () => {
  router.push('/hospdetail')
  // 提示信息
  ElMessage.warning('请先选择科室')
}

const fetchData = () => {
  // 拿到用户信息
  userInfo.value = getUser('userinfo')
}
</script>

<template>
  <header>
    <div class="container">
      <!-- <div class="description">欢迎来到医疗服务预约平台</div> -->
      <div class="menu">
        <el-menu :ellipsis="false" :default-active="$route.path" class="el-menu-demo" mode="horizontal" :router="true">
          <el-menu-item class="custom-menu-item" index="/"
            style="font-size: 20px; font-weight: 600">欢迎来到医疗服务预约平台</el-menu-item>
          <el-sub-menu index="/hospdetail">
            <template #title>医院详情</template>
            <el-menu-item index="/hospdetail">医院详情</el-menu-item>
            <!-- <el-menu-item index="/instructions">预约须知</el-menu-item> -->
            <el-menu-item index="" @click="toDetail">预约挂号</el-menu-item>
          </el-sub-menu>
          <!-- <el-menu-item index="/user">预约挂号</el-menu-item> -->
          <!-- 新添加的菜单选项 -->
          <div class="new-menu-item">
            <el-dropdown>
              <span class="el-dropdown-link"
                style="width: 100px;height: 20px;display: flex;justify-content: center;align-items: center;">
                <div v-if="userInfo">
                  <span>{{ userInfo.phone }}</span>
                </div>
                <div v-if="!userInfo">
                  <!-- <router-link to="/login" class="link">{{ userInfo ? userInfo.phone : '登录' }}</router-link> -->
                  <router-link to="/login" class="link">登录</router-link>
                </div>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item style="display: flex; justify-content: center"><router-link to="/userauth"
                      class="link">个人中心</router-link></el-dropdown-item>
                  <el-dropdown-item style="display: flex; justify-content: center"><router-link class="link" to="/"
                      @click="handleLogout">退出</router-link></el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
            <!-- <image
              style="
                width: 45px;
                height: 45px;
                border-radius: 50%;
                margin-left: 30px;
              "
              src="../assets/images/github.png"
              :fit="cover"
            /> -->
            <!-- <el-image style="
                width: 45px;
                height: 45px;
                border-radius: 50%;
                margin-left: 30px;
              " src="../assets/images/github.png" :fit="cover" /> -->
          </div>
        </el-menu>
      </div>
    </div>
  </header>
</template>



<style scoped>
.container {
  display: flex;
  /* justify-content: center; 水平居中 */
  align-items: center;
  /* 垂直居中 */
  /* width: 100vw; 设置容器的宽度 */
  /* border-bottom: 1px solid ; */
}

.menu {
  width: 100%;
  /* flex-grow: 1; */
}

.custom-menu-item {
  background-color: transparent !important;
  /* 移除背景颜色 */
}

.custom-menu-item:hover {
  background-color: transparent !important;
  /* 鼠标悬停时不添加背景颜色 */
}

.custom-menu-item:focus {
  background-color: transparent !important;
  /* 点击时不添加背景颜色 */
}

.new-menu-item {
  margin-left: 920px;
  /*将新菜单项推到最右边 */
  display: flex;
  /* 使用 flexbox 布局 */
  justify-content: center;
  /* 水平居中 */
  align-items: center;
  /* 垂直居中 */
  height: 100%;
  /* 设置容器的高度 */
  /* border: 1px solid; 添加边框 */
}

.link {
  text-decoration: none;
  /* 去除下划线 */
  color: inherit;
  /* 继承父元素的文字颜色 */
  cursor: pointer;
  /* 显示鼠标指针 */
  outline: none;
  /* 移除点击时的默认轮廓样式 */
}

.example-showcase .el-dropdown-link {
  cursor: pointer;
  color: var(--el-color-primary);
  display: flex;
  align-items: center;
}

.el-dropdown-link:focus,
.el-dropdown-link:active {
  outline: none;
  /* 移除边框 */
}
</style>
