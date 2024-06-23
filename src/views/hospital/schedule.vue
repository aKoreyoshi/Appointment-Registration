<script setup>
import '../../assets/css/hospital.css'
import { onMounted, ref } from 'vue'
import router from '@/router'
import { getBookingScheduleRule, findScheduleList, getHospitalInfo } from '@/api/hosp/hospital'
import { ElMessage } from 'element-plus';

onMounted(() => {
  getHospInfo()
  fetchData()
})

// =====================定义数据======================
const currentPage = ref(1)
const pageSize = ref(7)
const total = ref(0)
const bookingScheduleList = ref([])
const rule = ref({})
const departmentCode = ref('')
const scheduleList = ref([])
const workDate = ref(null) // 当前日期
const status = ref(0) // 0 无号 -1 停止挂号 1 即将放号

const nextWorkDate = ref(null) // 下一页第一个日期
const preWorkDate = ref(null) // 上一页第一个日期
const tabShow = ref(true) // 切换挂号列表与即将挂号切换
const activeIndex = ref(0) // 当前选中的日期索引
const pageFirstStatus = ref(0) // 页面第一个日期状态 0 无号 -1 停止挂号 1 即将放号
const hospInfo = ref({})
// 获取医院信息
const getHospInfo = async () => {
  const { code, data } = await getHospitalInfo()
  console.log('data', data)
  if (code === 200) {
    hospInfo.value = data
  }
}

// 获取当前时间
function getCurrentTime() {
  const now = new Date()
  const hours = String(now.getHours()).padStart(2, '0');
  const minutes = String(now.getMinutes()).padStart(2, '0');
  return `${hours}:${minutes}`;
}

// 在您的响应式数据定义区域添加
const activeDate = ref(null);
// 定义一个方法来确定是否激活状态
const isActive = (workDate) => {
  return workDate === activeDate.value;
};
// =====================selectDate函数======================
const selectDate = async (date) => {
  activeDate.value = date; // 更新当前选中的日期
  // 根据日期拿到当前日期对应的数据
  const bookingRule = bookingScheduleList.value.find(function (item) {
    return item.workDate == date
  })
  // 调用查询排班api
  const { code, data } = await findScheduleList(departmentCode.value, date)
  // console.log(data)
  if (code === 200) {
    scheduleList.value = data
  }
  // 给status赋值
  status.value = bookingRule.status
}
// =====================分页getPage函数======================
const getPage = async (page) => {
  currentPage.value = page
  workDate.value = null
  activeIndex.value = 0
  fetchData()
}
// =====================booking函数进行预约=======================
const booking = (scheduleId) => {
  // 判断医院状态
  if (hospInfo.value.hospitalSet.status === 1) {
    ElMessage.warning('医院状态暂时被锁定，停止预约')
    return
  }
  router.push({ path: '/booking', query: { scheduleId: scheduleId } })

}

