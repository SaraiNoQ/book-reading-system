<template>
  <div>
    <div class="user-info">
      <div class="head detail-info">
        <h1 class="user-setting">个人设置</h1>
        <div class="avatar-setting">头像设置/账号信息设置</div>
      </div>
      <div class="head detail-setting">
        <!-- <h1 class="text-base text-left font-bold mb-2">头像设置</h1> -->
        <div>
          <el-upload
            class="avatar-uploader"
            :show-file-list="false"
            action=""
            :auto-upload="false"
            accept=".jpg, .jpeg, .png"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
            :on-change="onChange"
            :on-exceed="handleExceed"
            :limit="1"
            ref="upload_img">
              <!-- <img v-if="imageUrl" :src="imageUrl" class="avatar"> -->
              <el-avatar
                :size="102"
                class="cursor-pointer"
                v-if="imageUrl"
                :src="imageUrl"></el-avatar>
              <el-avatar
                :size="102"
                class="cursor-pointer"
                v-else> user </el-avatar>
          </el-upload>
        </div>

        <!-- 昵称+简介设置 -->
        <div class="nick-info">
          <el-form
            label-width="100px"
            :model="formLabelAlign"
          >
            <el-form-item label="账号">
              <div style="width: 100%;">
                <el-input v-model="formLabelAlign.username" disabled/>
                <div class="form-setting text-gray-400">
                  <span class="tip">暂不支持修改</span>
                </div>
              </div>
            </el-form-item>

            <el-form-item label="昵称">
              <div style="width: 100%;">
                <el-input v-model="formLabelAlign.name" @input="focusName"/>
                <div class="form-setting">
                  <span class="tip">2～30个字符，支持中英文、数字</span>
                  <div class="form" v-show="visible.name">
                    <button
                      class="btn-gray"
                      @click="cancelName">取消</button>
                    <button
                      class="btn-orange"
                      @click.prevent="saveName">保存</button>
                  </div>
                </div>
              </div>
            </el-form-item>
          </el-form>
        </div>
      </div>

      <!-- 密码设置 -->
      <div class="head pass-setting">

        <!-- 初始密码设置 -->
        <div class="fisrt-psd">
          <div style="width: 92%;">
            <h3 class="psd">密码</h3>
            <!-- <div class="not-set">{{ passChanged }}</div> -->
            <div class="not-set">修改密码</div>
          </div>
          <div class="ml4">
            <button class="btn-blue"
              @click="changePasswordVisible">{{ passwordVisible ? '收起' : '编辑' }}</button>
          </div>
        </div>

        <!-- 展开：重置密码 -->
        <div v-if="passwordVisible">
          <!-- 密码表单 -->
          <el-form
            ref="changePswRef"
            label-width="0"
            :model="formPassword"
            size="large"
            style="marginTop: 24px;"
            @keyup.enter.native="changePassword"
            :rules="rules"
          >
            <el-form-item class="reset-form" prop="firstPsw">
              <div class="reset-form-input">
                <el-input
                  v-model="formPassword.firstPsw"
                  placeholder="输入新密码"
                  show-password
                />
              </div>
              <div class="reset-form-tip" :class="{ red: tipColor }">
                8 位以上数字和字母
              </div>
            </el-form-item>
            <el-form-item prop="secondPsw">
              <div class="reset-form-input">
                <el-input
                  v-model="formPassword.secondPsw"
                  placeholder="验证旧密码"
                  show-password
                />
              </div>
            </el-form-item>
          </el-form>

            <!-- 提交按钮 -->
          <div class="submit-btn">
            <button
              type="button"
              data-mdb-ripple="true"
              data-mdb-ripple-color="light"
              class="btn"
              @click="changePassword"
            >提交</button>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import Axios from '../../utils/api'
import httpAPI from '../../utils/port'

