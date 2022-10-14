<template>
    <div class="user-logout">
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
              <div>{{ nickName }}</div>
              <!-- <div><img src="../assets/userInfo.jpg"></div> -->
              <el-avatar
                :size="48"
                :src="imageUrlPrefix + userAvatar"
                v-if="userAvatar && userAvatar !== ''"></el-avatar>
              <el-avatar
                :size="48"
                v-else> user </el-avatar>
            </span>
            <el-dropdown-menu slot="dropdown">
              <!-- <el-dropdown-item command="userRegister">
                注册
              </el-dropdown-item> -->
              <el-dropdown-item command="userInfo">个人中心</el-dropdown-item>
              <el-dropdown-item command="userLogout" divided>注销</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
</template>

<script>
import httpAPI from '../utils/port'
export default {
  name: 'UserInfo',
  created () { this.imageUrlPrefix = httpAPI() },
  data () {
    return {
      imageUrlPrefix: ''
    }
  },
  computed: {
    userAvatar () {
      return localStorage.getItem('avatar')
    },
    nickName () {
      return localStorage.getItem('nickname')
    }
  },
  // watch: {
  //   userAvatar (val) {
  //     if (val) {
  //       this.$store.commit('setUserInfo', val)
  //     }
  //   }
  // },
  methods: {
    handleCommand (command) {
      if (command === 'userRegister') {
        this.$router.push('/regist')
      }
      if (command === 'userLogout') {
        this.$axios.post('/user/logout').then(resp => {
          console.log('logout', resp)
          const respData = resp.data ? resp.data : resp
          if (respData) {
            // window.location.href = 'http://10.6.50.239/'
            // this.$router.go(0)
            // this.$store.commit('setLoginState', false)
            // this.$store.commit('setUserName', '')
            // sessionStorage.setItem('store', JSON.stringify(this.$store.state))
            this.$store.commit('setUserInfo', {})
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
        // console.log(window.sessionStorage.getItem('store').charAt(window.sessionStorage.getItem('store').search('loginState') + 12) === 'f')
      }
      if (command === 'userInfo') {
        this.$router.push('/setting')
      }
    }
  }
}
</script>

<style lang="scss">
.user-logout {
      margin-right: 5px;
      .el-dropdown-link {
        display: flex;
        align-items: center;
        justify-content: flex-end;
        color: black;
        font-size: 20px;
        &:hover {
          cursor: pointer;
        }
        img {
          width: 48px;
          height: 48px;
          border-radius: 24px;
        }
        div {
          margin-right: 10px;;
        }
      }
    }
</style>
