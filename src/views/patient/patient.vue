<script setup>
import { onMounted } from 'vue'
import { removePatient, getPatientList } from '@/api/user/patient'
import router from '@/router'
import { getUser, removeUser } from '@/utils/storage'
import { ElMessage } from 'element-plus'

onMounted(() => {
  fetchData()
})
const userInfo = ref(null)
// 点击添加按钮，跳转到添加页面
const addPatient = () => {
  userInfo.value = getUser('userinfo')
  console.log('userInfo', userInfo.value)
  if (userInfo.value.authStatus === 0) {
    ElMessage.warning('请先进行实名认证');
    router.push('/userauth');
    return
  }
  if (userInfo.value.authStatus === 1) {
    ElMessage.warning('您的认证正在审核中，请耐心等待');
    router.push('/patient');
    return
  }
  router.push('/add')
}
// 点击查看详情，跳转到详情页面
const toDetail = (id) => {
  router.push({ path: '/detail', query: { id: id } })
}

// =====================获取就诊人列表======================
// 定义数据列表 -> 就诊人列表
const patientList = ref([])
const getPatientListData = async () => {
  const { code, data } = await getPatientList()
  if (code === 200) {
    console.log('patient', data)
    patientList.value = data.patients
  }
}
// =====================删除就诊人信息======================
const delPatient = async (id) => {
  const { code } = await removePatient(id)
  if (code === 200) {
    // 刷新页面
    fetchData()
    // 提示信息
    ElMessage.success('删除成功')
  }
}
// =====================fetchData函数======================
const fetchData = () => {
  // 调用就诊人列表
  getPatientListData()

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
          <el-menu-item index="/userauth">
            <el-icon></el-icon>
            <span>实名认证</span>
          </el-menu-item>
          <el-menu-item index="/patient" style="background-color: rgb(236,245,255);">
            <el-icon>
              <Menu />
            </el-icon>
            <span>就诊人管理</span>
          </el-menu-item>
          <el-menu-item index="/order">
            <el-icon>
            </el-icon>
            <span>挂号订单</span>
          </el-menu-item>
        </el-menu>
      </el-col>
      <!-- 右侧页面 -->
      <el-col :span="20">
        <div style="width: 1200px;margin-left: 20px;" class="content">
          <div class="btn" style="margin-left: 15px;margin-top: 20px;">
            <span style="font-size: 20px;font-weight: 600;">就诊人管理</span>
          </div>
          <div class="patient-list">
            <el-card class="patient-card" shadow="always" v-for="(patient, index) in patientList" :key="index">
              <div style="width: 100%; ">
                <div style=" display: flex; align-items: center;">
                  <span style="margin-left: 15px;width: 100px;">{{ patient.name }}</span>
                  <span style="font-size: 13px;margin-left: 15px;">{{ patient.certificateType }} {{
        patient.certificateNo
      }}</span>
                  <span style="font-size: 13px; margin-left: auto;">
                    <el-button @click="toDetail(patient.id)" link>查看详情 ></el-button></span>
                </div>
                <el-divider />
                <div class="footer">
                  <div class="footer-left">自费</div>
                  <div class="footer-right">
                    <span>{{ patient.certificateNo }} {{ patient.certificateType }}</span>
                  </div>
                </div>
              </div>
            </el-card>
            <el-card style="margin: 10px;width: 978px;text-align: center;">
              <div>
                <el-button type="primary" link @click="addPatient"> + 添加就诊人</el-button>
              </div>
            </el-card>
          </div>
        </div>
      </el-col>
    </el-row>

  </div>
  <Footer />
</template>

<style scoped>
/* .tac {
  height: 545px;
} */

/* 就诊人列表样式 */
.patient-list {
  margin-top: 15px;
  margin-left: 5px;
  width: 1000px;
  /* display: flex; */
}

.patient-card {
  /* height: 100px; */
  margin: 10px;
  /* display: flex; */
  /* justify-content: center; */
  /* align-items: center; */
}

.card-content {
  display: flex;
}

/* 姓名 */
.title {
  display: inline-block;
  margin-left: 20px;
  align-content: center;
}

/* 证件信息 */
.patient-info {
  width: 400px;
  display: inline-block;
  margin-left: 60px;
}

/* 按钮组 */
.button-group {
  display: inline-block;
  margin-left: 50px;
  align-content: center;
}

/* 引入css */
.header-wrapper .title {
  font-size: 16px;
  margin-top: 0;
}

.content-wrapper {
  margin-left: 0;
}

.patient-card .el-card__header .detail {
  font-size: 14px;
}

.footer {
  width: 450px;
  border-radius: 5px;
  height: 50px;
  align-content: center;
  background-color: rgb(136, 202, 253);
}

.footer-left {
  display: inline-block;
  margin-left: 15px;
  border-radius: 3px;
  background-color: white;
  color: rgb(136, 202, 253);

}

.footer-right {
  display: inline-block;
  margin-left: 20px;
  color: white;
}
</style>
