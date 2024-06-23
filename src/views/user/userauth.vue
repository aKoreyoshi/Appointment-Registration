<script setup>
import { UploadFilled } from '@element-plus/icons-vue'
import { onMounted } from 'vue'
import { getCertificateTypes, authentication } from '@/api/user/user'
import { useAccount } from '@/store/modules/account'
import { getUser } from '@/utils/storage'
import { ElMessage } from 'element-plus'
import axios from 'axios'


onMounted(() => {
  // 获取证件类型
  fetchCertificateTypes()
  fetchData()
})

// 未认证
// const fromStatus = ref(true)
const fromStatus = ref('')
// 认证完成
// const authStatus = ref(false)
const authStatus = ref('')
const user = ref(null)

// 认证完成展示数据
const authdData = ref({
  name: '',
  phone: '',
  certificateType: '',
  certificateNo: ''
})
// 认证表单数据
const authFromData = ref({
  name: '',
  certificateType: '',
  certificateNo: '',
  certificateUrl: ''
})
// 验证规则
const rules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
  ],
  certificate_type: [
    { required: true, message: '请选择证件类型', trigger: 'blur' },
  ],
  certificate_no: [
    { required: true, message: '请输入证件号', trigger: 'blur' },
  ]
}
// 上传文件
const beforeUpload = (file) => {
  const formData = new FormData();
  formData.append('file', file);

  axios.post('http://111.229.164.96:8888/api/oss/fileUpLoad', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
      // 如果需要携带其他自定义的请求头，可以在这里添加
    }
  })
    .then(response => {
      // 处理上传成功的逻辑，response 中包含服务器返回的数据
      console.log('File uploaded successfully:', response.data);
      authFromData.value.certificateUrl = response.data.data
    })
    .catch(error => {
      // 处理上传失败的逻辑
      console.error('Failed to upload file:', error);
    });
  console.log('用户信息', authFromData.value)
  // 阻止默认上传行为
  return false;
};
// 上传认证
const onSubmit = async () => {

  const { code, message } = await authentication(authFromData.value)
  if (code === 200) {
    // 刷新数据
    fetchData()
    // 切换信息展示
    fromStatus.value = false
    authStatus.value = true

    // location.reload()
    // 提示信息
    ElMessage.success('上传信息成功')
  }
}
// 修改认证
const updateAuth = () => {
  fromStatus.value = true
  authStatus.value = false
}
// 证件类型
const certificateTypes = ref([])
// 获取证件类型
const fetchCertificateTypes = async () => {
  // 获取证件类型
  const { code, data } = await getCertificateTypes()
  if (code === 200) {
    certificateTypes.value = data.certificateTypeList[0].children
  }
}

const fetchData = () => {

  // 页面刷新先更新用户信息
  useAccount().getUserinfo()

  // 获取用户信息
  user.value = getUser('userinfo')
  console.log(user.value)
  if (user.value.name === '') {
    // 未认证为true
    fromStatus.value = true
    // 认证完成为false
    authStatus.value = false

  } else {
    // 未认证为false
    fromStatus.value = false
    // 认证完成为true
    authStatus.value = true
    // 赋值
    authdData.value.name = user.value.name
    authdData.value.phone = user.value.phone
    authdData.value.certificateType = user.value.certificateType
    authdData.value.certificateNo = user.value.certificateNo
  }

}
// =================加密处理=====================
const encryptField = (value) => {
  if (!value) return value;
  if (value.length == 2) {
    return value[0] + '*'.repeat(value.length - 1)
  } if (value.length == 3) {
    return value[0] + '*'.repeat(value.length - 2) + '*'.repeat(value.length - 1)
  }
  return value[0] + '*'.repeat(value.length - 5) + value.slice(-4);
}
const encryptedData = computed(() => {
  return {
    name: encryptField(authdData.value.name),
    phone: encryptField(authdData.value.phone),
    certificateType: authdData.value.certificateType,
    certificateNo: encryptField(authdData.value.certificateNo)
  };

});
</script>

