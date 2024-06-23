<script setup>
import '../../assets/css/hospital.css'
import { onMounted, ref } from 'vue'
import router from '@/router'
import { getScheduleById } from '@/api/hosp/hospital'
import { getPatientList } from '@/api/user/patient'
import { generateOrder } from '@/api/order/order'
import { ElMessage } from 'element-plus';
import { getUser } from '@/utils/storage'


onMounted(() => {
  // 先拿到传进来的排班id
  scheduleId.value = router.currentRoute.value.query.scheduleId
  console.log('scheduleId', scheduleId.value)
  fetchData()
})

// =====================定义数据======================
const scheduleId = ref(null)  // 排班id
const schedule = ref({
  param: {}
})  // 排班对象
const patientList = ref([])  // 病人  列表
const patient = ref({})  // 病人对象
const activeIndex = ref(0)  // 当前选择的病人索引
const submitBnt = ref('确认挂号')  // 提交按钮状态

// =====================选择就诊人======================
const selectPatient = (index) => {
  activeIndex.value = index
  patient.value = patientList.value[index]
  console.log('patient', patient.value)
}

// =====================预约下单======================
const submitOrder = async () => {
  if (patient.value.id === null) {
    ElMessage.warning('请选择就诊人')
    return
  }
  // 先拿到用户信息做一个判断
  const userinfo = getUser('userinfo')
  console.log('认证状态', userinfo.authStatus)
  if (userinfo.authStatus === 0) {
    // 说明用户未认证
    ElMessage.warning('请先完成用户认证')
    return
  }
  if (userinfo.authStatus === 1) {
    // 说明用户认证正在审核中，不能预约
    ElMessage.warning('请等待认证审核完成')
    return
  }
  if (submitBnt.value === '正在提交...') {
    ElMessage.warning('请勿重复提交')
    return
  }
  submitBnt.value = '正在提交...'
  // console.log('pateintId', patient.value.id, "scheduleId", scheduleId.value)
  const { code, data } = await generateOrder(scheduleId.value, patient.value.id)
  if (code === 200) {
    ElMessage.success('预约成功')
    let orderNo = data
    // 跳转到订单详情页面
    router.push({
      path: '/orderdetail',
      query: {
        orderNo: orderNo
      }
    })
    submitBnt.value = '确认挂号'
  }
}


// =====================fetchData函数======================
const fetchData = async () => {
  // 调用api接口
  const { code, data } = await getScheduleById(scheduleId.value)
  // console.log('schedule', data)
  if (code === 200) {
    // 给排班对象赋值
    schedule.value = data
  }
  // 获取就诊列表
  // patientList.value = (await getPatientList()).data.patients
  await GetPatientList()
  // console.log('patientList', patientList.value)
  // 默认选中第一个病人
  selectPatient(0)
}


// 获取就诊人列表
const GetPatientList = async () => {
  const { code, data } = await getPatientList()
  if (code === 200) {
    patientList.value = data.patients
  }
}

