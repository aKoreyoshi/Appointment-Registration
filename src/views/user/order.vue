<script setup>
// import '../../assets/css/hospital.css'
// import '../../assets/css/hospital_personal.css'
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import router from '@/router'
import { getOrderList, getOrderStatus } from '@/api/order/order'

onMounted(async () => {
  // 获取订单状态
  const { data } = await getOrderStatus()
  // console.log('订单状态', data)
  orderStatus.value = data
  fetchData()
})


// ================定义数据格式===================
const orderQueryVo = ref({
  patientName: '',
  status: '',
})
const orderList = ref([]) // 订单列表
const orderStatus = ref([]) // 订单状态
const currentPage = ref(1) // 当前页数
const pageSize = ref(10) // 每页显示条数
const total = ref(0) // 总条数
// const loading = ref(false) // 加载状态


const fetchData = async () => {
  const { code, data } = await getOrderList(currentPage.value, pageSize.value, orderQueryVo.value)
  if (code === 200) {
    console.log('data', data)
    orderList.value = data.records
    total.value = data.total
  }
}
// 重置查询条件
const resetData = () => {
  orderQueryVo.value = {
    patientName: '',
    status: '',
  }
  currentPage.value = 1
  fetchData()
}

// 查看订单详情
const show = (orderNo) => {
  router.push({ path: '/orderdetail', query: { orderNo: orderNo } })
}
// 监听当前页码的变化
const handleCurrentChange = (newPage) => {
  currentPage.value = newPage;
  fetchData()
};
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
          <el-menu-item index="/patient">
            <el-icon></el-icon>
            <span>就诊人管理</span>
          </el-menu-item>
          <el-menu-item index="/order" style="background-color: rgb(236,245,255);">
            <el-icon>
              <Menu />
            </el-icon>
            <span>挂号订单</span>
          </el-menu-item>
        </el-menu>
      </el-col>
      <!-- 右侧页面 -->
      <el-col :span="20">
        <!-- 右侧内容 #start -->
        <div class="page-container">
          <div class="personal-order">
            <div class="title" style="margin-top: 10px;margin-left: 10px;font-size: 24px;font-weight: bold;"> 挂号订单</div>
            <el-form :inline="true" style="margin-top: 10px;margin-left: 15px">
              <el-form-item label=" 就诊人：">
                <el-input v-model="orderQueryVo.patientName" placeholder="就诊人姓名" style="width: 200px;" />
              </el-form-item>
              <el-form-item label="订单状态：" style="margin-left: 40px">
                <el-select v-model="orderQueryVo.status" placeholder="全部" class="v-select patient-select"
                  style="width: 200px;">
                  <el-option v-for="item in orderStatus" :key="item.status" :label="item.comment" :value="item.status">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="text" class="search-button v-link highlight clickable selected" @click="fetchData()">
                  查询
                </el-button>
                <el-button type="text" link @click="resetData">重置</el-button>
              </el-form-item>
            </el-form>
            <div class="table-wrapper table">
              <el-table :data="orderList" stripe style="width: 100%">
                <el-table-column label="就诊时间" width="150">
                  <template #default="scope">
                    {{ scope.row.workDate }} {{ scope.row.workTime === 0 ? '上午' : '下午' }}
                  </template>
                </el-table-column>
                <el-table-column prop="hosptialName" label="医院" width="150">
                </el-table-column>
                <el-table-column prop="departmentName" label="科室">
                </el-table-column>
                <el-table-column prop="doctorName" label="医生" width="120">
                </el-table-column>
                <el-table-column label="医事服务费" width="120" #default="scope">
                  {{ scope.row.amount }}元
                </el-table-column>
                <el-table-column prop="patientName" label="就诊人" width="120">
                </el-table-column>
                <el-table-column prop="param.orderStatusString" label="订单状态">
                </el-table-column>
                <el-table-column label="操作" #default="scope">
                  <el-button link @click="show(scope.row.orderNo)" style="color:rgb(64,158,255)">查看详情</el-button>
                </el-table-column>
              </el-table>
            </div>
            <div class="page" style="display: flex;align-items: center;justify-content: center;">
              <!-- 分页 -->
              <el-pagination class="pagination" layout="prev, pager, next" :current-page="currentPage" :total="total"
                :page-size="pageSize" @current-change="handleCurrentChange">
              </el-pagination>
            </div>
          </div>
        </div>
        <!-- 右侧内容 #end -->
      </el-col>
    </el-row>

  </div>
  <Footer />
</template>

<style scoped>
.tac {
  height: 545px;
}
</style>
