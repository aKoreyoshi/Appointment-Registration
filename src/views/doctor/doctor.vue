<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getDoctorList, deleteDoctorById } from '@/api/doctor'
import { getDepartment } from '@/api/department'

onMounted(async () => {
  // 获取科室信息
  console.log('data', await getDepartment())
  department.value = traverseOptions(await getDepartment())
  fetchData()
})

// ------------删除医生信息------------
const delSchedule = id => {
  ElMessageBox.confirm('您确定要永久删除此条数据吗?', 'Warning', {
    cancelButtonText: '取消',
    confirmButtonText: '确认',
    type: 'warning',
  }).then(async () => {
    console.log('id', id)
    const { code } = await deleteDoctorById(id)
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
const doctorDto = ref({
  doctorId: '',
  doctorName: '',
  departmentName: [],
})
const department = ref([])
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
  doctorDto.value = {
    doctorId: '',
    doctorName: '',
    departmentName: [],
  }
  fetchData()
}
// 条件查询
const querySchedule = () => {
  fetchData()
}
// 过滤数据
function traverseOptions(options) {
  return options.map(item => {
    const { departmentName, children } = item
    const newItem = { value: departmentName, label: departmentName }
    if (children && children.length > 0) {
      newItem.children = traverseOptions(children)
    }
    return newItem
  })
}

const fetchData = async () => {
  doctorDto.value.departmentName =
    doctorDto.value.departmentName.length > 0
      ? doctorDto.value.departmentName[1]
      : ''
  console.log('doctorDto', doctorDto.value.departmentName[1])
  const { code, data } = await getDoctorList(
    pageParam.value.currentPage,
    pageParam.value.pageSize,
    doctorDto.value
  )
  // console.log(data)
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
    .post('http://111.229.164.96:8090/admin/hosp/doctor/saveDoctor', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
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

<template>
  <div class="app-container">
    <div>
      <span>上传医生信息:&nbsp;</span>
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
              <el-form-item label="医生编号:">
                <el-input
                  name="departmentName"
                  v-model="doctorDto.doctorId"
                  placeholder="请输入医生编号"
                  style="width: 80%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="医生名称:">
                <el-input
                  name="bigDepartmentName"
                  v-model="doctorDto.doctorName"
                  placeholder="请输入医生名称"
                  style="width: 70%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="所属科室:">
                <el-cascader
                  style="width: 250px"
                  v-model="doctorDto.departmentName"
                  placeholder="请选择科室"
                  :options="department"
                  @change="handleChange"
                  :show-all-levels="false"
                />
              </el-form-item>
            </el-col>
            <el-col :span="5" style="margin-left: 30px; margin-top: 5px">
              <el-button type="primary" size="small" @click="querySchedule">
                搜索
              </el-button>
              <el-button size="small" @click="resetData">重置</el-button>
            </el-col>
          </el-row>
        </el-form>
        <!-- 展示排班记录 -->
        <el-table :data="list" style="width: 100%">
          <el-table-column align="center" prop="id" label="序号" width="70" />
          <el-table-column
            align="center"
            prop="doctorId"
            label="医生编号"
            width="120"
          />
          <el-table-column
            align="center"
            prop="doctorName"
            label="医生姓名"
            width="120"
          />
          <el-table-column
            align="center"
            prop="doctorTitle"
            label="医生职称"
            width="110"
          />
          <el-table-column
            align="center"
            prop="gender"
            label="性别"
            width="70"
          />
          <el-table-column
            align="center"
            prop="doctorPhone"
            label="联系电话"
            width="130"
          />
          <el-table-column
            align="center"
            prop="doctorSkill"
            label="医生技能"
            width="300"
          />
          <el-table-column
            align="center"
            prop="departmentName"
            label="所属科室"
            width="200"
          />
          <el-table-column
            align="center"
            prop="name"
            label="操作"
            width="130"
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

<style scoped>
</style>