<script setup>
import '../../assets/css/hospital.css'
// import '../../assets/css/hospital_personal.css'
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import router from '@/router'
import { getOrderByNo, cancelOrder } from '@/api/order/order'

onMounted(() => {
  // 获取到订单编号
  orderNo.value = router.currentRoute.value.query.orderNo
  fetchData()
})

// ====================定义数据======================
const orderNo = ref(null)
const orderInfo = ref({
  param: {},
})
// 初始化isCancel为null
const isCancel = ref(null);

// =====================取消预约==========================
const handleCancel = () => {
  console.log('订单No', orderNo.value)
  ElMessageBox.confirm('确定要取消预约吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const { code } = await cancelOrder(orderNo.value)
    if (code === 200) {
      ElMessage.success('取消预约成功')
      // 刷新数据
      fetchData()
    }
  })
}
// =====================支付订单=========================
const handleConfirm = (amount, workDate, orderNo) => {
  // 跳转到支付页
  router.push({ path: '/pay', query: { amount: amount, workDate: workDate, orderNo: orderNo } })
}
const fetchData = async () => {

  // 获取订单信息
  const { code, data } = await getOrderByNo(orderNo.value)
  console.log('data', data)
  if (code === 200) {
    orderInfo.value = data
  }
  canCancel()
}
// =====================判断是否能取消预约============================
const canCancel = () => {
  // 获取到退号时间
  const quitTime = orderInfo.value.quitTime
  // 获取当前时间的日期部分
  const now = new Date();
  console.log('now', now)
  const nowTime = now.toISOString().split('T')[0]; // 将日期转换为"YYYY-MM-DD"格式
  // 将quitTime转换为Date对象
  const quitDate = new Date(quitTime);
  console.log('quitDate', quitDate)
  // 比较当前日期和quitTime
  isCancel.value = quitDate > now
  console.log('isCancel', isCancel.value)
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
          <div class="order-detail">
            <div class="title" style="margin-top: 10px;margin-left: 10px;"> 挂号详情</div>
            <div class="status-bar"
              style="display: flex;width:95%;height: 100px;margin-left: 10px;margin-top: 10px;background-color: rgb(236,245,255);">
              <div class="left-wrapper" style="width: 65%;display: flex;align-items: center;">
                <div class="status-wrapper BOOKING_SUCCESS"
                  style="color: rgb(64,158,255);margin-left: 10px;font-size: 20px;">
                  <span class="iconfont"></span> {{ orderInfo.param.orderStatusString }}
                </div>
              </div>
              <div class="right-wrapper" style="height: 100px;display: flex;align-items: center;">
                <img style="height: 90px;" src="//img.114yygh.com/static/web/code_order_detail.png" class="code-img">
                <div class="content-wrapper" style="margin-left: 5px;flex-direction: column;">
                  <div> 微信<span class="iconfont"></span>关注“医疗服务预约平台”</div>
                  <div class="watch-wrapper"> 快速挂号，轻松就医</div>
                </div>
              </div>
            </div>
            <div class="info-wrapper">
              <div style="margin-top: 25px;margin-left: 10px;display: flex;align-items: center;">
                <div class="block"></div>
                <div style="margin-left: 0">挂号信息</div>
              </div>
              <div class="info-form" style="margin-left: 300px;">
                <el-form ref="form" :model="form">

                  <el-form-item label="就诊人信息：">
                    <div class="content"><span>{{ orderInfo.patientName }}</span></div>
                  </el-form-item>
                  <el-form-item label="就诊日期：">
                    <div class="content"><span>{{ orderInfo.workDate }} {{ orderInfo.reserveTime == 0 ? '上午' : '下午'
                        }}</span></div>
                  </el-form-item>
                  <el-form-item label="就诊医院：">
                    <div class="content"><span>{{ orderInfo.hosptialName }} </span></div>
                  </el-form-item>
                  <el-form-item label="就诊科室：">
                    <div class="content"><span>{{ orderInfo.departmentName }} </span></div>
                  </el-form-item>
                  <el-form-item label="医生姓名：">
                    <div class="content"><span>{{ orderInfo.doctorName }} </span></div>
                  </el-form-item>
                  <el-form-item label="医事服务费：">
                    <div class="content">
                      <div class="fee" style="color: rgb(64,158,255);font-weight: 600;">{{ orderInfo.amount }}元
                      </div>
                    </div>
                  </el-form-item>
                  <el-form-item label="挂号单号：">
                    <div class="content"><span>{{ orderInfo.orderNo }} </span></div>
                  </el-form-item>
                  <el-form-item label="挂号时间：">
                    <div class="content"><span>{{ orderInfo.createTime }}</span></div>
                  </el-form-item>
                </el-form>
              </div>
            </div>
            <div class="rule-wrapper mt40">
              <div class="rule-title"> 注意事项</div>
              <div>1、请确认就诊人信息是否准确，若填写错误将无法取号就诊，损失由本人承担；<br>
                <span style="color:red">2、【取号】就诊当天需在9:30之前在医院取号，未取号视为爽约，该号不退不换；</span><br>
                3、【退号】在就诊前一天可在线退号 ，逾期将不可办理退号退费；<br>
                4、医疗服务预约平台支持自费患者使用身份证预约，同时支持北京市医保患者使用北京社保卡在平台预约挂号。请于就诊当日，携带预约挂号所使用的有效身份证件到院取号；<br>
                5、请注意北京市医保患者在住院期间不能使用社保卡在门诊取号。
              </div>
            </div>
            <div class="btn-wrapper" style="text-align: center;margin-top: 10px;">
              <!-- 订单状态：0-待支付，1-已支付，-1-已取消 -->
              <div v-if="orderInfo.status === 0">
                <div v-if="isCancel">
                  <el-button style="width: 100px;" type="primary" @click="handleCancel" plain>取消预约</el-button>
                  <el-button type="primary" style="margin-left: 20px;width: 100px;"
                    @click="handleConfirm(orderInfo.amount, orderInfo.workDate, orderInfo.orderNo)">支付</el-button>
                </div>
                <div v-if="!isCancel">
                  <el-button style="width: 100px;" type="primary" @click="handleCancel" plain disabled>订单已失效</el-button>
                  <el-button type="primary" style="margin-left: 20px;width: 100px;" disabled
                    @click="handleConfirm(orderInfo.amount, orderInfo.workDate, orderInfo.orderNo)">支付</el-button>
                </div>
              </div>
              <div v-if="orderInfo.status === 1">
                <div v-if="isCancel">
                  <el-button style="width: 100px;" type="primary" @click="handleCancel" plain>取消预约</el-button>
                  <el-button type="primary" style="margin-left: 20px;width: 100px;"
                    @click="handleConfirm(orderInfo.amount, orderInfo.workDate, orderInfo.orderNo)">支付</el-button>
                </div>
                <div v-if="!isCancel">
                  <el-button style="width: 100px;" type="primary" @click="handleCancel" plain disabled>订单已失效</el-button>
                  <el-button type="primary" style="margin-left: 20px;width: 100px;" disabled
                    @click="handleConfirm(orderInfo.amount, orderInfo.workDate, orderInfo.orderNo)">支付</el-button>
                </div>
              </div>
              <div v-if="orderInfo.status === -1">
                <!-- <el-button style="width: 100px;" type="primary" @click="handleCancel" plain>取消预约</el-button> -->
                <el-button type="primary" disabled style="margin-left: 20px;width: 100px;"
                  @click="handleConfirm">支付</el-button>
              </div>
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
/* .tac {
  height: 545px;
} */
</style>
