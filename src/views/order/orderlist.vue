<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getOrderList, getOrderStatus, getOrderDetail } from '@/api/order'

onMounted(async () => {
  // 获取订单状态
  const { code, data } = await getOrderStatus()
  console.log(data)
  if (code === 200) {
    orderStatus.value = data
  }
  fetchData()
})

// ==================定义数据类型====================
const orderQueryVo = ref({
  patientName: '',
  hospitalName: '',
  status: '',
})
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const orderList = ref([])
const orderStatus = ref([])

const dialogVisible = ref(false) // 详情框
const orderDetail = ref([]) // 订单详情

// =======================查看订单详情=========================
const handleShow = async orderNo => {
  // console.log(orderNo)
  const { code, data } = await getOrderDetail(orderNo)
  console.log(data)
  if (code === 200) {
    orderDetail.value = data
    dialogVisible.value = true
  }
}

// =======================重置查询=======================
const resetData = () => {
  orderQueryVo.value = {
    patientName: '',
    hospitalName: '',
    status: '',
  }
  currentPage.value = 1
  fetchData()
}
const handlerCurrentChange = newPage => {
  currentPage.value = newPage
  fetchData()
}

const fetchData = async () => {
  console.log('tiaojianshuju', orderQueryVo.value)
  const { code, data } = await getOrderList(
    currentPage.value,
    pageSize.value,
    orderQueryVo.value
  )
  if (code === 200) {
    console.log(data)
    orderList.value = data.page.records
    total.value = data.page.total
  }
}
</script>

<template>
  <el-card>
    <div class="filter" style="font-size: 20px; font-weight: 600">订单列表</div>
    <div class="search-form" style="margin-top: 10px">
      <el-form :model="orderQueryVo" inline>
        <el-form-item label="就诊人姓名：">
          <el-input
            v-model="orderQueryVo.patientName"
            placeholder="请输入就诊人姓名"
            style="width: 300px"
          ></el-input>
        </el-form-item>
        <el-form-item label="订单状态：" style="margin-left: 10px">
          <el-select
            v-model="orderQueryVo.status"
            placeholder=""
            style="width: 300px"
          >
            <el-option
              v-for="item in orderStatus"
              :key="item.status"
              :label="item.comment"
              :value="item.status"
            />
          </el-select>
        </el-form-item>
        <el-form-item style="margin-left: 10px">
          <el-button type="primary" @click="fetchData">查询</el-button>
          <el-button @click="resetData">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </el-card>
  <el-card class="table-data" style="margin-top: 10px">
    <el-table :data="orderList" border style="width: 100%">
      <el-table-column prop="orderNo" label="订单编号" width="180" />
      <el-table-column prop="patientName" label="就诊人姓名" width="110" />
      <el-table-column prop="patientPhone" label="就诊人电话" width="130" />
      <el-table-column prop="departmentName" label="就诊科室" width="190" />
      <el-table-column prop="doctorName" label="医生姓名" width="130" />
      <el-table-column prop="workDate" label="就诊日期" width="130" />
      <el-table-column
        prop="param.orderStatusString"
        label="订单状态"
        width="200"
      />
      <el-table-column label="操作" #default="scope">
        <el-button type="primary" link @click="handleShow(scope.row.orderNo)">
          查看详情
        </el-button>
      </el-table-column>
    </el-table>
    <div class="pagination" style="margin-top: 10px">
      <el-pagination
        small
        background
        layout="prev, pager, next"
        :total="total"
        class="mt-4"
        @current-change="handlerCurrentChange"
      />
    </div>
  </el-card>

  <!-- 详情框 -->
  <el-dialog
    title="订单详情"
    v-model="dialogVisible"
    width="30%"
    :before-close="handleClose"
  >
    <div>
      <el-form :model="orderDetail" label-width="120px">
        <el-form-item label="订单编号：">
          <span style="font-weight: 600">{{ orderDetail.orderNo }}</span>
        </el-form-item>
        <el-form-item label="就诊人姓名：">
          <span style="font-weight: 600">{{ orderDetail.patientName }}</span>
        </el-form-item>
        <el-form-item label="就诊人电话：">
          <span style="font-weight: 600">{{ orderDetail.patientPhone }}</span>
        </el-form-item>
        <el-form-item label="就诊医院：">
          <span style="font-weight: 600">{{ orderDetail.hosptialName }}</span>
        </el-form-item>
        <el-form-item label="就诊科室：">
          <span style="font-weight: 600">{{ orderDetail.departmentName }}</span>
        </el-form-item>
        <el-form-item label="医生姓名：">
          <span style="font-weight: 600">{{ orderDetail.doctorName }}</span>
        </el-form-item>
        <el-form-item label="就诊日期：">
          <span style="font-weight: 600">{{ orderDetail.workDate }}</span>
        </el-form-item>
        <el-form-item label="就诊时间：">
          <span style="font-weight: 600">
            {{ orderDetail.workTime == 0 ? '上午' : '下午' }}
          </span>
        </el-form-item>
        <el-form-item label="订单状态：">
          <span style="font-weight: 600">
            {{ orderDetail.param.orderStatusString }}
          </span>
        </el-form-item>
        <el-form-item label="支付金额：">
          <span style="font-weight: 600">{{ orderDetail.amount }}元</span>
        </el-form-item>
        <el-form-item label="创建时间：">
          <span style="font-weight: 600">{{ orderDetail.createTime }}</span>
        </el-form-item>
      </el-form>
    </div>
    <div style="text-align: right; margin-top: 20px">
      <el-button type="primary" @click="dialogVisible = false">关闭</el-button>
    </div>
  </el-dialog>
</template>

<style scoped>
</style>