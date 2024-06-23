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
        <div>
          <div class="pay-content">
            <div class="title">订单提交成功</div>
            <div class="text time-margin">
              <span>请在 </span>
              <span class="time">{{ workDate }}</span>
              <span> 之前付款，超时订单将自动取消</span>
            </div>
            <div class="text">支付金额</div>
            <div class="price">
              <span class="num">{{ amount }}</span>
              <span>元</span>
            </div>
            <div class="pay-choose-view" style="">
              <div class="pay-choose-box flex-view">
                <div class="choose-box choose-box-active">
                  <img :src="WxPayIcon">
                  <span>微信支付</span>
                </div>
                <div class="choose-box">
                  <img :src="AliPayIcon">
                  <span>支付宝</span>
                </div>
              </div>
              <div class="tips">请选择任意一种支付方式</div>
              <button class="pay-btn pay-btn-active" @click="handlePay">确认支付</button>
            </div>
            <div class="pay-qr-view" style="display: none;">
              <div class="loading-tip" style="">正在生成安全支付二维码</div>
              <div class="qr-box" style="display: none;">
                <div id="qrCode" class="qr">
                </div>
                <div class="tips">请打开微信扫一扫进行支付</div>
                <button class="pay-finish-btn">支付完成</button>
                <button class="back-pay-btn">选择其他支付方式</button>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import WxPayIcon from '@/assets/images/wx-pay.svg';
import AliPayIcon from '@/assets/images/ali-pay.svg';
import router from '@/router'
import { ElMessage } from 'element-plus'
import { isPay } from '@/api/order/order'


let ddlTime = ref()
const amount = ref()
const workDate = ref()
const orderNo = ref('')

onMounted(() => {
  amount.value = router.currentRoute.value.query.amount
  workDate.value = router.currentRoute.value.query.workDate
  orderNo.value = router.currentRoute.value.query.orderNo
  ddlTime.value = formatDate(new Date().getTime(), 'YY-MM-DD hh:mm:ss')
  console.log('订单号', orderNo.value)
})

const handlePay = async () => {
  // message.warn('暂无支付功能')
  // 更改订单状态
  console.log(orderNo.value)
  const { code } = await isPay(orderNo.value)
  if (code === 200) {
    // 提示信息
    ElMessage.success('支付成功')
    // 跳转到订单页
    router.push({ path: '/orderdetail', query: { orderNo: orderNo.value } })
  }
}
const formatDate = (time, format = 'YY-MM-DD hh:mm:ss') => {
  const date = new Date(time)

  const year = date.getFullYear(),
    month = date.getMonth() + 1,
    day = date.getDate() + 1,
    hour = date.getHours(),
    min = date.getMinutes(),
    sec = date.getSeconds()
  const preArr = Array.apply(null, Array(10)).map(function (elem, index) {
    return '0' + index
  })

  const newTime = format.replace(/YY/g, year)
    .replace(/MM/g, preArr[month] || month)
    .replace(/DD/g, preArr[day] || day)
    .replace(/hh/g, preArr[hour] || hour)
    .replace(/mm/g, preArr[min] || min)
    .replace(/ss/g, preArr[sec] || sec)

  return newTime
}

</script>

<style scoped lang="less">
.flex-view {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
}

.pay-content {
  position: relative;
  margin: 120px auto 0;
  width: 500px;
  background: #fff;
  overflow: hidden;

  .title {
    color: #152844;
    font-weight: 500;
    font-size: 24px;
    line-height: 22px;
    height: 22px;
    text-align: center;
    margin-bottom: 11px;
  }

  .time-margin {
    margin: 11px 0 24px;
  }

  .text {
    height: 22px;
    line-height: 22px;
    font-size: 14px;
    text-align: center;
    color: #152844;
  }

  .time {
    color: #f62a2a;
  }

  .text {
    height: 22px;
    line-height: 22px;
    font-size: 14px;
    text-align: center;
    color: #152844;
  }

  .price {
    color: #ff8a00;
    font-weight: 500;
    font-size: 16px;
    height: 36px;
    line-height: 36px;
    text-align: center;

    .num {
      font-size: 28px;
    }
  }

  .pay-choose-view {
    margin-top: 24px;

    .choose-box {
      width: 140px;
      height: 126px;
      border: 1px solid #cedce4;
      border-radius: 4px;
      text-align: center;
      cursor: pointer;
    }

    .pay-choose-box {
      -webkit-box-pack: justify;
      -ms-flex-pack: justify;
      justify-content: space-between;
      max-width: 300px;
      margin: 0 auto;

      img {
        height: 40px;
        margin: 24px auto 16px;
        display: block;
      }
    }

    .tips {
      color: #6f6f6f;
      font-size: 14px;
      line-height: 22px;
      height: 22px;
      text-align: center;
      margin: 16px 0 24px;
    }

    .choose-box-active {
      border: 1px solid #4684e2;
    }

    .tips {
      color: #6f6f6f;
      font-size: 14px;
      line-height: 22px;
      height: 22px;
      text-align: center;
      margin: 16px 0 24px;
    }

    .pay-btn {
      cursor: pointer;
      background: #c3c9d5;
      border-radius: 32px;
      width: 104px;
      height: 32px;
      line-height: 32px;
      border: none;
      outline: none;
      font-size: 14px;
      color: #fff;
      text-align: center;
      display: block;
      margin: 0 auto;
    }

    .pay-btn-active {
      background: #4684e2;
    }
  }
}
</style>
