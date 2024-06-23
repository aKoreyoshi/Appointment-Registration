<!--
 * @Descripttion: 
 * @version: 
 * @Date: 2021-04-20 11:06:21
 * @LastEditors: huzhushan@126.com
 * @LastEditTime: 2022-09-24 18:18:43
 * @Author: huzhushan@126.com
 * @HomePage: https://huzhushan.gitee.io/vue3-element-admin
 * @Github: https://github.com/huzhushan/vue3-element-admin
 * @Donate: https://huzhushan.gitee.io/vue3-element-admin/donate/
-->
<template>
  <div class="app-container">
    <div>
      <el-card style="max-width: 100%; height: 70px; margin-bottom: 10px">
        <span>
          <el-button type="success" :icon="Refresh" @click="refresh">
            刷新
          </el-button>
          <el-button type="primary" @click="judgeData">导入数据</el-button>
          <el-button type="primary" @click="exportData">导出数据</el-button>
        </span>
      </el-card>
    </div>
    <div>
      <el-card style="max-width: 100%">
        <el-table
          :data="dictData"
          style="width: 100%"
          row-key="id"
          border
          :expand-row-keys="expandRowKeys"
          fit
          lazy
        >
          <el-table-column prop="name" label="名称" />
          <el-table-column prop="dictCode" label="编码" />
          <el-table-column prop="value" label="值" />
          <el-table-column prop="createTime" label="创建时间" />
        </el-table>
      </el-card>
    </div>
    <!-- 上传文件框 -->
    <el-dialog
      v-model="dialogVisible"
      title="导入数据"
      width="600px"
      :before-close="handleClose"
    >
      <el-form label-position="left">
        <el-form-item>
          <el-upload
            :multiple="false"
            :on-progress="onProgress"
            :on-success="onUploadSuccess"
            :action="'http://111.229.164.96:8080/admin/hosp/dict/importData'"
            class="upload-demo"
          >
            <el-button size="small" type="primary">点击上传</el-button>
            <template #tip>
              <div class="el-upload__tip">
                只能上传Excel文件，且大小不超过500KB
              </div>
            </template>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
  
  
  
  <script setup>
// import { useUserStore } from '@/pinia/modules/app'
import { ref, onMounted, computed } from 'vue'
import { Refresh } from '@element-plus/icons-vue'
import { getDict, isEmpty } from '@/api/dict'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ElLoading } from 'element-plus'
import axios from 'axios'

onMounted(() => {
  expandRowKeys.value = [1, 30000]
  fetchData()
})

// ------------上传数据------------
const dialogVisible = ref(false)
// 上传之前判断是否已经存在数据
const judgeData = async () => {
  const { data } = await isEmpty()
  console.log(data)
  if (data) {
    // 数据为空，可以导入
    // 打开上传弹窗
    dialogVisible.value = true
  } else {
    // 已经存在数据，不支持继续上传
    // 提示警告信息
    ElMessage.warning('当前已经存在数据,不支持继续上传!')
  }
}
// 长传过程，为保证体验，进行一个缓冲加载
const onProgress = () => {
  const loading = ElLoading.service({
    lock: true,
    text: '数据正在上传中...',
  })
  setTimeout(() => {
    loading.close()
  }, 7000)
}
// 上传成功后调用
const onUploadSuccess = () => {
  // 提示信息
  ElMessage.success('上传成功')
  // 关闭弹窗
  dialogVisible.value = false
  // 刷新页面
  fetchData()
}

// ------------导出数据------------
const exportData = async () => {
  // await axios.get('http://localhost:8080/admin/hosp/dict/exportData')
  // .then(resp => {
  //   if (resp.data.code === 200) {
  //      // 提示信息
  //      ElMessage.success('上传成功')
  //   }
  // })
  window.location.href = 'http://111.229.164.96:8080/admin/hosp/dict/exportData'
}

// ------------刷新页面------------
const refresh = () => {
  window.location.reload()
}

// ------------获取数据字典------------
const dictData = ref([])
const expandRowKeys = ref([])
// 判断子树
// const getChildrens = (tree, treeNode, resolve) => {
//   dict.dictList(tree.id).then(response => {
//     resolve(response.data)
//   })
// }
const fetchData = async () => {
  const { code, data } = await getDict()
  dictData.value = data
  console.log('code', code)
  console.log('data', data)
}
</script>
  
  
  <style lang="scss" scoped>
</style>
  
  
  