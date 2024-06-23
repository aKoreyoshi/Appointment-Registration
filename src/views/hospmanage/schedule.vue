<template>
  <div class="app-container">
    <div>
      <span>上传排班信息:&nbsp;</span>
      <input type="file" @change="onFileChange" />
      <button @click="uploadFile">点击上传</button>
    </div>
    <br />
    <div>
      <el-card style="max-width: 100%">
        <!-- <span>排班查询:</span> -->
        <el-form>
          <el-row>
            <el-col :span="6">
              <el-form-item label="医生名字:">
                <el-input
                  name="doctorName"
                  v-model="scheduleDto.doctorName"
                  placeholder="请输入医生名字"
                  style="width: 80%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="工作日期:">
                <el-date-picker
                  name="workDate"
                  v-model="scheduleDto.workDate"
                  type="date"
                  placeholder="请选择日期"
                  format="YYYY-MM-DD"
                  value-format="YYYY-MM-DD"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="工作时间:">
                <el-select
                  name="workTime"
                  v-model="scheduleDto.workTime"
                  placeholder="请选择时间"
                >
                  <el-option label="上午" value="0" />
                  <el-option label="下午" value="1" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-button type="primary" size="small" @click="querySchedule">
                搜索
              </el-button>
              <el-button size="small" @click="resetData">重置</el-button>
            </el-col>
          </el-row>
        </el-form>
        <!-- 展示排班记录 -->
        <el-table :data="list" style="width: 100%">
          <el-table-column
            align="center"
            prop="scheduleId"
            label="序号"
            width="75"
          />
          <el-table-column
            align="center"
            prop="departmentCode"
            label="科室编号"
            width="130"
          />
          <el-table-column
            align="center"
            prop="doctorTitle"
            label="职称"
            width="110"
          />
          <el-table-column
            align="center"
            prop="doctorName"
            label="医生名称"
            width="110"
          />
          <el-table-column
            align="center"
            prop="doctorSkill"
            label="擅长技能"
            width="200"
          />
          <el-table-column
            align="center"
            prop="workDate"
            label="工作日期"
            width="100"
          />
          <el-table-column
            align="center"
            prop="workTime"
            label="工作时间"
            width="100"
            #default="scope"
          >
            {{ scope.row.workTime === 0 ? '上午' : '下午' }}
          </el-table-column>
          <el-table-column
            align="center"
            prop="availableNumber"
            label="可预约数"
            width="100"
          />
          <el-table-column
            align="center"
            prop="reservedNumber"
            label="剩余预约数"
            width="110"
          />
          <el-table-column
            align="center"
            prop="registrationFee"
            label="挂号费用"
            width="100"
          />
          <el-table-column
            align="center"
            prop="name"
            label="操作"
            width="100"
            #default="scope"
          >
            <el-button type="danger" @click="delSchedule(scope.row.id)">
              删除
            </el-button>
          </el-table-column>
        </el-table>
        <!-- 分页 -->
        <el-pagination
          v-model:current-page="pageParam.currentPage"
          v-model:page-size="pageParam.pageSize"
          background
          small
          @size-change="fetchData"
          @current-change="fetchData"
          class="mt-4"
          layout="total,  prev, pager, next"
          :total="total"
        />
      </el-card>
    </div>
    <br />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getScheduleList, deleteShceduleById } from '@/api/schedule'

onMounted(() => {
  fetchData()
})

// ------------删除排班------------
const delSchedule = id => {
  ElMessageBox.confirm('您确定要永久删除此条数据吗?', 'Warning', {
    cancelButtonText: '取消',
    confirmButtonText: '确认',
    type: 'warning',
  }).then(async () => {
    const { code } = await deleteShceduleById(id)
    if (code === 200) {
      // 提示信息
      ElMessage.success('删除成功')
      // 刷新页面
      fetchData()
    }
  })
}

// ------------条件分页查询------------
// 查询条件对象
const scheduleDto = ref({
  doctorName: '',
  workDate: '',
  workTime: '',
})
// 分页数据对象
const pageParam = ref({
  currentPage: 1,
  pageSize: 10,
})

// 总页数
const total = ref(0)
// 数据集合
const list = ref([])

// 重置检索条件
const resetData = () => {
  scheduleDto.value = {}
  fetchData()
}
// 条件查询
const querySchedule = () => {
  fetchData()
}

const fetchData = async () => {
  const { code, message, data } = await getScheduleList(
    pageParam.value.currentPage,
    pageParam.value.pageSize,
    scheduleDto.value
  )
  list.value = data.content
  total.value = data.totalElements
}

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
    .post(
      'http://111.229.164.96:8090/admin/hosp/schedule/saveSchedule',
      formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      }
    )
    .then(response => {
      console.log('data', response.data)
      if (response.data.code === 200) {
        // 提示信息
        ElMessage.success('上传成功')
        fetchData()
      } else {
        // 提示信息
        ElMessage.error('上传失败')
      }
    })
  // 刷新数据
  fetchData()
}
</script>

<style scoped>
</style>