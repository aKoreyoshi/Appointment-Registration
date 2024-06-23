<!--
 * @Descripttion: 
 * @version: 
 * @Date: 2021-04-20 11:06:21
 * @LastEditors: huzhushan@126.com
 * @LastEditTime: 2022-09-24 18:18:43
 * @Author: huzhushan@126.com
 * @HomePage: https://huzhushan.gitee.io/vue3-element-admin
 * @Github: https://github.com/huzhushan/vue3-element-admin
 * @Donate: https://huzhushan.gitee.io/vue3-element-admin/donate/
-->
<template>
  <!-- github角标 -->
  <github-corner class="github-corner" />
  <el-card style="max-width: 100%">
    <el-row>
      <el-col :span="4" class="center-col">
        <div class="grid-content">
          <el-image
            class="img-circle"
            style="width: 100px; height: 100px"
            :src="userinfo.avatar"
          />
        </div>
      </el-col>
      <el-col :span="20" class="col-container">
        <div class="centered-div">
          <p>{{ greetings }},尊敬的{{ userinfo.userName }}</p>
          <p>
            今日天气{{ weatherInfo.weather }},气温在{{
              weatherInfo.nightTemperature
            }}℃至{{ weatherInfo.dayTemperature }}℃之间,{{
              weatherInfo.windDirection
            }}。
            <!-- 今日天气晴朗,气温在13℃至23℃之间,偏东南风。 -->
          </p>
        </div>
      </el-col>
    </el-row>
  </el-card>
  <el-card class="description-card" shadow="never">
    <span class="centered-text">欢迎来到医疗服务预约平台后台管理系统</span>
  </el-card>
</template>



<script setup>
// import { useUserStore } from '@/pinia/modules/app'
import { ref, onMounted, computed } from 'vue'
import { getWeather } from '@/api/login'
import { storeToRefs } from 'pinia'
import { useAccount } from '@/pinia/modules/account'

// 获取用户信息
const { userinfo } = storeToRefs(useAccount())

onMounted(() => {
  // weather()
  fetchData()
  // 获取时间
  getTime()
})

const date = new Date()

// 时间

const greetings = ref('')
const getTime = () => {
  const hours = date.getHours()
  // const hours = 7
  if (hours >= 6 && hours < 8) {
    greetings.value = '晨起披衣出草堂，轩窗已自喜微凉🌅！'
  } else if (hours >= 8 && hours < 12) {
    greetings.value = '上午好，'
  } else if (hours >= 12 && hours < 18) {
    greetings.value = '下午好，'
  } else if (hours >= 18 && hours < 24) {
    greetings.value = '晚上好，'
  } else if (hours >= 0 && hours < 6) {
    greetings.value =
      '偷偷向银河要了一把碎星，只等你闭上眼睛撒入你的梦中，晚安🌛！'
  }
}

// 天气信息对象
const weatherInfo = ref({
  weather: '',
  dayTemperature: '',
  nightTemperature: '',
  windDirection: '',
})

const fetchData = async () => {
  const { code, data, message } = await getWeather()
  const cityWeather = data.data.now.city
  if (code === 200) {
    weatherInfo.value.weather = cityWeather.weather
    weatherInfo.value.dayTemperature = cityWeather.day_air_temperature
    weatherInfo.value.nightTemperature = cityWeather.night_air_temperature
    weatherInfo.value.windDirection = cityWeather.wind_direction
  }
}
</script>


<style lang="scss" scoped>
.img-circle {
  border-radius: 50%;
  width: 200px; /* 或者你想要的任何尺寸 */
  height: 200px; /* 或者你想要的任何尺寸 */
  object-fit: cover; /* 保持图片的纵横比，同时填充整个元素的内容框 */
}

.grid-content {
  border-radius: 4px;
  min-height: 36px;
}

.center-col {
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  height: 100%; /* 或者你想要的任何高度，确保 el-col 有足够的高度 */
}

.col-container {
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
}

.centered-div {
  display: flex;
  flex-direction: column; /* 确保子元素垂直排列 */
  align-items: flex-start; /* 垂直对齐到顶部，但由于 flex-direction 是 column，这实际上会让内容靠左对齐 */
  text-align: left; /* 确保文本靠左对齐 */
  width: 100%; /* 如果需要，可以设置 div 的宽度 */
}

.centered-div p {
  margin: 5px; /* 移除默认的 margin，避免额外的间距 */
  /* 根据需要添加其他样式，如 padding */
}

.description-card {
  width: 100%;
  height: 420px;
  margin-top: 10px;
  background-color: #e9e9eb;
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
}

.centered-text {
  font-size: 30px; /* 字体大小，根据需要调整 */
  font-weight: bold; /* 字体加粗 */
  color: gray; /* 字体颜色设置为灰色 */
  opacity: 0.8; /* 降低透明度，这里的值表示80%的不透明度 */
}
</style>


