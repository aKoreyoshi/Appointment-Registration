<script setup>
import { onMounted } from 'vue'
import { updatePatient, removePatient, getPatientById } from '@/api/user/patient'
import router from '@/router'
import moment from 'moment';
import { ElMessage, ElMessageBox } from 'element-plus'


onMounted(() => {
  fetchData()
})
// =====================详情信息数据格式定义======================
const patientInfo = ref({})
const brithday = ref('')

// =====================删除就诊人信息======================
const handleDeletePatient = async (id) => {
  ElMessageBox.confirm('确认要永久删除该就诊人信息吗?', 'warning', {
    cancelButtonText: '取消',
    confirmButtonText: '确定',
    type: 'warning',
  }).then(async () => {
    const { code } = await removePatient(id)
    if (code === 200) {
      // 提示信息
      ElMessage.success('删除成功')
      router.push('/patient')
    }
  })


}

// =====================fetchData函数======================
const fetchData = async () => {
  // 获取当前就诊人信息
  // 先拿到传进来的id
  const id = router.currentRoute.value.query.id
  // 调用api接口
  const { code, data } = await getPatientById(id)
  if (code === 200) {
    console.log('data', data)
    patientInfo.value = data.patient
  }
  // 由于日期格式的问题，这里需要做一下转换
  // 使用 moment.js 解析日期
  const date = moment(patientInfo.value.birthday);
  // 格式化日期成 yyyy-MM-dd 格式
  brithday.value = date.format('YYYY-MM-DD');

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
        <div class=" title">
          <span>就诊人详情</span>
        </div>
        <div class="content">
          <div class="content-title">
            <div style="width: 5px;background-color: #79bbff;height:22px;display: inline-block;margin: 0;"></div>
            <div style=" margin: 0;margin-left: 5px; display: inline-block;color: #C0C4CC;">
              就诊人信息
            </div>
          </div>
          <div class="content-detail">
            <el-form :model="patientInfo" style="max-width: 600px;" label-width="100px">
              <el-form-item label="姓名：">{{ patientInfo.name }}</el-form-item>
              <el-form-item label="证件类型：">{{ patientInfo.certificateType }}</el-form-item>
              <el-form-item label="证件号码：">{{ patientInfo.certificateNo }}</el-form-item>
              <el-form-item label="性别：">{{ patientInfo.gender == 0 ? '男' : '女' }}</el-form-item>
              <el-form-item label="出生日期：">{{ brithday }}</el-form-item>
              <el-form-item label="民族：">{{ patientInfo.ethnicGroup }}</el-form-item>
              <el-form-item label="当前住址：">{{ patientInfo.district }}</el-form-item>
              <el-form-item label="详细地址：">{{ patientInfo.address }}</el-form-item>
              <el-button style="margin-left: 50px;" type="primary"
                @click="handleDeletePatient(patientInfo.id)">删除就诊人</el-button>
              <!-- <el-button>修改就诊人</el-button> -->
            </el-form>
          </div>
        </div>
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

.title {
  margin-top: 10px;
  margin-left: 20px;
  font-size: 17px;
  font-weight: 600;
}

/* content样式 */
.content {
  margin-top: 20px;
  margin-left: 20px;
  height: 500px;
}

.content-title {
  /* height: 30px; */
  display: flex;
  align-items: center;
}

.content-detail {
  margin-left: 40%;
}
</style>
