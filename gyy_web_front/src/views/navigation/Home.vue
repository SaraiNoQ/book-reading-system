<template>
  <div>
    <!-- <div class="top-tab">
      <div></div>
      <aside-menu></aside-menu>
    </div> -->
    <el-container>
      <el-header style="height: 109px;">
        <div class="logo-heading">
          <img src="../../assets/bzylogo.png" @click="$router.push('/')" style="cursor: pointer">
          <h1>古籍药典阅读对照系统</h1>
        </div>
        <div>
          <search-tab v-show="isShow"></search-tab>
        </div>
        <div>
          <user-info v-if="loginButton && loginButton !== ''"></user-info>
          <index-login v-else key="index-login" style="marginRight: 20px"></index-login>
        </div>
      </el-header>
      <div class="content">
        <el-main>
          <router-view />
        </el-main>
      </div>
    </el-container>
  </div>
</template>

<script>
import AsideMenu from '../../components/AsideMenu'
import UserInfo from '../../components/UserInfo'
import IndexLogin from '../../components/IndexLogin.vue'
import Register from '../../components/Register'
import BreadCrumb from '../../components/BreadCrumb'
import SearchTab from '../../components/SearchTab'
export default {
  name: 'Home',
  // eslint-disable-next-line vue/no-unused-components
  components: { AsideMenu, UserInfo, Register, IndexLogin, BreadCrumb, SearchTab },
  mounted: function () {
    // console.log(this.loginButton)
    // this.loginButton = window.sessionStorage.getItem('store') === null || window.sessionStorage.getItem('store').charAt(window.sessionStorage.getItem('store').search('loginState') + 12) === 'f'
  },
  data () {
    return {
      // loginButton: true
      isShow: true
    }
  },
  methods: {
  },
  computed: {
    loginButton () {
      // return !this.$store.state.loginState
      return localStorage.getItem('token') || sessionStorage.getItem('token')
    }
  },
  watch: {
    $route: {
      handler (val, old) {
        console.log(val, old)
        if (val.fullPath === '/search/similar') {
          console.log('show')
          this.isShow = false
        }
        if (old.fullPath === '/search/similar') {
          this.isShow = true
        }
      },
      deep: true
    }
  }
}
</script>

<style lang="scss">
  .top-tab {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .el-header{
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px;
    background: url('../../assets/header.png') no-repeat;
    background-size: 100% 100%;
    -moz-background-size: 100% 100%;
    color: #333;
    .logo-heading {
        display: flex;
        align-items: center;
        h1 {
            display: inline-block;
            padding-left: 10px;
        }
    }
  }

  .content {
      .el-aside {
          float: left;
      }
      .el-main {
          float: right;
      }
  }
  .el-aside {
    background-color: #D3DCE6;
    color: #333;
    text-align: center;
    line-height: 200px;
  }

  .el-main {
    background-color: #E9EEF3;
    color: #333;
    text-align: center;
    line-height: 160px;
    width: 100%;
    height: calc(100vh - 109px);
    overflow-x: hidden;
  }

  body > .el-container {
    margin-bottom: 40px;
  }

  .el-container:nth-child(5) .el-aside,
  .el-container:nth-child(6) .el-aside {
    line-height: 260px;
  }

  .el-container:nth-child(7) .el-aside {
    line-height: 320px;
  }
</style>
