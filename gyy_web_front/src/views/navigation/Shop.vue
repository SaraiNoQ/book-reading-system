<template>
  <div class="book-shop" v-loading="loading">
    <div class="book-shop-content">
      <div
       class="block"
       v-for="(item, index) in this.info.data"
       :key="index"
       @click="openBook(index)"
      >
        <el-image
         :src="imageUrlPrefix + item.imgurl"
         class="book-css"
        >
        <div slot="error" class="image-slot">
          <i class="el-icon-picture-outline"></i>
        </div>
        <!-- <div slot="placeholder" class="image-slot">
          加载中<span class="dot">...</span>
        </div> -->
        </el-image>
        <div class="book-intro">
          <h3>{{ item.bookname }}</h3>
          <div class="book-intro-detail">
            <!-- <div v-for="(item, index) in this.info.data[0]"
            :key="index"
            v-show="index !== 'imgurl' && index !== 'bookname'"
            class="book-info"
            >
            {{ index }}：{{ item }}
            </div> -->
          <div class="book-info">作者：{{item.author}}</div>
          <div class="book-info">朝代：{{item.dynasty}}</div>
          <div class="book-info">版本：{{item.version}}</div>
          <div class="book-info">详情：{{item.detailinfo}}</div>
          </div>
        </div>
      </div>
    </div>
      <!-- <el-result :title="this.info.data[0].bookname"
       :subTitle="this.info.data[0].author"
       >
        <template slot="icon">
          <el-image :src="this.info.data[0].imgurl"></el-image>
        </template>
      </el-result> -->
    <el-pagination
      background
      layout="prev, pager, next"
      :page-size="pageSize"
      :total="total"
      :current-page.sync="currentPage"
      @current-change="page"
      >
    </el-pagination>
    <div class="bottom-info">
      <pre class="bottom-pre">北京中医药大学
  国学院中医训诂与考据研究所
      </pre>
      <!-- <p>北京中医药大学</p>
      <p>国学院中医训诂与考据研究所&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; All Rights Reserved 版权所有  </p> -->
    </div>
  </div>
</template>

<script>
import httpAPI from '../../utils/port'
// import { getRequest } from '../../utils/api.js'
export default {
  name: 'Shop',
  async created () {
    this.imageUrlPrefix = httpAPI()
    const tokenState = localStorage.getItem('token') || ''
    if (tokenState !== '') {
      try {
        // 发一个请求，如果401错误就会清空token
        await this.$axios.post('/bookChapter/getChapter?bookName=新雕孙真人千金方')
      } catch (error) {
        // this.$message.error('网络异常！')
      }
    }
  },
  mounted () {
    /**
     * {page} 当前的页数
     * {size} 每页有多少条数据
     * 'book/getBookInfo/1/' + '1'
     */
    this.$axios.get('/book/getBookInfo/1/' + this.pageSize).then(resp => {
      if (resp == null) {
        this.$message({
          type: 'error',
          message: '连接网络失败'
        })
      }
      this.info = resp instanceof Array ? resp : resp.data
      this.total = this.info.total
      this.loading = false
      // console.log('booksInfo', this.info, resp)
      this.$store.commit('setShopBookInfo', this.info.data)
      // 将书籍名称存放进session中，方便异文调用 this.info.data Array
      const diffbook = []
      this.info.data.forEach(element => {
        diffbook.push(element.bookname)
      })
      this.$store.commit('setdiffbook', diffbook)
      window.sessionStorage.setItem('store', JSON.stringify(this.$store.state))
    })
  },
  data () {
    return {
      info: {},
      pageSize: 6,
      total: 50,
      currentPage: 1,
      loading: true,
      imageUrlPrefix: ''
    }
  },
  methods: {
    // getBookInfo () {
    //   getRequest('book/getBookInfo/1/1').then(resp => {
    //     console.log(resp)
    //   })
    // },
    page (currentPage) {
      this.loading = true
      this.$axios.get('/book/getBookInfo/' + currentPage + '/' + this.pageSize).then(resp => {
        if (resp == null) {
          this.$message({
            type: 'error',
            message: '连接网络失败'
          })
        }
        this.info = resp instanceof Array ? resp : resp.data
        console.log('booksInfoPho', this.info)
        this.total = this.info.total
        this.loading = false
      })
    },
    openBook (i) {
      if (!localStorage.getItem('token') && !sessionStorage.getItem('token')) {
        this.$message({
          type: 'error',
          message: '您还未登陆，请登录！',
          duration: 1200
        })
        return
      }
      this.$router.push('/bookInfo')
      this.$store.commit('setCurrentPage', this.currentPage)
      this.$store.commit('setCurrentBook', i)
      // console.log(this.info.data[i].bookname) // 点击时书籍信息存放到vuex中
      this.$store.commit('setCurrentBookName', this.info.data[i].bookname)
      window.sessionStorage.setItem('store', JSON.stringify(this.$store.state))
    }
  }
}
</script>

<style lang="scss">
$bg-color: rgba(233, 194, 87, 0.685);
main.el-main {
  background: #E8E4E3;
}
.el-main {
  padding-bottom: 0px;
  padding-top: 15px;
}

.bottom-info {
  margin-top: 20px;
  color: #7F7F7F;
}

.book-shop {
  display: flex;
  flex-direction: column;
  align-content: center;
  align-items: center;
  justify-content: center;
}

.book-shop-content {
  box-shadow: 0px 0px 6;
  border-radius: 12px;
  background-color: #fff;
  padding: 20px 0 10px 25px;
  display: grid;
  grid-template-columns: repeat(3, 400px);
  margin-bottom: 5px;
  box-shadow: 0 1px 6px rgb(0 0 0 / 35%), 0 0 5px #f9f2e9 inset;
}

.block {
  cursor: pointer;
  line-height: 20px;
  display: flex;
  justify-content: flex-start;
  min-height: 220px;
  height: 250px;
  width: 400px;
  max-width: 400px;
  margin: 6px 5px 12px 0;

  .book-css {
    left: 0px;
    width: 200px;
  }

  .book-intro {
    top: 0px;
    margin-left: 8px;

    h3 {
      height: 40px;
      width: 180px;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
      text-align: left;
    }

    .book-intro-detail {
      height: 200px;
      width: 180px;
      flex: grid;

      .book-info {
      margin: 20px 0 20px 0;
      text-align: left;
      font-size: 16px;
      }
    }
  }
}

.el-pagination {
  height: 20px;
}

.bottom-pre {
  font-size: 14px;
  line-height: 20px;
  font-weight: 600;
  font-family: "Baskerville",
          "Georgia",
          "Liberation Serif",
          "Kaiti SC",
          "STKaiti",
          "AR PL UKai CN",
          "AR PL UKai HK",
          "AR PL UKai TW",
          "AR PL UKai TW MBE",
          "AR PL KaitiM GB",
          "KaiTi",
          "KaiTi_GB2312",
          "DFKai-SB",
          "TW-Kai",
          "serif",
}
</style>
