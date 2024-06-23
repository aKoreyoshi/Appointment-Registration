<script setup>
import { onMounted } from 'vue'
import { getHospitalInfo, getDeptList } from '@/api/hosp/hospital'
import router from '@/router'
import { ElMessage } from 'element-plus'


onMounted(() => {
  fetchData()
})

// =====================定义数据格式======================
const hospitalInfo = ref({})
const bookingRule = ref({})
const rules = ref([])
// 部门列表
const departmentList = ref([])
const parent = ref(null) // 父级部门
// 小部门数据
const subDepartmentList = ref([])
// 表示当前部门是否被选中
const selectedIndex = ref(0)
// ===================判断当前部门是否被选中=======================
// 获取小部门数据
const handleClick = (id) => {
  parent.value = departmentList.value[id].departmentName
  subDepartmentList.value = departmentList.value[id].children
  selectedIndex.value = id
}

// =====================处理预约规则字符串======================
function splitString(str) {
  const parts = str.split('；');
  return parts;
}
// =====================跳转到挂号页面======================
const toSchedule = async (departmentCode) => {
  // console.log(departmentCode)
  router.push({ path: '/schedule', query: { departmentCode: departmentCode } })
}

// =====================fetchData函数======================
const fetchData = async () => {
  const { code, data } = await getHospitalInfo()
  if (code === 200) {
    hospitalInfo.value = data.hospitalSet
    bookingRule.value = data.bookingRule
  }
  rules.value = splitString(bookingRule.value.rule)
  // 处理部门数据
  console.log(await getDeptList())
  departmentList.value = (await getDeptList()).data
  // 设置默认选中专科
  parent.value = departmentList.value[0].departmentName
  subDepartmentList.value = departmentList.value[0].children
  console.log(subDepartmentList.value)
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
          <el-menu-item index="/hospdetail" style="background-color: rgb(236,245,255);">
            <el-icon>
              <Menu />
            </el-icon>
            <span>医院详情</span>
          </el-menu-item>
          <!-- <el-menu-item index="/instructions">
            <el-icon></el-icon>
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
        <div class="container">
          <div class="hospital">
            <div class="hospital-name">
              <span style="font-size: 20px; font-weight:600 ;">
                {{ hospitalInfo.hospitalName }}
              </span>
              <span style="color: #CCCCCC;margin-left: 20px">
                <el-icon>
                  <office-building />
                </el-icon>
                {{ hospitalInfo.hospitalGrade }}
              </span>
            </div>
            <div class="hospital-content">
              <el-row style="width: 1100px">
                <el-col :span="4">
                  <div class="hospital-img" style="flex: 1;height: 200px;text-align: center;">
                    <img :src="'data:image/jpeg;base64,' + hospitalInfo.logoData" width="100" />
                  </div>
                </el-col>
                <el-col :span="20">
                  <el-descriptions title="挂号规则" style="margin-left: 15px;">
                    <el-descriptions-item label="预约周期：" style="font-weight: 600;">{{ bookingRule.cycle
                      }}天</el-descriptions-item>
                    <el-descriptions-item label="放号时间：">{{ bookingRule.releaseTime }}</el-descriptions-item>
                    <el-descriptions-item label="当天停挂时间：">{{ bookingRule.stopTime }}</el-descriptions-item>
                    <el-descriptions-item label="退号时间：">
                      <span v-if="bookingRule.quitDay == -1">就诊前一工作日{{ bookingRule.quitTime }}前取消</span>
                      <span v-if="bookingRule.quitDay == 0">就诊当天前取消</span>
                    </el-descriptions-item>
                  </el-descriptions>
                  <el-descriptions title="医院取号地点" style="margin-left: 15px;margin-top: 10px; display: block;">
                    <el-descriptions-item label="1" style="display: block;clear: both;">{{ rules[0]
                      }}</el-descriptions-item>
                    <el-descriptions-item label="2" style="display: block;clear: both;">{{ rules[1]
                      }}</el-descriptions-item>
                  </el-descriptions>
                </el-col>
              </el-row>
            </div>
            <div class="department-content">
              <el-card class="card-left" style="width: 250px;">
                <div class="title" style="font-size: 18px;font-weight: bold;margin-bottom: 10px;">科室列表</div>
                <div class="bigDepartmentList" v-for="(item, index) in departmentList" :key="index">
                  <div class="departmentName" :class="{ selected: selectedIndex === index }"
                    @click="handleClick(index)">{{
        item.departmentName }}</div>
                </div>
              </el-card>
              <el-card class="card-right" style="width: 900px;margin-left: 10px">
                <div class="departmentName-title" style="align-items: center;">
                  <div style="display: flex;align-items: center;">
                    <div
                      style="width: 5px;background-color: #79bbff;height:22px;display: inline-block;border-radius: 5px;">
                    </div>
                    <div style=" margin: 0;margin-left: 7px; display: inline-block;font-weight: 600;">
                      {{ parent }}
                    </div>
                  </div>
                  <div style=" display: flex; flex-wrap: wrap; margin-top: 30px;margin-left: 15px;">
                    <div v-for="child in subDepartmentList" :key="child.id"
                      style="flex-basis: calc(100% / 3);box-sizing: border-box;height: 30px;">
                      <span @click="toSchedule(child.departmentCode)">{{ child.departmentName }}</span>
                    </div>
                  </div>
                </div>
              </el-card>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
  <Footer />
</template>

<style scoped>
/* container父容器 */
.hospital {
  margin-top: 10px;
  margin-left: 15px;
}

/* 预约详情 */
.hospital-name {
  display: flex;
  align-items: center;
}

.hospital-content {
  margin-top: 30px;
  display: flex;
  flex-direction: row;
  /* border: 1px solid; */
}

.department-content {
  margin-top: 10px;
  display: flex;
}

/* 科室名称 */
.departmentName {
  height: 35px;
  display: flex;
  justify-content: center;
  /* 水平居中 */
  align-items: center;
  /* 垂直居中 */
}

.departmentName.selected {
  /* 选中时的样式 */
  background-color: #F2F6FC;
}
</style>