// 添加就诊人
const addPatient = () => {
  router.push('/add')
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
          <!-- <el-menu-item index="/instructions">
            <el-icon>
            </el-icon>
            <span>预约须知</span>
          </el-menu-item> -->
          <el-menu-item style=" background-color: rgb(236,245,255);" @click="toDetail">
            <el-icon>
              <Menu />
            </el-icon>
            <span>预约挂号</span>
          </el-menu-item>
        </el-menu>
      </el-col>
      <!-- 右侧页面 -->
      <el-col :span="20">
        <div class="page-container">
          <div class="title" style="font-size: 20px;margin-top: 5px;margin-left: 10px;">确认挂号信息</div>
          <div class="patient-list">
            <div style="display: flex;align-items: center;margin-top: 30px;margin-left: 60px;">
              <div
                style="width: 5px;background-color: rgb(73,144,241);height:22px;display: inline-block;border-radius: 5px;">
              </div>
              <div style=" margin: 0;margin-left: 5px; display: inline-block;font-weight: 600;">
                选择就诊人:
              </div>
            </div>

            <div class="patient-wrapper" style="margin-top: 20px;margin-left: 70px;">
              <div>
                <div class="v-card clickable item">
                  <div class="inline" v-for="(item, index) in patientList" :key="item.id" @click="selectPatient(index)"
                    style="margin-right: 10px">
                    <!-- 选中 selected  未选中去掉selected-->
                    <div :class="activeIndex == index ? 'item-wrapper selected' : 'item-wrapper'">
                      <div>
                        <div class="item-title">{{ item.name }}</div>
                        <div>{{ item.certificateType }}</div>
                        <div>{{ item.certificateNo }}</div>
                      </div>
                      <img src="//img.114yygh.com/static/web/checked.png" class="checked" />
                    </div>
                  </div>
                </div>
              </div>
              <div class="item space add-patient v-card clickable">
                <div class="">
                  <div class="item-add-wrapper" @click="addPatient">
                    + 添加就诊人
                  </div>
                </div>
              </div>
              <div class="el-loading-mask" style="display: none">
                <div class="el-loading-spinner">
                  <svg viewBox="25 25 50 50" class="circular">
                    <circle cx="50" cy="50" r="20" fill="none" class="path"></circle>
                  </svg>
                </div>
              </div>
            </div>
            <!-- 就诊人，选中显示 -->
            <div class="sub-title" v-if="patientList.length > 0" style="margin-top: 30px;margin-left: 60px;">
              <div class="block"></div>
              选择就诊卡：
              <span class="card-tips"><span class="iconfont"></span>
                如您持社保卡就诊，请务必选择医保预约挂号，以保证正常医保报销</span>
            </div>

            <el-card style="width: 900px;margin-left: 70px;margin-top: 15px;display: flex; flex-direction: column;"
              class="patient-card" shadow="always" v-if="patientList.length > 0">
              <div class="clearfix">
                <div>
                  <span class="name">
                    {{ patient.name }}
                    {{ patient.certificateNo }} 居民身份证</span>
                </div>
              </div>
              <div class="card SELF_PAY_CARD" style="margin-left: 90px;">
                <div class="info">
                  <span class="type">自费</span>
                  <span class="card-no">{{ patient.certificateNo }}</span><span class="card-view">居民身份证</span>
                </div>
                <span class="operate"></span>
              </div>
            </el-card>

            <div class="sub-title" style="margin-left: 60px;margin-top: 30px">
              <div class="block"></div>
              挂号信息
            </div>
            <div class="content-wrapper" style="margin-top: 10px;margin-left: 350px;">
              <el-form ref="form">
                <el-form-item label="就诊日期：">
                  <div class="content">
                    <span style="color: black">{{ schedule.workDate }}</span>
                    <span style="color: black">&nbsp;&nbsp;{{ schedule.param.dayOfWeek
                      }}</span>
                    <span style="color: black">&nbsp;&nbsp;{{ schedule.workTime == 0 ? "上午" : "下午" }}</span>
                  </div>
                </el-form-item>
                <el-form-item label="就诊医院：">
                  <div class="content">
                    <span style="color: black">{{ schedule.param.hospitalName }}</span>
                  </div>
                </el-form-item>
                <el-form-item label="就诊科室：">
                  <div class="content">
                    <span style="color: black">{{ schedule.param.departmentName }} </span>
                  </div>
                </el-form-item>
                <el-form-item label="医生姓名：">
                  <div class="content">
                    <span style="color: black">{{ schedule.doctorName }} </span>
                  </div>
                </el-form-item>
                <el-form-item label="医生职称：">
                  <div class="content">
                    <span style="color: black">{{ schedule.doctorTitle }} </span>
                  </div>
                </el-form-item>
                <el-form-item label="医生专长：">
                  <div class="content">
                    <span style="color: black">{{ schedule.doctorSkill }}</span>
                  </div>
                </el-form-item>
                <el-form-item label="医师服务费：">
                  <div class="content">
                    <div class="fee">{{ schedule.registrationFee }}元</div>
                  </div>
                </el-form-item>
              </el-form>
            </div>

            <!-- 用户信息 #start-->
            <div>
              <div class="sub-title" style="margin-left: 60px;margin-top: 15px;">
                <div class="block"></div>
                用户信息
              </div>
              <div class="content-wrapper" style="margin-top: 10px;margin-left: 350px;">
                <el-form ref="form" :model="form">
                  <el-form-item class="form-item" label="就诊人手机号：">
                    <span style="color: black;">{{ patient.phone }}</span>
                  </el-form-item>
                </el-form>
              </div>
            </div>
            <!-- 用户信息 #end -->
            <div class="bottom-wrapper">
              <div class="button-wrapper">
                <el-button style="width: 100px;margin-left: 90px;" type="primary" @click="submitOrder">
                  {{ submitBnt }}
                </el-button>
              </div>
            </div>

          </div>
        </div>
      </el-col>
    </el-row>
  </div>
  <Footer />
</template>

<style scoped>
/* .tac {
  height: 800px;
} */
</style>