export default {
  created () {
    this.imageUrlPrefix = httpAPI()
    this.imageUrl = this.$store.state.userInfo.icon ? (this.imageUrlPrefix + this.$store.state.userInfo.icon) : ''
  },
  data () {
    const validateNewPass = (rule, value, callback) => {
      if (value.length < 8) {
        // callback(new Error('密码长度不能小于 8 个字符！'))
        // 使右侧提示字符变红
        this.tipColor = true
      } else {
        this.tipColor = false
        callback()
      }
    }
    return {
      imageUrl: '',
      upload_img: '',
      formLabelAlign: {
        name: '',
        username: ''
      },
      visible: { name: '' },
      passwordVisible: false,
      formPassword: {
        firstPsw: '',
        secondPsw: ''
      },
      uploadErrorType: '',
      imageUrlPrefix: '',
      passChanged: '未修改',
      // 验证规则
      rules: {
        secondPsw: [
          { required: true, message: '旧密码不能为空！', trigger: 'blur' }
          // { min: 8, message: '长度在 8 到 5 个字符', trigger: 'blur' }
        ],
        firstPsw: [
          { required: true, message: '新密码不能为空！', trigger: 'blur' },
          { validator: validateNewPass, trigger: 'blur' }
          // { min: 8, message: '密码长度不能小于 8 个字符！', trigger: 'blur' }
        ]
      },
      // 提示字符颜色
      tipColor: false
    }
  },
  async beforeMount () {
    const res = await this.getUserInfo()
    console.log('userInfoImage', res.icon)
    // if (res) {
    //   this.formLabelAlign.name = res.nickname
    //   this.formLabelAlign.username = res.username
    // }
    const userInfo = this.$store.state.userInfo
    if (userInfo) {
      this.formLabelAlign.name = userInfo.nickname
      this.formLabelAlign.username = userInfo.username
    }
  },
  methods: {
    test () {
      console.log('test')
    },
    handleAvatarSuccess (res, file) {
      this.imageUrl = 'cdn测试域名' + res.key
      console.log('imageUrl', this.imageUrl)
    },
    beforeAvatarUpload (file) {
      console.log('type', file.type.slice(0, 5))
      const isJPG = file.type.slice(0, 5) === 'image'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG) {
        this.uploadErrorType = '上传头像图片只能是 JPG / JPEG / PNG / GIF 格式!'
        console.log('上传头像图片只能是 JPG 格式!')
      }
      if (!isLt2M) {
        this.uploadErrorType = '上传头像图片大小不能超过 2MB!'
        console.log('上传头像图片大小不能超过 2MB!')
      }
      return isJPG && isLt2M
    },
    async onChange (file, fileList) {
      if (!this.beforeAvatarUpload(file.raw)) {
        this.$message({
          message: this.uploadErrorType ? this.uploadErrorType : '上传头像的格式错误!',
          type: 'error'
        })
        return
      }
      const res = await this.getBase64(file.raw)
      // base64编码转成二进制
      // var arr = res.split(','); var mime = arr[0].match(/:(.*?);/)[1]
      // var bstr = atob(arr[1]); var n = bstr.length; var u8arr = new Uint8Array(n)
      // while (n--) {
      //   u8arr[n] = bstr.charCodeAt(n)
      // }
      // var obj = new Blob([u8arr], { type: mime })
      // var fd = new FormData()
      // fd.append('user_image', obj, 'image.png')

      console.log('uploading', [res])

      const fd = new FormData()
      const arr = []
      arr.push(res)
      fd.append('file', arr)

      const resp = await Axios.post('/user/updateIcon', fd)
      // 初始化imageUrl
      this.imageUrl = ''
      try {
        console.log('updateIcon', resp)
        if (resp.data) {
          this.$message({
            message: '头像修改成功!',
            type: 'success'
          })

          // 修改localStorage中的user_image
          const resp1 = await Axios.post('/user/getUserInfo')
          const userInfo = resp1.data ? resp1.data : resp1
          this.$store.commit('setUserInfo', userInfo)
          localStorage.setItem('avatar', userInfo.icon)
          // 图片url
          const filePath = `${this.imageUrlPrefix}${resp1.data.icon}`
          this.imageUrl = filePath
        }
        console.log('resp', resp)
      } catch (error) {
        console.log('error')
      }
    },
    // 只允许有一张照片
    handleExceed (files) {
      this.$refs.upload_img.clearFiles()
      const file = files[0]
      this.$refs.upload_img.handleStart(file)
    },
    focusName () {
      this.visible.name = true
    },
    cancelName () {
      this.visible.name = false
    },
    async saveName () {
      if (!this.formLabelAlign.name) {
        this.$message({
          message: '请输入正确的昵称!',
          type: 'error'
        })
        return
      }
      try {
        if (this.formLabelAlign.name.length < 2 || this.formLabelAlign.name.length > 30) {
          this.$message({
            message: '昵称长度为2-30个字符!',
            type: 'error'
          })
          return
        }
      } catch (error) {
        console.error(error)
      }
      const formData = new FormData()
      const nickName = this.formLabelAlign.name
      formData.append('nickName', nickName)

      const res = await Axios.post('/user/updateNickname', formData)
      const respData = res.data ? res.data : res
      if (respData === 'success') {
        this.$message({
          message: '昵称修改成功!',
          type: 'success'
        })
        // 记录昵称的改变
        const userInfo = this.$store.state.userInfo
        userInfo.nickname = nickName
        const b = userInfo
        this.$store.commit('setUserInfo', b)
        localStorage.setItem('nickname', nickName)
        this.visible.name = false
      } else {
        this.$message({
          message: '昵称修改失败!',
          type: 'error'
        })
      }
    },
    // 修改完成之后要修改localStorage中的数据
    // afterChangeInfo (data) {
    //     const userInfo = JSON.parse(localStorage.getItem('user'))
    //     if (data.nick_name) {
    //         userInfo.nick_name = data.nick_name
    //     }
    //     if (data.user_info) {
    //         userInfo.user_info = data.user_info
    //     }
    //     localStorage.setItem('user', JSON.stringify(userInfo))
    // },
    changePasswordVisible () {
      this.passwordVisible = !this.passwordVisible
    },
    getUserName () {
      return JSON.parse(localStorage.getItem('user')).user_name
    },
    async submitChangePsw () {
      if (this.formPassword.firstPsw && this.formPassword.firstPsw === this.formPassword.secondPsw && this.formPassword.firstPsw.length >= 8) {
        const formData = new FormData()
        const userName = this.getUserName()
        const token = ''
        formData.append('token', token)
        formData.append('user_name', userName)
        formData.append('new_password', this.formPassword.firstPsw)
        const res = await Axios.patch('/passwd', formData)
        console.log('psw', res)
        // @ts-ignore
        if (res.success) {
          this.$message({
            message: '密码修改成功！请重新登录...',
            type: 'success'
          })
        //   this.$store.commit('removeToken')
        //   localStorage.removeItem('user')
        //   this.$router.replace('/')
        }
      }
    },
    // base编码
    getBase64 (file) {
      return new Promise(function (resolve, reject) {
        const reader = new FileReader()
        let imgResult = ''
        reader.readAsDataURL(file)
        reader.onload = function () {
          imgResult = reader.result
        }
        reader.onerror = function (error) {
          reject(error)
        }
        reader.onloadend = function () {
          resolve(imgResult)
        }
      })
    },
    async getUserInfo () {
      const res = await Axios.post('/user/getUserInfo')
      return res.data ? res.data : res
    },
    async changePassword () {
      this.$refs.changePswRef.validate(async (valid) => {
        if (!valid) {
          this.$message({
            message: '请确认表单信息正确！',
            type: 'error'
          })
          return false
        }
        const fd = new FormData()
        fd.append('oldPass', this.formPassword.secondPsw)
        fd.append('newPass', this.formPassword.firstPsw)
        const res = await Axios.post('/user/updatePassword', fd)
        const resData = res.data ? res.data : res
        if (resData === 'success') {
          this.$message({
            message: '密码修改成功！请重新登录...',
            type: 'success'
          })
          setTimeout(() => {
            this.$axios.post('/user/logout').then(resp => {
              console.log('logout', resp)
              const respData = resp.data ? resp.data : resp
              if (respData) {
                try {
                  // 移除存储的用户名和头像
                  localStorage.removeItem('nickname')
                  localStorage.removeItem('avatar')
                  localStorage.removeItem('token')
                  sessionStorage.removeItem('token')
                } catch (error) {}
                this.$router.go(0)
              }
            })
          }, 1000)
        } else if (resData === '旧密码错误') {
          this.$message({
            message: '旧密码错误！请重新输入',
            type: 'error',
            duration: 1300
          })
          this.formPassword.secondPsw = ''
          // this.formPassword.firstPsw = ''
        } else {
          this.$message({
            message: '密码修改失败!',
            type: 'error',
            duration: 1300
          })
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
$my-red: #ed4259;
.head {
    font-family: -apple-system,BlinkMacSystemFont,Helvetica Neue,PingFang SC,Microsoft YaHei,Source Han Sans SC,Noto Sans CJK SC,WenQuanYi Micro Hei,sans-serif;
    // font-size: 15px;
    color: #121212;
    font-size: 19px;
}

.noInput {
    color: #fff !important;
    background-color: rgb(161, 196, 250) !important;
}

.red {
  color: $my-red !important;
}

::v-deep .el-form-item__label {
    padding: 0 25px 0 0;
}

::v-deep .el-form-item__content {
    line-height: 16px;
    display: flex;
    align-items: center;
    margin-bottom: 5px;
}

::v-deep .el-input__inner {
    background-color: rgb(240, 241, 244);
    border-radius: 8px;
}

::v-deep .el-form-item {
    margin-bottom: 15px;
}

::v-deep .user-info {
    // mx-auto mt-6 bg-white rounded-lg w-[90%] md:w-3/5 lg:w-1/2 box-border
    margin: 24px auto 0;
    background-color: #fff;
    border-radius: 8px;
    width: 90%;
    @media (min-width: 768px) {
        width: 60%;
    }
    @media (min-width: 1024px) {
        width: 50%;
    }
    box-sizing: border-box;
}

// 表单验证错误信息
::v-deep .el-form-item__error {
  padding-top: 1.5px;
}
.detail-info {
    // py-5 px-6 items-center border-y-[0.01rem]
    padding: 20px 24px;
    align-items: center;
    border-bottom: 0.16px solid rgb(229, 231, 235);
}
.user-setting {
    font-size: 20px;
    line-height: 28px;
    text-align: left;
    font-weight: 700;
}
.avatar-setting {
    margin-top: 4px;
    font-size: 14px;
    line-height: 20px;
    text-align: left;
    --tw-text-opacity: 1;
    color: rgb(107 114 128 / var(--tw-text-opacity));
}

.detail-setting {
    padding: 20px 24px;
    align-items: center;
    border-top: 0.16px solid rgb(229, 231, 235);
    border-bottom: 0.16px solid rgb(229, 231, 235);

}

.avatar-uploader {
    max-height: 130px;
}

.nick-info {
    margin-top: 24px;
    margin-right: 24px;
    margin-left: -30px;
    @media (min-width: 768px) {
        margin-left: 0px;
    }
}

.form-setting {
    position: relative;
    margin-top: 8px;
    text-align: left;
}

.tip {
    font-size: 12px;
    line-height: 16px;
    vertical-align: top;
    --tw-text-opacity: 1;
    color: rgb(156 163 175 / var(--tw-text-opacity));
}

.form {
    position: absolute;
    right: 0px;
    top: 0px;
}

.btn-gray {
    border-width: 1px;
    border-style:none;
    cursor: pointer;
    border: rgb(54, 57, 63) solid 1px;
    --tw-border-opacity: 1;
    // border-color: rgb(54 57 63 / var(--tw-border-opacity));
    border-radius: 8px;
    height: 20px;
    padding: 0px 8px;
    font-size: 12px;
    line-height: 16px;
    min-width: min-content;
    margin-left: 8px;
    --tw-text-opacity: 1;
    color: rgb(54 57 63 / var(--tw-text-opacity));
}

.btn-orange {
    border-width: 1px;
    border-style:none;
    cursor: pointer;
    --tw-border-opacity: 1;
    border: rgb(251, 146, 60) solid 1px;
    border-radius: 8px;
    height: 20px;
    padding: 0px 8px;
    font-size: 12px;
    line-height: 16px;
    min-width: min-content;
    margin-left: 8px;
    --tw-text-opacity: 1;
    color: rgb(251 146 60 / var(--tw-text-opacity));
}

.pass-setting {
    padding: 20px 24px;
    align-items: center;
    border-top: 0.16px solid rgb(229, 231, 235);
}

.fisrt-psd {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100px;
    margin-top: -20px;
}

.psd {
    text-align: left;
    font-size: 16px;
    line-height: 24px;
}

.not-set {
    margin-top: 4px;
    text-align: left;
    --tw-text-opacity: 1;
    color: rgb(107 114 128 / var(--tw-text-opacity));
    font-size: 14px;
    line-height: 20px;
}

.ml4 {
    margin-left: 4px;
}
.btn-blue {
    height: auto;
    --tw-text-opacity: 1;
    color: rgb(37 99 235 / var(--tw-text-opacity));
    background-color: transparent;
    font-size: 14px;
    line-height: 20px;
    cursor: pointer;
    text-align: center;
    width: 40px;
    border-style:none;
}

.reset-form-input {
    width: 65%;
    @media (min-width: 1024px) {
        width: 60%;
    }
}

.reset-form-tip {
    margin-left: 12px;
    --tw-text-opacity: 1;
    color: rgb(156 163 175 / var(--tw-text-opacity));
}

.submit-btn {
    position: relative;
    height: 40px;
    width: 100%;
}

.btn {
    position: absolute;
    display: inline-block;
    padding: 10px 24px;
    --tw-bg-opacity: 1;
    background-color: rgb(37 99 235 / var(--tw-bg-opacity));
    --tw-text-opacity: 1;
    color: rgb(255 255 255 / var(--tw-text-opacity));
    font-weight: 500;
    font-size: 14px;
    line-height: 20px;
    line-height: 1.25;
    text-transform: uppercase;
    border-radius: 4px;
    --tw-shadow: 0 4px 6px -1px rgb(0 0 0 / 0.1), 0 2px 4px -2px rgb(0 0 0 / 0.1);
    --tw-shadow-colored: 0 4px 6px -1px var(--tw-shadow-color), 0 2px 4px -2px var(--tw-shadow-color);
    box-shadow: var(--tw-ring-offset-shadow, 0 0 #0000), var(--tw-ring-shadow, 0 0 #0000), var(--tw-shadow);
    transition: all 0.2s ease-in-out;
    border-color: transparent;
    cursor: pointer;
    &:hover {
        box-shadow: var(--tw-ring-offset-shadow, 0 0 #0000), var(--tw-ring-shadow, 0 0 #0000), var(--tw-shadow-colored);
    }
    left: 0px;
    width: 65%;
    @media (min-width: 1024px) {
        width: 60%;
    }
}
</style>
