<template>
  <div class="app-container">
    <div>
      <span>上传医院信息:&nbsp;</span>
      <input type="file" @change="onFileChange" />
      <button @click="uploadFile">点击上传</button>
    </div>
    <br />
    <!-- 展示医院信息 -->
    <el-card style="max-width: 100%">
      <el-descriptions title="基本信息" :column="2" border>
        <el-descriptions-item
          label="医院名称"
          label-align="center"
          align="center"
          label-class-name="my-label"
          class-name="my-content"
          min-width="300px"
        >
          {{ hospInfo.hospitalName }}
        </el-descriptions-item>
        <el-descriptions-item
          label="医院logo"
          label-align="center"
          align="center"
          min-width="300px"
        >
          <img
            :src="'data:image/jpeg;base64,' + hospInfo.logoData"
            width="80"
          />
        </el-descriptions-item>
        <el-descriptions-item
          label="医院等级"
          label-align="center"
          align="center"
          min-width="300px"
        >
          {{ hospInfo.hospitalGrade }}
        </el-descriptions-item>
        <el-descriptions-item
          label="医院地址"
          label-align="center"
          align="center"
          min-width="300px"
        >
          {{ hospInfo.address }}
        </el-descriptions-item>
        <el-descriptions-item
          label="联系电话"
          label-align="center"
          align="center"
          min-width="300px"
        >
          {{ hospInfo.phone }}
        </el-descriptions-item>
        <el-descriptions-item
          label="医院状态"
          label-align="center"
          align="center"
          min-width="300px"
        >
          <el-switch v-model="status" @change="updateStatus" />
          &nbsp;
          <span>{{ status ? '可用' : '禁用' }}</span>
        </el-descriptions-item>
      </el-descriptions>
      <el-descriptions :column="1" border>
        <el-descriptions-item
          label="医院简介"
          label-align="center"
          min-width="300px"
        >
          {{ hospInfo.intro }}
        </el-descriptions-item>
        <el-descriptions-item
          label="坐车路线"
          label-align="center"
          min-width="300px"
        >
          {{ hospInfo.route }}
        </el-descriptions-item>
      </el-descriptions>
      <br />
      <el-descriptions title="预约规则" :column="2" border>
        <el-descriptions-item
          label="预约周期"
          label-align="center"
          align="center"
          label-class-name="my-label"
          class-name="my-content"
          min-width="300px"
        >
          {{ bookingRuleInfo.cycle }}
        </el-descriptions-item>
        <el-descriptions-item
          label="放号时间"
          label-align="center"
          align="center"
          min-width="300px"
        >
          {{ bookingRuleInfo.releaseTime }}
        </el-descriptions-item>
        <el-descriptions-item
          label="停挂时间"
          label-align="center"
          align="center"
          min-width="300px"
        >
          {{ bookingRuleInfo.stopTime }}
        </el-descriptions-item>
        <el-descriptions-item
          label="退号时间"
          label-align="center"
          align="center"
          min-width="300px"
        >
          {{ quitDay }}
          {{ bookingRuleInfo.quitTime }}
        </el-descriptions-item>
        <el-descriptions-item label="预约规则" label-align="center">
          {{ bookingRuleInfo.rule }}
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 上传数据表单 -->
    <el-dialog
      v-model="dialogVisible"
      title="上传医院数据"
      width="50%"
      :before-close="handleClose"
    >
      <el-form label-width="auto" style="max-width: 750px">
        <!-- <el-form-item>
          <el-input style="height: 200px;" type="textarea" v-model="hospData"/>
        </el-form-item> -->
        <textarea
          style="
            width: 100%;
            height: 200px;
            font-family: Arial, sans-serif;
            font-size: 16px;
          "
          v-model="hospData"
          placeholder="请输入文本..."
        />
        <el-form-item>
          <el-button type="primary" @click="onSubmit">提交</el-button>
          <el-button @click="dialogVisible = false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <!-- {{ hospData }} -->
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getHospInfo, updateHospStatus } from '@/api/hospital'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

onMounted(() => {
  fetchData()
})
// ------------医院状态------------
const status = ref(true)
const hospitalStatusDto = ref({
  hospitalCode: '',
  status: '',
})
const updateStatus = async statusValue => {
  // 拿到医院编号
  hospitalStatusDto.value.hospitalCode = hospInfo.value.hospitalCode
  if (statusValue) {
    hospitalStatusDto.value.status = 0
  } else {
    hospitalStatusDto.value.status = 1
  }
  console.log('hospitalStatusDto', hospitalStatusDto.value.hospitalCode)
  const { code, messaage } = await updateHospStatus(hospitalStatusDto.value)
  if (code === 200) {
    // 提示信息
    ElMessage.success('更新医院状态成功')
  }
}
// ------------退号时间------------
const quitDay = ref('')
// ------------上传数据------------
const selectedFile = ref(null)
const onFileChange = event => {
  selectedFile.value = event.target.files[0]
}
// 提交数据
const uploadFile = async () => {
  if (!selectedFile.value) {
    alert('请先选择一个文件')
    return
  }
  const formData = new FormData()
  formData.append('file', selectedFile.value)
  axios
    .post('http://111.229.164.96:8090/admin/hosp/hospital/saveHosp', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
    .then(response => {
      // console.log('data', response.data)
      if (response.data.code === 200) {
        // 提示信息
        ElMessage.success('上传成功')
        fetchData()
      } else {
        // 提示信息
        ElMessage.error('上传失败')
      }
    })
}

// ------------获取医院信息------------
// 定义接收数据的类型
const hospInfo = ref({})
const bookingRuleInfo = ref({})
// 调用获取信息接口
const fetchData = async () => {
  const { code, message, data } = await getHospInfo()
  console.log('data', data)
  if (code === 200) {
    // 赋值
    hospInfo.value = data.hospitalSet
    bookingRuleInfo.value = data.bookingRule
    const quit = data.bookingRule.quitDay
    if (quit === -1) {
      quitDay.value = '就诊前一日'
    }
    // 判断状态
    const statusValue = data.hospitalSet.status
    if (statusValue === 0) {
      status.value = true
    }
    if (statusValue === 1) {
      status.value = false
    }
  }
}
</script>

<style scoped>
</style>