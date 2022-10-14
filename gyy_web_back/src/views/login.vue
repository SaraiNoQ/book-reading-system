<template>
<div class="login">
  <div class="booo">
      <el-form :rules="rules" ref="loginForm" :model="loginForm" class="logincon" @keyup.enter.native="sumbitLogin">
      <h3 class="loginTittle">后台系统登陆</h3>

      <el-form-item prop="username">
        <el-input type="text" auto-complete="false" v-model="loginForm.username" placeholder="请输入用户名"></el-input>
      </el-form-item>

      <el-form-item prop="password">
        <el-input type="text" auto-complete="false" v-model="loginForm.password" placeholder="请输入密码" show-password ></el-input>
      </el-form-item>

      <!--
    <el-form-item prop="code">
        <el-input type="text" auto-complete="false" v-model="loginForm.code" placeholder="点击图片更换验证码" style="width:250px;margin-right: 5px"></el-input>
      </el-form-item>
    -->
      <el-checkbox v-model="checked" class="remeberme">记住我</el-checkbox>

      <el-button type="primary" style="width:100%" @click="sumbitLogin" :loading="buttonLoading">登录</el-button>


    </el-form>
  </div>
  
</div>
</template>

<script>
export default {
  name: "login",
  data() {
    return {
      captchaUrl: '',
      loginForm: {
        password: '',
        username: '',
      },
      checked: false,
      rules: {
        username: [{required: true, message: "请输入用户名", trigger: 'blur'}],
        password: [{required: true, message: "请输入密码", trigger: 'blur'}],
        // code: [{required:true,message:"请输入验证码",trigger:'blur'}]
      },
      buttonLoading: false // 按钮加载状态
    }
  },
  async created() {
    const tokenState = localStorage.getItem('token') || ''
    if (tokenState !== '') {
      try {
        const res = await this.$axios.post('/book/getDynasty')
        if (res.code === 401) {
          return
        }
        this.$router.replace('/home')
      } catch (error) {
        this.$message.error('网络异常！')
      }
    }
    const remeberme = JSON.parse(localStorage.getItem('remeberme'))
    if (remeberme) {
      this.loginForm.username = localStorage.getItem('username')
      this.loginForm.password = localStorage.getItem('password')
      this.checked = true
    }
  },
  methods: {
    sumbitLogin() {
      this.buttonLoading = true
      this.$refs.loginForm.validate(async (valid) => {
        if (valid) {
          const data = {
            userName: this.loginForm.username,
            password: this.loginForm.password,
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
            if (res.code === 200) {
              const token = res.data.token
              // 设置记住我选项
              localStorage.setItem('remeberme', JSON.stringify(this.checked))
              // 设置token
              if (this.checked) {
                localStorage.setItem('token', token)
                localStorage.setItem('username', this.loginForm.username)
                localStorage.setItem('password', this.loginForm.password)
              } else {
                sessionStorage.setItem('token', token)
              }
              this.$router.replace('/home')
              this.$message.success('登陆成功！')
            } else if (res.code === 401) {
              this.$message.error('密码错误！')
            } else {
              this.$message.error('权限不足！请使用管理员账号登陆！')
            }
          } catch (error) {
            this.$message.error('网络错误！')
          } finally {
            this.buttonLoading = false
          }
        } else {
          this.$message.error('账号或密码不能为空！')
          this.buttonLoading = false
          return false
        }
      })
      /*this.$refs.loginForm.validate((valid) => {
        // 判断是否输入正确，后期加上从后台获取、比对用户信息
        if (valid) {
          this.loading = true
          const formData = new FormData()
          formData.append('username', this.indexLoginForm.username)
          formData.append('password', this.indexLoginForm.password)
          postRequest('/login', formData).then(resp => {
            if (resp.status === 'success') {
              // 向Vuex中存储用户的username
              this.$store.commit('setUserName', formData.get('username'))
              this.$store.commit('setLoginState', true)
              sessionStorage.setItem('store', JSON.stringify(this.$store.state))
              setTimeout(() => {
                this.$router.replace('/home')
                this.loading = false
              }, 300)
              this.$message({
                message: '登录成功!',
                type: 'success',
                duration: 1200
              })
            } else {
              this.loading = false
              this.$message({
                message: '登录失败!请检查账号密码',
                type: 'error',
                duration: 1500
              })
            }
          })
        } else {
          return false
        }
      });*/
    },

  },
  mounted() {
  }
}
</script>

<style lang="scss" scoped>
.login {
  width: calc(100vw);
  height: calc(100vh);
  background-size: cover;
  background-position: center;
  position: relative;
  background-image: url('../assets/bgimg.png');
  .booo {
    position: absolute;
    top: calc(50vh - 150px);
    left: calc(50vw - 225px)
  }
}
.logincon{
  border-radius: 15px;
  background-clip: padding-box;
  margin: auto;
  width: 380px;
  padding : 15px 35px 15px 35px;
  background:#fff;
  border: 1px solid #eaeaea;
  box-shadow:  0 0 25px #cac6c6;
}
.loginTittle{
  margin:0px auto 40px auto;
  text-align: center;

}
.remeberme{
  text-align: left;
  margin: 0px 0px 15px 0px;
}

.el-alert__content{
  display: flex;
  align-items: center;
}


</style>