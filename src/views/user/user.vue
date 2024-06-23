<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getUserList, updateStatus, authUser } from '@/api/user'

onMounted(() => {
  fetchData()
})

// ======================定义数据格式========================
const userList = ref(null)
const userStatusDto = ref({
  phone: '',
  status: '',
})

// ====================更新用户状态=======================
const handleLock = async user => {
  userStatusDto.value.phone = user.phone
  if (user.status === 0) {
    userStatusDto.value.status = 1
  } else {
    userStatusDto.value.status = 0
  }
  const { code } = await updateStatus(userStatusDto.value)
  if (code === 200) {
    ElMessage.success('更新用户状态成功')
    fetchData()
  } else {
    ElMessage.error('更新失败')
  }
}

// ===================完成用户认证======================
const handleAuth = async user => {
  console.log('手机号', user.phone)
  const { code } = await authUser(user.phone)
  if (code === 200) {
    ElMessage.success('用户认证成功')
    fetchData()
  }
}
const handleNoAuth = () => {
  ElMessage.warning('用户尚未提交认证信息,暂不能认证!')
  fetchData
}

// ==================fetchData函数====================
const fetchData = async () => {
  const { code, data } = await getUserList()
  if (code === 200) {
    userList.value = data
  }
  console.log('user', userList.value)
}
</script>

<template>
  <div style="margin-left: 10px">
    <span style="font-weight: 600; font-size: 20px">用户管理</span>
  </div>
  <el-card style="margin-top: 10px; width: 100%">
    <el-table :data="userList" border>
      <el-table-column prop="name" label="用户姓名" width="150" />
      <el-table-column
        prop="phone"
        label="用户电话"
        width="150"
      ></el-table-column>
      <el-table-column
        prop="certificateType"
        label="证件类型"
        width="150"
      ></el-table-column>
      <el-table-column
        prop="certificateNo"
        label="证件号"
        width="200"
      ></el-table-column>
      <el-table-column
        prop="status"
        label="账号状态"
        width="100"
        #default="scope"
      >
        {{ scope.row.status === 0 ? '可用' : '禁用' }}
      </el-table-column>
      <el-table-column
        prop="authStatus"
        label="认证状态"
        width="150"
        #default="scope"
      >
        {{
          scope.row.authStatus === 0
            ? '未认证'
            : scope.row.authStatus === 1
            ? '认证中'
            : '认证成功'
        }}
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" />
      <el-table-column label="操作" #default="scope">
        <div style="display: inline-block">
          <div class="noAuth" v-if="scope.row.authStatus === 0">
            <el-button type="primary" link @click="handleNoAuth">
              认证
            </el-button>
          </div>
          <div class="noAuth" v-if="scope.row.authStatus === 1">
            <el-button type="primary" link @click="handleAuth(scope.row)">
              认证
            </el-button>
          </div>
        </div>
        <el-button
          style="display: inline-block; margin-left: 10px"
          type="primary"
          link
          @click="handleLock(scope.row)"
        >
          {{ scope.row.status === 0 ? '锁定' : '取消锁定' }}
        </el-button>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<style scoped>
</style>