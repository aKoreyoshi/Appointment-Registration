<script setup>
import { ref } from "vue";
import { ChatDotRound } from "@element-plus/icons-vue";
import { Iphone } from "@element-plus/icons-vue";
import { Login, getCode } from "@/api/user/user";
import { useApp } from "@/store/modules/app";
import { ElMessage } from "element-plus";
import router from '@/router/index'
import { useAccount } from '@/store/modules/account'

const formData = ref({
  phone: "",
  code: "",
});
// --------获取验证码倒计时---------
// 获取验证码倒计时
const countdown = ref(0);
const timer = ref(null); // 定义一个nullable的number类型
const getRegin = async () => {
  // 发送验证码的逻辑
  console.log(formData.value.phone);
  const { code, message } = await getCode(formData.value.phone);
  if (code === 200) {
    ElMessage.success('发送成功')
  }
  // 发送验证码后开始倒计时
  countdown.value = 60; // 设置倒计时时间为60秒
  timer.value = setInterval(() => {
    countdown.value--; // 每秒减少1秒
    if (countdown.value <= 0) {
      clearInterval(timer.value);
      timer.value = null; // 清除计时器后将其设置为null
    }
  }, 1000);
};
// --------手机号输入框判断---------
const validatePhone = (rule, value) => {
  if (!value) {
    return Promise.reject("请输入手机号");
  } else if (!/^\d{11}$/.test(value)) {
    return Promise.reject("手机号格式不正确");
  } else {
    return Promise.resolve();
  }
};
const phoneRules = [
  { required: true, message: "请输入手机号", trigger: "blur" },
  { validator: validatePhone, trigger: "blur" },
];
// --------登录---------
const handleSubmit = async () => {
  // console.log('登录',formData.value);
  const { code, data } = await Login(formData.value); // 调用登录接口，返回值包含code,message,data三个字段，根据实际情况调整接口返回值结构。
  // console.log("登录结果", code, "token", data.token); // 打印登录结果
  if (code === 200) {
    useApp().initToken(data.token); // 调用useUserStore中的setToken方法，将token存储到pinia中
    // // 保存用户信息
    // useAccount().getUserinfo()
    // 跳转到首页
    router.push('/')
    // 提示登录成功
    ElMessage.success('登录成功')
  }
};
</script>

<template>
  <Header></Header>
  <div class="login-container">
    <div class="left-half">
      <!-- 左侧内容 -->
    </div>
    <div class="right-half">
      <!-- 右侧内容 -->
      <div class="login-title">欢迎登录</div>
      <div class="login-form">
        <!-- 登录表单 -->
        <el-form :model="formData" style="max-width: 350px">
          <el-form-item prop="phone" hide-label :rules="phoneRules">
            <el-input v-model="formData.phone" placeholder="请输入手机号" :prefix-icon="Iphone" />
          </el-form-item>
          <el-form-item>
            <el-input v-model="formData.code" placeholder="请输入验证码" :prefix-icon="ChatDotRound">
              <template #append>
                <el-button style="width: 130px" :disabled="countdown > 0" @click="getRegin">{{
          countdown > 0 ? `${countdown}秒后重新发送` : "发送验证码"
        }}</el-button>
              </template>
            </el-input>
          </el-form-item>
          <el-button style="width: 350px" id="btn-submit" type="primary" @click="handleSubmit">登录</el-button>
        </el-form>
      </div>
    </div>
  </div>
</template>

<style>
/* 重置默认的边距和填充 */
/* .body{
  margin: 0;
  padding: 0;
} */
.login-container {
  width: 100%;
  /* 视口宽度的100% */
  height: 615px;
  /* 视口高度的100% */
  margin-top: 1px;
  /* border: 3px solid black; 添加边框 */
  box-sizing: border-box;
  /* 将边框大小包含在总宽度和高度中 */
  display: flex;
  /* 使用flex布局 */
  align-content: center;
}

/* 左侧子容器样式 */
.left-half {
  flex: 0.6;
  /* border: 1px solid black; */
  background: linear-gradient(to bottom, #181850, #1f1f8e);
  /* 渐变背景 */
}

/* 右侧子容器样式 */
.right-half {
  flex: 1.4;
  /* border: 1px solid red; */
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

/* 登录标题样式 */
.login-title {
  width: 350px;
  font-size: 25px;
  text-align: left;
  margin-bottom: 20px;
}

/* 登录表单样式 */
.login-form {
  text-align: center;
  /* 水平居中 */
}
</style>
