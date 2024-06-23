<script setup>
import { ref, onMounted } from 'vue'
import { savePatient, getAreaAndNation } from '@/api/user/patient'
import { getCertificateTypes } from '@/api/user/user'
import router from '@/router/index';
import { ElMessage } from 'element-plus'



onMounted(() => {
  fetchCertificateTypes()
  fetchData()
})

// 验证规则
const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'blur' }],
  birthday: [{ required: true, message: '请选择出生日期', trigger: 'blur' }],
  certificateType: [{ required: true, message: '请选择证件类型', trigger: 'blur' }],
  ethnicGroup: [{ required: true, message: '请选择民族', trigger: 'blur' }],
  certificateNo: [{ required: true, message: '请输入证件号码', trigger: 'blur' }],
  contactName: [{ required: true, message: '请输入联系人姓名', trigger: 'blur' }],
  contactPhone: [{ required: true, message: '请输入联系人手机号', trigger: 'blur' }]
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
// 定义地区和民族数据
const districtList = ref([])
const nationList = ref([])

// 过滤数据
function traverseOptions(options) {
  return options.map(item => {
    const { name, children } = item;
    const newItem = { value: name, label: name };
    if (children && children.length > 0) {
      newItem.children = traverseOptions(children);
    }
    return newItem;
  });
}

// 定义就诊人信息
const patient = ref({
  name: '',
  gender: '',
  birthday: '',
  certificateType: '',
  certificateNo: '',
  ethnicGroup: '',
  district: '',
  address: '',
  contactName: '',
  contactPhone: ''
})
// fetchData函数
const fetchData = async () => {
  const { code, data } = await getAreaAndNation()
  if (code === 200) {
    districtList.value = traverseOptions(data.districts[0].children)
    // console.log('民族', data.ethnicGroups)
    nationList.value = data.ethnicGroups[0].children
  }

}

// 点击取消，返回到就诊人管理页面
const onCancel = () => {
  router.push('/patient')
}


// 添加
const onSubmit = async () => {

  // 可以再做完善，看后续情况
  if (!patient.value) {
    ElMessage.error('请先填写信息');
    router.push('/add');
  }
  console.log('patient', patient.value)
  const { code } = await savePatient(patient.value)
  if (code === 200) {
    // 提示信息
    ElMessage.success('添加成功')
    // 跳转到就诊人管理页面
    router.push('/patient')
  }
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
          <el-menu-item index="/patient" style="background-color: rgb(236,245,255);">
            <el-icon>
            </el-icon>
            <span>就诊人管理</span>
          </el-menu-item>
          <el-menu-item index="/order">
            <el-icon></el-icon>
            <span>挂号订单</span>
          </el-menu-item>
        </el-menu>
      </el-col>
      <!-- 右侧页面 -->
      <el-col :span="20">
        <div style="width: 1100px;height: 500px; position: relative;" class="content">
          <div class="title">
            <span>请为您要添加的就诊人完善信息！</span>
          </div>
          <div class="form-data" style="margin-top: 5%;margin-left: 15%;">
            <el-form :inline="true" :model="patient" class="demo-form-inline" label-width="100px" ref="ruleFormRef"
              :rules="rules">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="就诊人姓名" prop="name">
                    <el-input v-model="patient.name" placeholder="请输入姓名" clearable style="width: 250px;" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="性别" prop="gender">
                    <el-select v-model="patient.gender" placeholder="请选择性别" clearable style="width: 250px;">
                      <el-option label="男" value="0" />
                      <el-option label="女" value="1" />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="出生日期" prop="birthday">
                    <el-date-picker style="width: 250px;" v-model="patient.birthday" type="date" placeholder="请选择一个日期"
                      clearable />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="民族" prop="ethnicGroup">
                    <el-select style="width: 250px;" v-model="patient.ethnicGroup" placeholder="请选择民族">
                      <el-option v-for="item in nationList" :key="item.id" :label="item.label"
                        :value="item.name"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="证件类型" prop="certificateType">
                    <el-select style="width: 250px;" v-model="patient.certificateType" placeholder="请选择证件类型">
                      <el-option v-for="item in certificateTypes" :key="item.id" :label="item.label"
                        :value="item.name"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="证件号" prop="certificateNo">
                    <el-input style="width: 250px;" v-model="patient.certificateNo" placeholder="请输入证件号" clearable />
                  </el-form-item>
                </el-col>

              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item label="地区">
                    <div class="m-4">
                      <el-cascader style="width: 250px;" v-model="patient.district" placeholder="请选择地区"
                        :options="districtList" @change="handleChange" />
                    </div>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item style="width: 820px;" label="详细地址">
                    <el-input v-model="patient.address" placeholder="请输入详细地址" clearable />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="联系人姓名" prop="contactName">
                    <el-input style="width: 250px;" v-model="patient.contactName" placeholder="请输入联系人姓名" clearable />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="联系人电话" prop="contactPhone">
                    <el-input style="width: 250px;" v-model="patient.contactPhone" placeholder="请输入联系人电话" clearable />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24" style="display: flex; justify-content: center; align-items: center;">
                  <el-form-item>
                    <el-button type="default" @click="onCancel">取消</el-button>
                    <el-button type="primary" @click="onSubmit">点击添加</el-button>
                  </el-form-item>
                </el-col>
              </el-row>
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

/* 按钮 */
.el-button {
  margin-top: 15px;
  width: 100px;
}

.title {
  font-size: 25px;
  font-weight: bold;
  font-family: "KaiTi", "STKaiti", "楷体", serif;
  color: rgb(64, 158, 255);
  margin: 0 auto;
  margin-top: 5%;
  margin-left: 35%;
}
</style>
