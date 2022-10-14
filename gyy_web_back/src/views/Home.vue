<template>
  <div>
    <el-container>
      <el-header class="heading" style="height: 100px">
        <div class="tittle">
          <img src="../assets/bzylogo.png" height="84" width="84" />
          <h1 style="color: white">古籍药典阅读对照管理系统</h1>
        </div>

        <el-dropdown>
            <span class="el-dropdown-link">
              <i><el-avatar src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></el-avatar></i>
            </span>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item @click.native="dialogTWOVisible= true">切换账户</el-dropdown-item>
                    <el-dropdown-item @click.native="dialogVisible= true">登出</el-dropdown-item>

          </el-dropdown-menu>
        </el-dropdown>
      </el-header>
        <el-container>
        <el-aside width="180px" style="overflow-x: hidden;">
          <aside-menu></aside-menu>
        </el-aside>
        <el-main>
          <keep-alive>
            <router-view />
          </keep-alive>
        </el-main>
      </el-container>
    </el-container>


    <el-dialog
        title="提示"
        :visible.sync="dialogVisible"
        width="30%"
        >
      <span>确认登出？</span>
      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="dialogVisible = false;userLogout()">确 定</el-button>
  </span>
    </el-dialog>

    <el-dialog
        title="提示"
        :visible.sync="dialogTWOVisible"
        width="30%"
        >
      <span>确认更换账号？</span>
      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogTWOVisible = false">取 消</el-button>
    <el-button type="primary" @click="dialogTWOVisible = false;userLogout()">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
import AsideMenu from "../components/AsideMenu";
export default {
  name: "Home",
  components:{AsideMenu},
  data(){
    return{
      dialogVisible:false,
      dialogTWOVisible:false,
    }
  },
  mounted () {
    // getRequest('/chapter/getChapter').then(resp=>{
    //   const demoData = []
    //   if(resp)
    //   {
    //     const cha=localStorage.getItem('contentdata');

    //     let bookData=JSON.parse(JSON.stringify(resp))
    //     for(var key in bookData)
    //     {
    //       let op={
    //         chapter:'',
    //         cataLog:[]
    //       }
    //       op.chapter=key;
    //       op.cataLog=resp[key];
    //       //console.log(op);
    //       demoData.push(op);
    //     }
    //     this.$store.commit('setCatalog', demoData);
    //     console.log('store update!');
    //   }
    // })
    // getRequest('/chapter/getChapter').then(resp=>{
    //     const demoData = []
    //     if (resp) {
    //       // const cha=localStorage.getItem('contentdata');

    //       // const cha=localStorage.getItem('contentdata');
    //       const bookData = resp.data ? resp.data : resp
    //       bookData.forEach(e => {
    //         e.forEach(e1 => {
    //           demoData.push(e1)
    //         })
    //       })
          // this.chapterdata = bookData
          // for(var key in bookData)
          // {
          // let op={
          //     chapter:'',
          //     cataLog:[]
          // }
          // op.chapter=key;
          // op.cataLog=resp[key];
          // //console.log(op);
          // demoData.push(op);
          // }
          // demoData.forEach(e => {
          //   // e 为某一本书的全部内容
          //   const chapterData = e.chapter
          //   const idReg = /id\=.*?\,/
          //   const booknameReg = /bookname\=.*?\,/
          //   // 获得到bookId
          //   const obj = {}
          //   obj.bookId = idReg.exec(chapterData)[0].replace(',', '').slice(3)
          //   obj.chapter = booknameReg.exec(chapterData)[0].replace(',', '').slice(9)
          //   obj.cataLog = e.cataLog
          //   chapterdata.push(obj)
          // })
      //     console.log('cahpterdata', demoData);
      //     this.$store.commit('setCatalog', demoData);
      //   }
      // })
  },
  methods:{
    userLogout(){
      // axios({
      //   headers: {
      //     'Content-Type': 'application/x-www-form-urlencoded'
      //   },
      //   method: 'get',
      //   url: '/logout',
      // }).then((resp) => {
      //   console.log(resp)
      //  this.$router.replace('/');
      // })
      this.$axios.post('/user/logout').then(resp=>{
        if (resp !== null) {
          localStorage.removeItem('token');
          sessionStorage.removeItem('token');
          this.$router.replace('/')
        }
      })
    }
  }
};
</script>

<style scoped lang="scss">
.heading {
  background-color: #f4cf98;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 15px;
  box-sizing: border-box;
}
.tittle {
  display: flex;
  align-items: center;
}

h1 {
  display: inline-block;
  padding-left: 10px;
} 

</style>
