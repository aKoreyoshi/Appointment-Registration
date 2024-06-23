<script setup>
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'
import { onMounted, ref, onBeforeUnmount } from 'vue'
import { getDeptList, getHospitalInfo, getWeatherInfo } from '@/api/hosp/hospital'
import router from '@/router'
import { useAccount } from '@/store/modules/account'
import Banner from '@/assets/images/banner.png'

onMounted(() => {
  // 页面刷新先更新用户信息
  useAccount().getUserinfo()
  // 获取天气信息
  getWeather()
  fetchData()
})
// =================数据定义=================
// 设置轮播图
const imageList = [
  '@/assets/imaages/pic.jpg',
  'image1.jpg',
  'image2.jpg',
  'image3.jpg',
];
// 公告信息
const tableData = [
  {
    msg: '重要通知：医疗服务预约平台上线通知',
  },
  {
    msg: '关于平台功能更新的公告',
  },
  {
    msg: '关于节假日预约安排的通知',
  },
]
const tableData2 = [
  {
    msg: '停诊公告：儿科科室停诊尊敬的家长们，为了进行例行维护和设备检修，本院儿科科室将于2024-05-03停诊一天。停诊期间，原定于儿科科室的所有预约将被取消。如果您的孩子需要医疗服务，请前往其他医院或就近诊所就诊。给您带来的不便，我们深感抱歉。',
  },
  // {
  //   msg: '首都医科大学附属北京潞河医院老年医学科门诊停诊公告',
  // },
  // {
  //   msg: '中日友好医院中西医结合心内科门诊停诊公告',
  // },
]
// 部门列表
const departmentList = ref([])
const parent = ref(null) // 父级部门
// 小部门数据
const subDepartmentList = ref([])
// 表示当前部门是否被选中
const selectedIndex = ref(0)
const weahter = ref({
  weather: '',
  dayTemp: '',
  nightTemp: '',
  windDirection: '',
}) // 天气信息

// ===================判断当前部门是否被选中=======================
// 获取小部门数据
const handleClick = (id) => {
  parent.value = departmentList.value[id].departmentName
  subDepartmentList.value = departmentList.value[id].children
  selectedIndex.value = id
}

// =====================跳转到挂号页面======================
const toSchedule = async (departmentCode) => {
  // console.log(departmentCode)
  router.push({ path: '/schedule', query: { departmentCode: departmentCode } })
}

const fetchData = async () => {
  const { code, data } = await getDeptList()
  if (code === 200) {
    departmentList.value = data
  }
  // 设置默认选中专科
  parent.value = departmentList.value[0].departmentName
  subDepartmentList.value = departmentList.value[0].children
}
// 当前时间
function getCurrentTime() {
  const currentDateTime = ref(new Date().toLocaleString()); // 初始化当前日期和时间

  const timer = setInterval(() => {
    currentDateTime.value = new Date().toLocaleString(); // 每秒刷新当前日期和时间
  }, 1000);

  onBeforeUnmount(() => {
    clearInterval(timer); // 在组件销毁前清除定时器，避免内存泄漏
  });

  return { currentDateTime };
}
const currentTime = getCurrentTime()
// ===================获取天气信息======================
const getWeather = async () => {
  const { code, data } = await getWeatherInfo()
  const nowData = data.data.now
  if (code === 200) {
    weahter.value.weather = nowData.city.weather
    weahter.value.dayTemp = nowData.city.day_air_temperature
    weahter.value.nightTemp = nowData.city.night_air_temperature
    weahter.value.windDirection = nowData.city.wind_direction
  }
  console.log('weahter', weahter.value)
}
</script>

