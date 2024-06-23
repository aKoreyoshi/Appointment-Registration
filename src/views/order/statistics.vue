<script setup>
import * as echarts from 'echarts'
import { ref, onMounted } from 'vue'
import { getOrderCount } from '@/api/order'

const chart = ref(null)
const chartData = ref({ dateList: [], countList: [] })
onMounted(() => {
  // 获取数据
  fetchData()
})
const fetchData = async () => {
  const { code, data } = await getOrderCount()
  if (code === 200) {
    chartData.value = data
    renderChart()
  }
}

const renderChart = () => {
  const myChart = echarts.init(chart.value)

  const option = {
    xAxis: {
      type: 'category',
      data: chartData.value.dateList,
    },
    yAxis: {
      type: 'value',
    },
    series: [
      {
        data: chartData.value.countList,
        // data: [20, 6, 31, 17, 9, 15, 23],
        type: 'line',
      },
    ],
  }

  myChart.setOption(option)
}
</script>

<template>
  <div style="margin-left: 10px">
    <span style="font-weight: 600; font-size: 20px">挂号统计</span>
  </div>
  <div id="main" ref="chart" style="width: 100%; height: 400px"></div>
</template>

<style scoped>
</style>