// =====================fetchData函数======================
const fetchData = async () => {
  // 先拿到传进来的科室编号
  departmentCode.value = router.currentRoute.value.query.departmentCode
  // 调用api接口
  const { code, data } = await getBookingScheduleRule(
    currentPage.value, pageSize.value, departmentCode.value
  )
  // 赋值
  bookingScheduleList.value = data.scheduleRuleVoList
  total.value = data.total
  rule.value = data.rule
  // 默认调用第一个排班
  selectDate(data.scheduleRuleVoList[0].workDate)
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
          <el-menu-item @click="toDetail" style=" background-color: rgb(236,245,255);">
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
          <div>
            <div style="display: flex;flex-direction: column;">
              <div style="margin-top: 10px;">
                <span class=" v-link clickable" style="margin-left: 15px;margin-top: 10px;" @click="show">{{
        rule.hospitalName }}</span>
                <el-divider style="margin-top: 10px;" direction="vertical" />
                <span style="margin-top: 10px;">{{ rule.bigDepartmentName }}</span>
              </div>
              <div style="margin-left: 15px;margin-top: 15px;font-size: 17px;font-weight: 600;">
                {{ rule.departmentName }}
              </div>
            </div>
            <div class="mt60" style="width: 1100px;">
              <div class="title-wrapper" style="margin-top: 5px;">{{ rule.workDateString }}</div>
              <div class="calendar-list-wrapper" style="display: flex; justify-content: center;">
                <!-- <div :class="['calendar-item', item.curClass, { 'active': activeDate.value == item.workDate }]" -->
                <div class="calendar-item" :class="{ active: isActive(item.workDate) }"
                  style="width: 124px;margin-left: 10px;" v-for="(item, index) in bookingScheduleList" :key="index"
                  @click="selectDate(item.workDate)">
                  <div class="date-wrapper"><span>{{ item.workDate }}</span><span class="week">{{ item.dayOfWeek
                      }}</span></div>
                  <div class="status-wrapper" v-if="item.status == 0">{{ item.availableNumber == -1 ? '无号' :
        item.availableNumber == 0 ? '约满' : '有号' }}</div>
                  <div class="status-wrapper" v-if="item.status == 1">即将放号</div>
                  <div class="status-wrapper" v-if="item.status == -1">停止挂号</div>
                </div>
              </div>
              <div class="pagination-wrapper" style="display: flex; justify-content: center; margin-top: 10px;">
                <el-pagination class="pagination" layout="prev, pager, next" :current-page="currentPage" :total="total"
                  :page-size="pageSize" @current-change="getPage">
                </el-pagination>
              </div>
            </div>
            <div class="mt60" v-if="tabShow">
              <div class="">
                <div class="list-title">
                  <div class="block"></div>
                  上午号源
                </div>
                <div class="am-list" v-for="schedule in scheduleList" :key="schedule.scheduleId">
                  <div class="list-item" v-if="schedule.workTime == 0">
                    <div class="doc-title" style="display: flex;flex-direction: column; width: 200px;">
                      <div class="doc-name"
                        style="height: 35px;display: flex;align-items: center;justify-content: center;">
                        <span> {{ schedule.doctorTitle }} &nbsp;&nbsp;{{ schedule.doctorName }} </span>
                      </div>
                      <div class="doc-department"
                        style="height: 35px;display: flex;align-items: center;justify-content: center;">
                        <span style="color: #999;">{{ schedule.param.departmentName }}</span>
                      </div>
                    </div>
                    <div class="fee"
                      style="margin-left: 550px;width: 100px;display: flex;align-items: center;justify-content: center;">
                      <span style="font-size: 13px;color: rgb(96,174,255);">￥{{ schedule.registrationFee }}</span>
                    </div>
                    <div class="available-number" style="display: flex;align-items: center;justify-content: center;">
                      <el-button v-if="status === 0" @click="booking(schedule.scheduleId)" type="primary"
                        style="width: 100px;">剩余 {{
        schedule.availableNumber }}</el-button>
                      <el-button v-if="status === -1" type="primary" style="width: 100px;" disabled>停止挂号</el-button>
                      <el-button v-if="status === 1" type="primary" style="width: 100px;" disabled>即将放号</el-button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="mt60" v-if="tabShow">
              <div class="">
                <div class="list-title">
                  <div class="block"></div>
                  下午号源
                </div>
                <div class="am-list" v-for="schedule in scheduleList" :key="schedule.scheduleId">
                  <div class="list-item" v-if="schedule.workTime == 1">
                    <div class="doc-title" style="display: flex;flex-direction: column; width: 200px;">
                      <div class="doc-name"
                        style="height: 35px;display: flex;align-items: center;justify-content: center;">
                        <span> {{ schedule.doctorTitle }} &nbsp;&nbsp;{{ schedule.doctorName }} </span>
                      </div>
                      <div class="doc-department"
                        style="height: 35px;display: flex;align-items: center;justify-content: center;">
                        <span style="color: #999;">{{ schedule.param.departmentName }}</span>
                      </div>
                    </div>
                    <div class="fee"
                      style="margin-left: 550px;width: 100px;display: flex;align-items: center;justify-content: center;">
                      <span style="font-size: 13px;color: rgb(96,174,255);">￥{{ schedule.registrationFee }}</span>
                    </div>
                    <div class="available-number" style="display: flex;align-items: center;justify-content: center;">
                      <el-button v-if="status === 0" @click="booking(schedule.scheduleId)" type="primary"
                        style="width: 100px;">剩余 {{
        schedule.availableNumber }}</el-button>
                      <el-button v-if="status === -1" type="primary" style="width: 100px;" disabled>停止挂号</el-button>
                      <el-button v-if="status === 1" type="primary" style="width: 100px;" disabled>即将放号</el-button>
                    </div>
                  </div>
                </div>
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
.tac {
  height: 800px;
}


.calendar-item.active {
  border: 2px solid #409eff;
  /* 举例使用 Element UI 的主题蓝色 */
  box-sizing: border-box;
  /* 您可以添加其他样式来区分选中的日期，例如背景色、边框颜色等 */
  border-radius: 5px;
}



.hospital-source-list .list-item {
  width: auto;
  height: auto;
  color: #333;
  background: none;
  display: inline;
  vertical-align: top;
  font-weight: 700;
  padding: 0;
  line-height: inherit;
  border-radius: 4px;
  cursor: default;
  /* margin-top: 40px; */
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-pack: justify;
  -ms-flex-pack: justify;
  justify-content: space-between;
}

.hospital-source-list .list-item:hover {
  border: 0;
}

.header-wrapper {
  position: fixed;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
}

/* 上午号源 */
.list-item {
  display: flex;
  margin-left: 50px;
  /* margin-top: 5px; */
  /* height: 100px; */
  width: 80%;
  height: 80px;
}
</style>
