<template>
    <div class="regist">
      <div class="login-tools">

        <el-form :model="form" :rules="rules" ref="loginForm" @keyup.enter.native="registeAccount">
          <el-form-item label="账号" :label-width="formLabelWidth" prop="username">
              <el-input autocomplete="off" v-model="form.username" placeholder="输入用户名称"></el-input>
          </el-form-item>
          <el-form-item label="邮箱" :label-width="formLabelWidth" prop="account">
              <el-input autocomplete="off" v-model="form.account" placeholder="输入邮箱注册"></el-input>
          </el-form-item>
          <el-form-item label="密码" :label-width="formLabelWidth" prop="password1">
              <el-input autocomplete="off" v-model="form.password1" placeholder="8-16位字符组成，区分大小写" show-password></el-input>
          </el-form-item>
          <el-form-item label="确认密码" :label-width="formLabelWidth" prop="password2">
              <el-input autocomplete="off" v-model="form.password2" placeholder="8-16位字符组成，区分大小写" show-password></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="registeAccount()" :loading="isLoading">注 册</el-button>
            <el-button @click="clearDialog()">取 消</el-button>
        </div>
      </div>
    </div>
</template>

<script>
import Axios from '../utils/api'
export default {
  name: 'Register',
  data () {
    const validateSecondPwd = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码！'))
      } else if (value.length < 8 || value.length > 16) {
        callback(new Error('长度在 8 到 16 个字符！'))
      } else if (this.form.password1 !== value) {
        callback(new Error('两次密码不一致！'))
      } else {
        callback()
      }
    }
    const validateEmail = (rule, value, callback) => {
      // eslint-disable-next-line no-control-regex
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
      dialogFormVisible: false,
      form: {
        username: '',
        password1: '',
        password2: '',
        account: ''
      },
      disabled: false,
      isLoading: false,
      formLabelWidth: '120px',
      rules: {
        username: [
          { required: true, message: '用户名不能为空！', trigger: 'blur' }
        ],
        password1: [
          { required: true, message: '密码不能为空！', trigger: 'blur' },
          { validator: validatePsw, trigger: 'blur' }
        ],
        password2: [
          {
            validator: validateSecondPwd,
            trigger: 'blur'
          },
          { required: true, message: '密码不能为空！', trigger: 'blur' }
        ],
        account: [
          { required: true, message: '邮箱不能为空！', trigger: 'blur' },
          { validator: validateEmail, trigger: 'blur' }
        ]
      }
    }
  },
  mounted () {
    // this.identifyCode = ''
    // this.makeCode(this.identifyCodes, 4)
    // disableBrowserBack()
    // history.pushState(null, null, document.URL)
    // if (window.history && window.history.pushState) {
    //   // eslint-disable-next-line no-undef
    //   $(window).on('popstate', function () {
    //     window.history.pushState('forward', null, '')
    //     window.history.forward(1)
    //   })
    //   window.history.pushState('forward', null, '') // 在IE中必须得有这两行
    //   window.history.forward(1)
    // }
  },
  methods: {
    openRegisterAccount () {
      this.dialogFormVisible = true
    },
    registeAccount () {
      this.$refs.loginForm.validate(value => {
        if (value) {
          this.isLoading = true
          // 初始化请求体
          const formData = new FormData()
          formData.append('username', this.form.username)
          formData.append('password', this.form.password1)
          formData.append('email', this.form.account)
          Axios.post('/regist', formData).then(resp => {
            const respData = resp.data ? resp.data : resp
            console.log('resp', respData)
            if (respData.status === 'success') {
              // 关闭注册页面，清除注册页面数据
              this.dialogFormVisible = false
              this.clearDialog()
              console.log('resp', respData)
              setTimeout(() => {
                console.log('test')
                window.location.href = 'http://10.6.50.239/'
              }, 1000)
              this.$message({ type: 'success', message: '注册成功！', duration: 1500 })
            } else {
              this.$message({
                message: respData.status,
                type: 'error',
                duration: 2500
              })
            }
            this.isLoading = false
          })
        }
      })
    },
    // 验证表单各元素，只有全部有效才能发送验证码（弃置）
    // getCode () {
    //   const fieldsToValidate = ['account', 'password1', 'password2', 'captcha']
    //   Promise.all(fieldsToValidate.map(item => {
    //     return new Promise((resolve, reject) => {
    //       this.$refs.loginForm.validateField(item, result => {
    //         resolve(result)
    //       })
    //     })
    //   })).then(result => {
    //     // result是一个数组，返回各个字段的验证信息，如果为空字符串则认为验证通过
    //     const msg = result.find(res => {
    //       return res !== ''
    //     })
    //     if (!msg) {
    //       this.$message({ message: '验证码已发送', type: 'success', duration: 1500 })
    //       this.tackBtn() // 验证码倒数60秒
    //     }
    //   })
    // },

    // tackBtn () { // 验证码倒数60秒
    //   let time = 59
    //   const timer = setInterval(() => {
    //     if (time === 0) {
    //       clearInterval(timer)
    //       this.valiBtn = '点击获取'
    //       this.disabled = false
    //     } else {
    //       this.disabled = true
    //       this.valiBtn = time + '秒后重试'
    //       time--
    //     }
    //   }, 1000)
    //   // this.refreshCode()
    // },
    // 取消注册清空数据
    clearDialog () {
      this.dialogFormVisible = false
      this.form.password1 = ''
      this.form.password2 = ''
      this.form.account = ''
      this.form.username = ''
      this.disabled = false
    }
  }
}
</script>

<style lang="scss" scoped>
$bg:#283443;
$blue: skyblue;
::v-deep .el-form-item__label {
    padding: 3px 12px 0 0;
    font-size:16px;
}
.regist {
  margin-top: 60px;
  display: flex;
  justify-content: center;
}
.login-tools {
  width: 550px;
    ::v-deep .el-dialog {
        &__header {
            background: $bg;

            .el-dialog__title {
                color: $blue;
                font-size: 20px;
            }
        }
        &__body {
            background: $bg;

            .el-form-item__label {
                color: $blue;
            }
        }
        &__footer {
            background: $bg;
        }
    }
}
.dialog-footer {
    display: flex;
    justify-content: space-between;
    padding: 0 150px 0;

    button {
      margin-left: 60px;
    }
}
.identifybox {
    padding-top: 15px;
}
</style>
