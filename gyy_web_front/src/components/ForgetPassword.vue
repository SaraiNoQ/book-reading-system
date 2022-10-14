<template>
    <div class="forget-pwd">
        <el-button type="text" @click="dialogFormVisible = true" style="padding:0px">忘记密码</el-button>

        <el-dialog title="找回密码" :visible.sync="dialogFormVisible">
          <el-form :model="form1" :rules="rule" ref="forgetPswForm">
              <el-form-item label="账号" :label-width="formLabelWidth" prop="account">
                  <el-input autocomplete="off" v-model="form1.account" placeholder="邮箱"></el-input>
              </el-form-item>
              <el-form-item label="新密码" :label-width="formLabelWidth" prop="password1">
                  <el-input autocomplete="off" v-model="form1.password1" placeholder="8-16位字符组成，区分大小写"></el-input>
              </el-form-item>
              <el-form-item label="确认密码" :label-width="formLabelWidth" prop="password2">
                  <el-input autocomplete="off" v-model="form1.password2" placeholder="再次输入新密码，两次密码需相同"></el-input>
              </el-form-item>
              <el-form-item label="验证码" :label-width="formLabelWidth" prop="captcha">
                  <el-input autocomplete="off" v-model="form1.captcha" placeholder="前往邮箱获取验证码"></el-input>
              </el-form-item>
          </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取 消</el-button>
            <el-button type="primary">确 定</el-button>
        </div>
        </el-dialog>
    </div>
</template>

<script>
export default {
  name: 'ForgetPsw',
  data () {
    // 验证第二个密码
    const validSecondPwd = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value.length < 8 || value.length > 16) {
        callback(new Error('长度在 8 到 16 个字符!'))
      } else if (this.form.password1 !== value) {
        callback(new Error('两次密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      dialogTableVisible: false,
      dialogFormVisible: false,
      form1: {
        password1: '',
        password2: '',
        account: '',
        captcha: ''
      },
      formLabelWidth: '120px',
      rule: {
        account: [{ required: true, message: '邮箱号不能为空!', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱!', trigger: 'blur' }
        ],
        password1: [{ required: true, message: '新密码不能为空!', trigger: 'blur' },
          { max: 16, min: 8, message: '密码格式错误!', trigger: 'blur' }
        ],
        password2: [
          {
            validator: validSecondPwd,
            trigger: 'blur'
          },
          { required: true }
        ]
      }
    }
  },
  methods: {
    login () {
      this.dialogFormVisible = false
      this.$router.push('/home')
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

.forget-pwd {
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
    padding: 0 75px 0;
}
.identifybox {
    padding-top: 15px;
}
</style>