<template>
  <Header></Header>
  <div>
    <el-row class="tac">
      <!-- 左侧导航栏 -->
      <el-col :span="4">
        <!-- <h5 class="mb-2">Default colors</h5> -->
        <el-menu :default-active="$route.path" class="el-menu-vertical-demo" :router="true">
          <el-menu-item index="/userauth" style="background-color: rgb(236,245,255);">
            <el-icon>
              <Menu />
            </el-icon>
            <span>实名认证</span>
          </el-menu-item>
          <el-menu-item index="/patient">
            <el-icon></el-icon>
            <span>就诊人管理</span>
          </el-menu-item>
          <el-menu-item index="/order">
            <el-icon></el-icon>
            <span>挂号订单</span>
          </el-menu-item>
        </el-menu>
      </el-col>
      <!-- 右侧页面 -->
      <el-col :span="20" class="col-right">
        <div style="width: 1100px;height: 500px;" class="content">
          <!-- ================================认证完成组件====================================== -->
          <!-- tips -->
          <div style="margin-top: 5%" class="tips" v-if="authStatus">
            <span v-if="user.authStatus === 2">恭喜您认证完成！</span>
            <span v-if="user.authStatus === 1">您的认证正在审核中！</span>
          </div>
          <!-- // 认证完成展示数据 -->
          <div class="atuh-data" v-if="authStatus">
            <el-form :label-position="right" label-width="auto" :model="authdData" style="max-width: 400px">
              <el-form-item label="姓名">
                <el-input v-model="encryptedData.name" disabled />
              </el-form-item>
              <el-form-item label="手机号">
                <el-input v-model="encryptedData.phone" disabled />
              </el-form-item>
              <el-form-item label="证件类型">
                <el-input v-model="encryptedData.certificateType" disabled />
              </el-form-item>
              <el-form-item label="证件号码">
                <el-input v-model="encryptedData.certificateNo" disabled />
              </el-form-item>
              <div style="justify-content: center;align-items: center;display: flex;">
                <el-form-item>
                  <span v-if="user.authStatus === 2">
                    <el-button type="primary" @click="updateAuth" disabled>修改认证</el-button>
                  </span>
                  <span v-if="user.authStatus === 1">
                    <el-button type="primary" @click="updateAuth">修改认证</el-button>
                  </span>
                </el-form-item>
              </div>
            </el-form>
          </div>
          <!-- ================================未认证组件====================================== -->
          <div class="tips" v-if="fromStatus">
            完成实名认证后才能添加就诊人，正常进行挂号。为了不影响后续步骤，建议提前实名认证。
          </div>
          <div class="authing" v-if="fromStatus">
            <el-form ref="ruleFormRef" :model="authFromData" label-width="auto" class="demo-dynamic" :rules="rules"
              :size="formSize" status-icon>
              <ul class="ul-form">
                <li>
                  <div class="cl-input">
                    <el-form-item label="姓名" prop="name">
                      <el-input v-model="authFromData.name" placeholder="请输入姓名" />
                    </el-form-item>
                  </div>
                </li>
                <li>
                  <div class="cl-input">
                    <el-form-item label="证件类型" prop="certificateType">
                      <el-select v-model="authFromData.certificateType" placeholder="请选择证件类型">
                        <el-option v-for="item in certificateTypes" :key="item.id" :label="item.label"
                          :value="item.name"></el-option>
                      </el-select>
                    </el-form-item>
                  </div>
                </li>
                <li>
                  <div class="cl-input">
                    <el-form-item label="证件号" prop="certificateNo">
                      <el-input v-model="authFromData.certificateNo" placeholder="请输入证件号" />
                    </el-form-item>
                  </div>
                </li>
                <li>
                  <div class="cl-input">
                    <el-form-item label="证件图片">
                      <el-upload class="upload-demo" drag action="" multiple style="width: 400px;"
                        :before-upload="beforeUpload" :show-file-list="false">
                        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
                        <div class="el-upload__text">
                          拖拽文件至此或者 <em>点击上传</em>
                        </div>
                      </el-upload>
                    </el-form-item>
                  </div>
                </li>
                <li>
                  <div class="cl-input" style="display: flex;justify-content: center;align-items: center;">
                    <el-form-item>
                      <el-button style="width: 100px" type="primary" @click="onSubmit">上传</el-button>
                    </el-form-item>
                  </div>
                </li>

              </ul>
            </el-form>
          </div>
        </div>
      </el-col>
    </el-row>

  </div>
  <Footer />
</template>

<style scoped>
.tac {
  height: 545px;
  /* border: 1px solid; */
}

.col-right {
  display: flex;
  justify-content: center;
  align-items: center;
  /* background-color: antiquewhite */
}

/* tips */
.tips {
  font-size: 25px;
  font-weight: bold;
  font-family: "KaiTi", "STKaiti", "楷体", serif;
  color: rgb(64, 158, 255);
  /* 在顶部显示 */
  margin-top: 10px;
}

.content {
  display: flex;
  /* justify-content: center; */
  align-items: center;
  flex-direction: column;
}

.atuh-data {
  margin-top: 5%;
  align-self: center;
  /* 垂直居中 */
}

/* 认证表格 */
.authing {
  overflow-y: auto;
  /* 当内容溢出时显示垂直滚动条 */
}

ul {
  list-style-type: none;
  padding: 0;
  margin: 0;
}

.ul-form li {
  width: 1050px;
  /* height: 50px; */
  /* border: 1px solid; */
}

.cl-input {
  width: 400px;
  /* border: 1px solid; */
  margin: 0 auto;
  /* 左右自动边距，实现水平居中 */
  margin-top: 15px;
}
</style>
