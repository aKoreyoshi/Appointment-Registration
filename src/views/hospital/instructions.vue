<script setup>
import { onMounted } from 'vue'
import router from '@/router';
import { ElMessage } from 'element-plus';



onMounted(() => {
  fetchData()
})


// =====================fetchData函数======================
const fetchData = () => {

}


// 跳转
const toDetail = () => {
  router.push('/hospdetail')
  // 提示信息
  ElMessage.warning('请先选择科室')
}

</script>

<template>
  <Header></Header>
  <div>

    <el-row class="tac">
      <!-- 左侧导航栏 -->
      <el-col :span="4">
        <!-- <h5 class="mb-2">Default colors</h5> -->
        <el-menu :default-active="$route.path" class="el-menu-vertical-demo" :router="true">
          <el-menu-item index="/hospdetail">
            <el-icon></el-icon>
            <span>医院详情</span>
          </el-menu-item>
          <!-- <el-menu-item index="/instructions" style=" background-color: rgb(236,245,255);">
            <el-icon>
              <Menu />
            </el-icon>
            <span>预约须知</span>
          </el-menu-item> -->
          <el-menu-item index="" @click="toDetail">
            <el-icon>
            </el-icon>
            <span>预约挂号</span>
          </el-menu-item>
        </el-menu>
      </el-col>
      <!-- 右侧页面 -->
      <el-col :span="20">
        <div class="container">预约须知</div>
      </el-col>
    </el-row>

  </div>
  <Footer />
</template>

<style scoped>
.tac {
  height: 545px;
  /* border: 1px solid; */
}
</style>
