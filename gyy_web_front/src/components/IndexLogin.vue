<template>
    <div class="index-login">
        <el-button type="text" @click="openLoginDialog()">登录</el-button>
        <el-dialog title="登录" :visible.sync="dialogFormVisible" width="35%" @keyup.enter.native="registeAccount()">
          <el-form :model="form" ref="indexloginForm" :rules="rules" class="login-form" style="padding:12px 24px 36px">
            <el-form-item label="账号" :label-width="formLabelWidth" prop="account">
              <el-input autocomplete="off" v-model="form.account" placeholder="用户名或邮箱"></el-input>
            </el-form-item>
            <el-form-item label="密码" :label-width="formLabelWidth" prop="password">
              <el-input autocomplete="off" v-model="form.password" placeholder="密码" show-password></el-input>
            </el-form-item>
            <el-checkbox v-model="checked" class="remeberme">记住我</el-checkbox>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="registeAccount()" :loading="isLoading">登 录</el-button>
            <el-button @click="clearDialog()">取 消</el-button>
          </div>
        </el-dialog>
        <!-- <el-button type="text" @click="openRegisterPage()">注册</el-button> -->
    </div>
</template>

<script>
export default {
  name: 'IndexLogin',
  // watch: function () {
  //   this.disabled = this.$store.state.loginFormDisabled
  // },
  mounted () {
    const remeberme = JSON.parse(localStorage.getItem('remeberme'))
    if (remeberme) {
      this.form.account = localStorage.getItem('account') || ''
      this.form.password = localStorage.getItem('password') || ''
      this.checked = true
    }
  },
  data () {
    return {
      dialogFormVisible: false,
      form: {
        password: '',
        account: ''
      },
      disabled: false,
      formLabelWidth: '50px',
      isLoading: false,
      rules: {
        account: [
          { required: true, message: '用户名不能为空!', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '密码不能为空!', trigger: 'blur' },
          { min: 6, message: '密码格式错误!', trigger: 'blur' }
        ]
      },
      checked: false
    }
  },
  methods: {
    openLoginDialog () {
      this.dialogFormVisible = true
    },
    openRegisterPage () {
      this.$router.push('/regist')
    },
    registeAccount () {
      this.isLoading = true
      this.$refs.indexloginForm.validate(async (valid) => {
        if (!valid) {
          this.$message.error('账号或密码不能为空！')
          this.isLoading = false
          return false
        }

        const data = {
          userName: this.form.account,
          password: this.form.password
        }
        try {
          const res = await this.$axios({
            headers: {
              'Content-Type': 'application/json'
            },
            method: 'post',
            url: '/user/login',
            data: data
          })
          if (res.data.code === 200) {
            const token = res.data.data.token
            // 设置记住我选项
            localStorage.setItem('remeberme', JSON.stringify(this.checked))
            // 设置token
            if (this.checked) {
              localStorage.setItem('token', token)
              localStorage.setItem('account', this.form.account)
              localStorage.setItem('password', this.form.password)
            } else {
              sessionStorage.setItem('token', token)
            }
            // 向Vuex中存储用户的username
            // this.$store.commit('setUserName', data.userName)
            // this.$store.commit('setLoginState', true)
            const resp = await this.$axios.post('/user/getUserInfo')
            const userInfo = resp.data ? resp.data : resp
            this.$store.commit('setUserInfo', userInfo)
            // window.sessionStorage.setItem('store', JSON.stringify(this.$store.state))
            console.log('userInfo', userInfo)
            window.localStorage.setItem('nickname', userInfo.nickname || '')
            window.localStorage.setItem('avatar', userInfo.icon || '')

            setTimeout(() => {
              this.dialogFormVisible = false
              this.$router.go(0)
            }, 500)
            this.$message({
              message: '登录成功!',
              type: 'success',
              duration: 1200
            })
          } else {
            this.$message({
              message: '账号密码错误!',
              type: 'error',
              duration: 1200
            })
          }
        } catch (error) {
          console.log('login error', error)
        } finally {
          this.isLoading = false
        }
      })
    },
    clearDialog () {
      this.dialogFormVisible = false
      this.form.account = ''
      this.form.password = ''
    }
  }
}
</script>

<style lang="scss">
.index-login {
  .el-button--text {
    margin-right: 20px;
    font-size: 18px;
    color: #333;
  }
}

.login-form {
  padding-bottom: 10px !important;
}
</style>