<template>
  <Header />
  <div class="container">
    <div class="left">
      <!-- 头部信息，包含天气和轮播图等 -->
      <div class="head-left">
        <div class="weather">
          <el-card style="height: 220px">
            <p>尊敬的用户您好！欢迎您使用医疗服务预约平台！</p>
            <p>当前时间：{{ currentTime.currentDateTime.value }}</p>
            <p>今天天气{{ weahter.weather }}，最低气温为{{ weahter.nightTemp }}度，最高气温为{{ weahter.dayTemp }}度，
              {{ weahter.windDirection }}</p>
          </el-card>

        </div>
        <!-- 轮播图 -->
        <div class="carousel">
          <!-- <el-carousel :interval="4000" type="card" height="100px">
            <el-carousel-item v-for="item in 3" :key="item">
              <el-image src="/src/assets/images/banner.png" :fit="cover" />
            </el-carousel-item>
            src\assets\images\banner.png
          </el-carousel> -->
          <el-carousel height="200px">
            <el-carousel-item v-for="item in 3" :key="item">
              <el-image :src="Banner" :fit="cover" />
            </el-carousel-item>
          </el-carousel>
        </div>
      </div>
      <!-- 内容 -->
      <div class="content-left">
        <el-card class="card-left" style="width: 250px;">
          <div class="title" style="font-size: 18px;font-weight: bold;margin-bottom: 10px;">科室列表</div>
          <div class="bigDepartmentList" v-for="(item, index) in departmentList" :key="index">
            <div class="departmentName" :class="{ selected: selectedIndex === index }" @click="handleClick(index)">{{
              item.departmentName }}</div>
          </div>
        </el-card>
        <el-card class="card-right" style="width: 900px;margin-left: 10px">
          <div class="departmentName-title" style="align-items: center;">
            <div style="display: flex;align-items: center;">
              <div style="width: 5px;background-color: #79bbff;height:22px;display: inline-block;border-radius: 5px;">
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
    <!-- 右侧容器 -->
    <div class="right">
      <el-card style="height: 550px">
        <span class="title" style="font-size: 20px;font-weight: bold;margin-top: 20px;">平台公告</span>
        <el-table :data="tableData" style="width: 100%;">
          <el-table-column prop="msg" label="全部" width="250" />
        </el-table><br>
        <span class="title" style="font-size: 20px;font-weight: bold;margin-top: 20px;">停诊公告</span>
        <el-table :data="tableData2" style="width: 100%;">
          <el-table-column prop="msg" label="全部" width="250" />
        </el-table>
      </el-card>

    </div>
  </div>


  <Footer />
</template>

<style scoped>
/* 轮播图 */
.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
  text-align: center;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}

/* 父容器 */
.container {
  display: flex;
  /* flex-direction: column; */
  /* background-color: rgb(133, 208, 240); */
  /* overflow: auto; */
  /* 设置溢出时显示滚动条 */
}

/* 左侧容器 */
.left {
  margin: 10px;
  width: 1200px;
  height: auto;
  /* border: 1px solid red; */
  /* display: flex; */
}

.head-left {
  height: 250px;
  /* border: 1px solid green; */
  display: flex;
}

.content-left {
  /* height: 500px; */
  padding: 0;
  /* margin-top: 5px; */
  display: flex;
  /* border: 1px solid blue */
}

.weather {
  width: 360px;
  height: 220px;
  margin-top: 10px;
  margin: 20 px;
  /* border: 1px solid green; */
}

/* 右侧容器 */
.right {
  margin: 5px;
  margin-top: 20px;
  width: 320px;
  height: 500px;
  /* border: 1px solid blueviolet; */
}

/* 轮播图样式 */
.carousel {
  width: 700px;
  margin: 25px auto;
}

/* 数据 */
/* 科室名称 */
.departmentName {
  height: 35px;
  display: flex;
  justify-content: center;
  /* 水平居中 */
  align-items: center;
  /* 垂直居中 */
}

/*
.departmentName:hover {
  background-color: #F2F6FC;
} */

.departmentName.selected {
  /* 选中时的样式 */
  background-color: #F2F6FC;
}
</style>
