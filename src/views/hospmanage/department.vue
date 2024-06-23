<template>
  <div class="app-container">
    <div>
      <span>上传科室信息:&nbsp;</span>
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
              <el-form-item label="科室名称:">
                <el-input
                  name="departmentName"
                  v-model="departmentDto.departmentName"
                  placeholder="请输入科室名称"
                  style="width: 80%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="大科室名称:">
                <el-input
                  name="bigDepartmentName"
                  v-model="departmentDto.bigDepartmentName"
                  placeholder="请输入科室名称"
                  style="width: 80%"
                />
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
            prop="departmentCode"
            label="科室编号"
            width="150"
          />
          <el-table-column
            align="center"
            prop="departmentName"
            label="科室名称"
            width="200"
          />
          <el-table-column
            align="center"
            prop="bigDepartmentCode"
            label="大科室编号"
            width="350"
          />
          <el-table-column
            align="center"
            prop="bigDepartmentName"
            label="大科室名称"
            width="150"
          />
          <el-table-column
            align="center"
            prop="intro"
            label="科室描述"
            width="250"
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

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getDepartmentList, deleteDepartmentById } from '@/api/department'

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
    console.log('id', id)
    const { code } = await deleteDepartmentById(id)
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
const departmentDto = ref({
  departmentName: '',
  bigDepartmentName: '',
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
  departmentDto.value = {}
  fetchData()
}
// 条件查询
const querySchedule = () => {
  fetchData()
}

const fetchData = async () => {
  const { code, message, data } = await getDepartmentList(
    pageParam.value.currentPage,
    pageParam.value.pageSize,
    departmentDto.value
  )
  console.log(data)
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
      'http://111.229.164.96:8090/admin/hosp/department/saveDepartment',
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