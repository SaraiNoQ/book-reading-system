<template>
<div>
  <div class="head2" style="margin-bottom: 30px;height: 60px">
    <el-card style="display: flex;background-color: #f4cf98">
      <el-row style="">
        <el-button @click="initTableData();resetForm('searchList')" icon="el-icon-refresh-left" circle title="刷新列表"></el-button>
        <el-button @click="searchUser()" type="primary" icon="el-icon-search" circle title="搜索用户"></el-button>
        <el-button @click="userDelete()" type="danger" icon="el-icon-delete" circle title="删除用户"></el-button>
        <el-button @click="userReset()" type="success" icon="el-icon-user-solid" circle title="重置密码，初始密码为'mima12345'"></el-button>
        <el-button @click="showPrivi" type="success" icon="el-icon-s-custom" circle title="修改权限"></el-button>
        <el-button @click="showAddUser" type="success" icon="el-icon-plus" circle title="新增用户"></el-button>
      </el-row>
    </el-card>

  </div>

  <!-- 加载区 -->
  <div v-loading="loading">
    <div>
      <el-card style="margin-bottom: 10px;background-color: white">
        <el-form :inline="true" ref="searchList" :model="searchList" class="demo-form-inline">
          <el-form-item label="昵称" prop="nickName">
            <el-input v-model="searchList.nickName" placeholder="请输入内容" clearable ></el-input>
          </el-form-item>
          <el-form-item label="用户名" prop="userName">
            <el-input v-model="searchList.userName" placeholder="请输入内容" clearable ></el-input>
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="searchList.email" placeholder="请输入内容" clearable ></el-input>
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <div>
      <el-table
          :data="tableData"
          border
          style="width: 100%"
          highlight-current-row
          @current-change="handleCurrentChangeTable">
        <el-table-column
            label="序号"
            align="left"
            width="100">
          <template slot-scope="scope">
            {{ (scope.$index+1)+(page-1)*size }}
          </template>
        </el-table-column>
        <el-table-column
            prop="nickname"
            label="昵称"
            align="left"
            width="200">
        </el-table-column>
        <el-table-column
            prop="username"
            label="用户名"
            align="left"
            width="200px">
        </el-table-column>
        <el-table-column
            prop="email"
            label="邮箱"
            align="left"
            width="400px">
        </el-table-column>
        <el-table-column
            prop="usertype"
            label="用户类型"
            align="left"
            width="220px"
            :formatter="stateFormat">
        </el-table-column>
      </el-table>
    </div>

    <div style="display: flex;justify-content: center">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="page"
          :page-sizes="[ 10, 20, 50]"
          :page-size="100"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <!-- 功能区 -->
    <el-dialog
      width="610px"
      title="新增用户"
      :visible.sync="addUserDialogVisible"
    >
      <el-form :model="addUserForm" :rules="rules" ref="addForm" >
        <el-form-item label="用户名" :label-width="formLabelWidth" prop="username">
          <el-input style="width: 75%" type="text" v-model="addUserForm.username" autocomplete="off" placeholder="请输入英文字符"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" :label-width="formLabelWidth" prop="email">
          <el-input style="width: 75%" v-model="addUserForm.email" autocomplete="off" placeholder="请输入邮箱号"></el-input>
        </el-form-item>
        <el-form-item label="密码" :label-width="formLabelWidth" prop="password">
          <el-input style="width: 75%" v-model="addUserForm.password" autocomplete="off" placeholder="长度大于8字符、同时至少包含英文和数字"></el-input>
        </el-form-item>
        <el-form-item label="用户类型" :label-width="formLabelWidth"  prop="type">
          <el-select v-model="addUserForm.type" placeholder="请选择">
            <el-option
                v-for="item in optionsList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addUserDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addUser" :loading="addUserLoading">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 删除区 -->
    <el-dialog
        title="提示"
        :visible.sync="dialogOneVisible"
        width="30%"
    >
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogOneVisible = false">取 消 删 除</el-button>
        <el-button type="primary" @click="dialogOneVisible = false ;userDelete();initTableData()">确 定 删 除</el-button>
      </span>
    </el-dialog>

    <!--搜索区-->
    <el-dialog title="信息" :visible.sync="dialogTableVisible">
      <el-table :data="gridData">
        <el-table-column property="id" label="ID"></el-table-column>
        <el-table-column property="nickname" label="昵称" width="150"></el-table-column>
        <el-table-column property="username" label="用户名" width="150"></el-table-column>
        <el-table-column property="email" label="E-Mail"></el-table-column>
      </el-table>
    </el-dialog>

    <!-- 重置区 -->
    <el-dialog
      title="提示"
      :visible.sync="dialogTwoVisible"
      width="30%"
    >
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogTwoVisible = false">取 消 重 置</el-button>
        <el-button type="primary" @click="dialogTwoVisible = false; userReset()">确 定 重 置</el-button>
      </span>
    </el-dialog>

    <el-dialog
      :title="'修改用户“' + searchList.userName + '”的权限'"
      :visible.sync="priviVisible"
      width="500px"
    >
      <el-form>
        <el-form-item label="用户类型" :label-width="formLabelWidth"  prop="type">
          <el-select v-model="userType" placeholder="请选择">
            <el-option
                v-for="item in optionsList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="priviVisible = false" :loading="changePriviLoading">取 消</el-button>
        <el-button type="primary" @click="changePrivi" :loading="changePriviLoading">确 定</el-button>
      </span>
    </el-dialog>

    </div>
  </div>
</template>

<script>
import Qs from "qs";
const PEMISSION_ERROR = '无删除权限(请使用更高权限账户完成操作)'

export default {
  name: "User",
  data() {
    const validateEmail = (rule, value, callback) => {
      if (!/^([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/.test(value)) {
        callback(new Error('邮箱格式错误！'))
      } else {
        callback()
      }
    }
    const validatePsw = (rule, value, callback) => {
      if (value.length < 8 || value.length > 16) {
        callback(new Error('长度在 8 到 16 个字符！'))
      } else if (!/^(?![a-zA-Z]+$)(?!\\d+$)(?![^\\da-zA-Z\\s]+$).{6,15}$/.test(value)) {
        callback(new Error('密码必须包含数字、字母两种！'))
      } else {
        callback()
      }
    }
    return {
      tableData: [],
      total: 0,
      size: 10,
      page: 1,
      searchList: {
        userName: '',
        nickName: '',
        email: '',
      },
      dialogOneVisible: false,
      dialogTwoVisible: false,
      dialogTableVisible: false,
      gridData: [],
      userTotal: 1,
      deleteData: [],
      loading: true, // 工作区加载状态
      clickId: '',
      // 用户类型
      optionsList: [{
        label: '普通用户',
        value: 0,
      }, {
        label: '管理员',
        value: 1,
      }],
      // 添加用户
      addUserDialogVisible: false,
      addUserForm: {
        username: '',
        password: '',
        email: '',
        type: 0,
      },
      formLabelWidth: '120px',
      addUserLoading: false,
      rules: {
        username: [
          { required: true, message: '用户名不能为空！', trigger: 'change' }
        ],
        password: [
          { required: true, message: '密码不能为空！', trigger: 'change' },
          { validator: validatePsw, trigger: 'change' }
        ],
        email: [
          { required: true, message: '邮箱不能为空！', trigger: 'change' },
          { validator: validateEmail, trigger: 'change' }
        ]
      },
      priviVisible: false,
      userType: '',
      changePriviLoading: false
    }
  },
  mounted () {
    this.$axios.get('/user/getUser/' + this.page + '/' + this.size)
      .then(resp => {
        if (resp) {
          this.loading = false // 工作区加载状态
          this.tableData = resp.data
          this.total = resp.total
        }
      })
  },

  methods:{
   handleSizeChange(newSize){
      this.size = newSize;
      this.initTableData();
    },
    handleCurrentChange(newPage){
      this.page = newPage;
      this.initTableData();
    },
    // 点击列表
    handleCurrentChangeTable (val) {
      console.log(val)
      try {
        this.clickId = val.id
        this.searchList.userName = val.username
        this.searchList.nickName = val.nickname
        this.searchList.email = val.email
      } catch (error) {
        console.log('click error!', error)
      }
    },
    async initTableData() {
      this.loading = true
      const resp = await this.$axios.get('/user/getUser/' + this.page + '/' + this.size)
      if (resp) {
        this.tableData = resp.data ? resp.data : resp
        this.total = resp.total
      }
      this.loading = false
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done();
        })
        .catch(_ => {})
    },
    searchUser() {
      if (this.searchList.userName === '' && this.searchList.nickName === '' && this.searchList.email === '') {
        this.$message({
          message: '请输入搜索条件！',
          type: 'warning'
        })
        return
      }
      const formData = new FormData()
      formData.append('username', this.searchList.userName.trim())
      formData.append('nickname', this.searchList.nickName.trim())
      formData.append('email', this.searchList.email.trim())
      formData.append('page', this.page)
      formData.append('size', this.size)
      this.$axios.post('/user/searchForUsers/1/2', formData).then((resp)=>{
        if(resp){
          this.tableData=resp.data;
          this.total=resp.data.length;
        }
        else{
          this.$message({
            type: 'error',
            message: '查询失败!'
          })
        }
      })
    },
    userDelete() {
      if (this.clickId === '') {
        this.$message({
          message: '请选择要删除的用户！',
          type: 'warning'
        })
        return
      }
      this.$confirm('此操作将永久删除该用户, 是否删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const formData = new FormData()
        formData.append('id', this.clickId)
        this.$axios({
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          },
          method: 'post',
          url: '/user/deleteUser',
          data: formData
        }).then((resp)=>{
          if(resp.status === 'success'){
            this.$message({
              type: 'success',
              message: '删除成功！'
            })
            this.clickId = ''
            this.searchList = {}
            this.initTableData()
          } else if(resp.status === PEMISSION_ERROR) {
            this.$message({
              type:'error',
              message: '权限不足！'
            })
          } else {
            this.$message({
              type:'error',
              message: '删除失败！'
            })
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    userReset() {
      if (!this.clickId) {
        this.$message({
          type: 'error',
          message: '重置用户不能为空！'
        })
        return
      }
      this.$confirm('此操作重置用户密码, 是否重置?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let data = {
          'username': this.searchList.userName,
        }
        this.$axios({
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          },
          method: 'post',
          url: '/user/resetUserpassword',
          data: Qs.stringify(data)
        }).then((resp)=>{
          if(resp.status === 'success'){
            this.$message({
              type: 'success',
              message: '重置成功!'
            })
          } else if (resp.status === PEMISSION_ERROR) {
            this.$message({
              type:'error',
              message: '权限不足！'
            })
          } else {
            this.$message({
              type:'error',
              message: '重置失败!'
            })
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消重置'
        });
      });
    },
    onchange(){
     this.deleteData.push(this.searchList)
    },
    stateFormat(row, column) {
      if (row.usertype === '0') {
        return '用户'
      } else if (row.usertype === '1') {
        return '管理员'
      } else if (row.usertype === '2')  {
        return '超级管理员'
      } else {
        return '其他'
      }
    },
    resetForm(formName) {
      this.$refs[formName].resetFields(); // 清空input标签中file的值
    },
    showAddUser() {
      this.addUserDialogVisible = true;
    },
    async addUser() {
      this.$refs.addForm.validate(async valid => {
        if (valid) {
          const info = await this.addCommonUser()
          if (info.substring(0, 7) !== 'success') {
            this.$message.error(info)
            this.addUserDialogVisible = false
            return
          }
          if(this.addUserForm.type === 1) {
            // TODO create admin
            const targetId = info.substring(7)
            const res = await this.$axios.get(`/user/addAdmin?targetId=${targetId}&userType=1`)
            if (res === 'success') {
              this.$message.success('创建管理员成功！')
              this.addUserDialogVisible = false
            } else {
              this.$message.error('创建用户成功。但权限授予失败！')
              this.addUserDialogVisible = false
            }
            return
          }
          this.$message.success('新增用户成功！')
          this.addUserDialogVisible = false
          this.initTableData()
        } else {
          console.log('error submit!')
        }
      })
    },
    async addCommonUser() {
      this.addUserLoading = true
      try {
        let params = `?email=${this.addUserForm.email}`
        if (this.addUserForm.username) {
          params += `&username=${this.addUserForm.username}`
        }
        if (this.addUserForm.password) {
          params += `&password=${this.addUserForm.password}`
        }

        const res = await this.$axios.post(`/user/addCommonUser${params}`)
        if (res.status === 'success') {
          return 'success' + res.userId
        }
        return res.status
      } catch (error) {
        console.log('create user failed!', error)
        return 'false'
      } finally {
        this.addUserLoading = false
      }
    },
    async addAdmin() {
      
    },
    async showPrivi() {
      if (this.clickId === '' || this.searchList.userName === '') {
        this.$message({
          message: '请选择要进行此项操作的用户！',
          type: 'warning'
        })
        return
      }

      this.priviVisible = true
    },
    async changePrivi () {
      this.changePriviLoading = true
      try {
        const targetId = this.clickId
        console.log(this.userType)
        const res = await this.$axios.get(`/user/addAdmin?targetId=${targetId}&userType=${this.userType}`)
        // resData = res.data || res
        if (res === 'success') {
          this.$message({
            message: '修改成功！',
            type: 'success'
          })
          this.initTableData()
        } else {
          this.$message({
            message: '您的权限不足！',
            type: 'error'
          })
        }
        this.priviVisible = false
      } catch (error) {
        console.error(error)
        this.$message({
          message: '修改失败！',
          type: 'error'
        })
      } finally {
        this.changePriviLoading = false
      }
    }
  },

  /*
  created() {
    this.initTableData()
  }
  */

}
</script>

<style scoped>
.head2{
  background-color: #faebd6;
}
.table{
  /*此元素会作为块级表格来显示（类似 <table>），表格前后带有换行符。*/
  display:table;
  /*border-collapse:collapse;*/
  border-collapse:separate;
  border:1px solid #ccc;
}
.table-caption{
  /*此元素会作为一个表格标题显示（类似 <caption>）*/
  display:table-caption;
  margin:0;
  font-size:16px;
}
.table-header-group{
  /*此元素会作为一个或多个行的分组来显示（类似 <thead>）。*/
  display:table-header-group;
  background:#eee;
  font-weight:bold;
}
.table-row-group{
  /*此元素会作为一个或多个行的分组来显示（类似 <tbody>）。*/
  display:table-row-group;
}
.table-footer-group{
  /*此元素会作为一个或多个行的分组来显示（类似 <tfoot>）。*/
  display:table-footer-group;
}
ul{
  list-style:none;
}
.table-row{
  /*此元素会作为一个表格行显示（类似 <tr>）。*/
  display:table-row;
}
.table-cell{
  /*此元素会作为一个表格单元格显示（类似 <td> 和 <th>）*/
  display:table-cell;
  padding:0 5px;
  border:1px solid #ccc;
}
.table-row-group .table-row:hover,
.table-footer-group .table-row:hover{
  background:#f6f6f6;
  color:green;
  font-weight: bold;
}

.table-column-group{
  /*此元素会作为一个或多个列的分组来显示（类似 <colgroup>）。*/
  display:table-column-group;
}
.table-column{
  /*此元素会作为一个单元格列显示（类似 <col>）*/
  display:table-column;
  width:100px;
}


</style>