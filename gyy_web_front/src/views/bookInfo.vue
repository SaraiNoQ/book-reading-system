<template>
    <div v-loading="loading">
      <bread-crumb class="bread-crumb"></bread-crumb>
      <div class="book">
        <div class="block">
          <el-image
            :src="imageUrlPrefix + this.bookInfo.imgurl"
            class="book-css"
          >
            <div slot="error" class="image-slot">
              <i class="el-icon-picture-outline"></i>
            </div>
            <div slot="placeholder" class="image-slot">
              加载中<span class="dot">...</span>
            </div>
          </el-image>
          <div class="book-intro">
            <h3>{{ this.bookInfo.bookname }}</h3>
            <div class="book-intro-detail">
            <div class="book-info">作者：{{this.bookInfo.author}}</div>
            <div class="book-info">朝代：{{this.bookInfo.dynasty}}</div>
            <div class="book-info">版本：{{this.bookInfo.version}}</div>
            <div class="book-info">章节：{{this.detailinfo ? this.detailinfo : '暂无相关信息'}}</div>
            <el-button @click="startRead()">开 始 阅 读</el-button>
          </div>
        </div>
      </div>
    </div>

    <div class="book-introduction content-wrap">
      <div class="nav-wrap fl">
        <ul>
          <li>
            <a class="lang" :class="{ act: isAct1 }" @click="clickCatalog">目录</a>
          </li>
          <li>
            <a class="lang" :class="{ act: isAct2 }" @click="clickIntroduction">作品信息</a>
          </li>
        </ul>
      </div>
    </div>

    <div class="intro-detail" v-show="isAct2">
      <p class="intro-word">
        {{ this.bookInfo.introduction ? this.bookInfo.introduction : '暂无相关作品介绍！' }}
      </p>
    </div>

    <div class="catalog" v-loading="catalogLoading" v-show="isAct1">
      <div class="catalogborder">
        <h2>
          目录<i>·</i>
          <span class="head-span">{{this.detailinfo}}</span>
        </h2>
        <div class="volumn" v-for="(item, index) in catalogArr" :key="index">
          <!-- <h3 class="no-baseline" v-if="item.grade == 0">{{item.chapter}}</h3> -->
          <div>
            <h3 class="" @click="getCataChapter(item)">{{item.chapter}}</h3>
            <div class="info">
              <ul class="cf"
              v-for="(children2, index2) in item.cataLog"
              :key="'2-'+index2"
              @click="getCataChapter(children2)"
              >
                <li><span class="book-name">{{children2.chapter}}</span></li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import BreadCrumb from '../components/BreadCrumb'
import httpAPI from '../utils/port'
export default {
  name: 'bookInfo',
  components: { BreadCrumb },
  created () {
    this.imageUrlPrefix = httpAPI()
  },
  mounted () {
    /**
     * {page} 当前的页数
     * {size} 每页有多少条数据
     */
    this.$axios.get('/book/getBookInfo/' + this.$store.state.currentPage + '/6').then(resp => {
      // console.log(resp.data)
      // 刷新页面以后resp会发生变化，需要判断data
      let bookData = resp.data
      if (!(bookData instanceof Array)) {
        bookData = bookData.data
      }
      if (bookData[this.$store.state.currentBook] == null) {
        this.$message({
          type: 'error',
          message: '连接网络失败！'
        })
      }
      this.bookInfo = bookData[this.$store.state.currentBook]
      this.loading = false
    }, error => {
      this.$message({
        type: 'error',
        message: error
      })
    })

    this.$axios.post('/bookChapter/getChapter?bookName=' + this.$store.state.currentBookName)
      .then((resp) => {
        // 第一次直接返回数组，刷新后返回响应对象
        const cataArr = resp.data || resp
        // if (!(cataArr instanceof Array)) {
        //   cataArr = resp.data
        // }
        if (cataArr.length === 0) {
          this.$message({
            type: 'error',
            message: '网络错误！无法获取章节信息！'
          })
        }
        this.catalogLoading = false
        // cataArr is object
        this.catalogArr = cataArr[0].cataLog

        this.arrIdChapter = []
        let chapterNo = 0
        this.catalogArr.forEach(element => {
          this.arrIdChapter.push({ id: element.id, chapter: element.chapter })
          chapterNo++
          if (element.cataLog.length) {
            element.cataLog.forEach(children => {
              this.arrIdChapter.push({ id: children.id, chapter: children.chapter })
            })
          }
        })
        // console.log('arrIdChapter', this.arrIdChapter, this.catalogArr)
        this.detailinfo = chapterNo + '章' + this.arrIdChapter.length + '节'

        // 将目录信息存入session中，便于阅读时目录显示
        this.$store.commit('setCatalogArr', this.catalogArr)
        window.sessionStorage.setItem('store', JSON.stringify(this.$store.state))
        this.isAct1 = true // 设置为true
      }).catch(() => {
        // this.$message({
        //   type: 'error',
        //   message: '登录失效！请重新登录...'
        // })
        this.isAct1 = true // 设置为true
      })

    const config = this.$store.state.config
    config.readWidth = 800
    this.$store.commit('setConfig', config)
    window.sessionStorage.setItem('store', JSON.stringify(this.$store.state))
    // console.log('chapterId', JSON.parse(sessionStorage.getItem('catalogArr'))[0].id)
    console.log('img', this.bookInfo.imgurl)
  },
  data () {
    return {
      bookInfo: this.$store.state.shopBookInfo[this.$store.state.currentBook],
      detailinfo: '',
      loading: true, // 开发时改为false
      catalogArr: [],
      catalogLoading: true,
      arrIdChapter: [],
      imageUrlPrefix: '',
      // click red
      isAct1: true,
      isAct2: false
    }
  },
  methods: {
    startRead () {
      // this.catalogArr.forEach(element => {
      //   if (element.cataLog) {
      //     // element.cataLog[0].cataLog[0].val // 获取到目录的chapter
      //     this.$store.commit('setCurrentChapter', element.cataLog[0].cataLog[0].val)
      //     window.sessionStorage.setItem('store', JSON.stringify(this.$store.state))
      //     this.$router.push('/chapter')
      //   }
      // })
      // console.log(this.arrIdChapter[0].id)
      this.$store.commit('setChapterId', this.arrIdChapter[0].id)
      this.$store.commit('setCurrentChapter', this.catalogArr[0].chapter)
      window.sessionStorage.setItem('store', JSON.stringify(this.$store.state))
      this.$router.push('/chapter')
    },
    getCataChapter (children2) {
      this.$router.push('/chapter')
      this.$store.commit('setCurrentChapter', children2.chapter)
      this.$store.commit('setChapterId', children2.id)
      this.getCataArr()
      window.sessionStorage.setItem('store', JSON.stringify(this.$store.state))
    },
    getCataArr () {
      // const arrIdChapter = []
      // this.catalogArr.forEach(element => {
      //   arrIdChapter.push({ id: element.id, chapter: element.chapter })
      //   if (element.cataLog.length) {
      //     element.cataLog.forEach(children => {
      //       arrIdChapter.push({ id: children.id, chapter: children.chapter })
      //     })
      //   }
      // })
      this.$store.commit('setChapterArr', this.arrIdChapter)
      this.bookInfo.detailinfo = this.arrIdChapter.length + '节'
    },
    clickIntroduction () {
      this.isAct2 = true
      this.isAct1 = false
    },
    clickCatalog () {
      this.isAct1 = true
      this.isAct2 = false
    }
  }
}
</script>

<style lang="scss" scoped>
$my-red: #ed4259;

.act {
  color: $my-red !important;
  margin-bottom: -1px;
  border-bottom: 2px solid $my-red;
  padding-bottom: 2.5px;
}
$intro-width: 580px;
.book {
  display: flex;
  justify-content: center;
}

.content-wrap {
  line-height: 16px;
  border-bottom: 1px solid #e6e6e6;
}

.book-introduction {
  display: block;
  height: 41px;
  width: 990px;
  margin: 0 auto 10px auto;
  padding-left: calc(100vw - 100%);

  .fl {
    float: left;
  }

  li, ul, ol {
    list-style: none outside none;
    font: 18px/40px FZZCYSK;
    float: left;
    overflow: hidden;
    height: 40px;
    padding: 0 21px;
    text-align: center;
    margin: 0;

    a {
      display: block;
      overflow: hidden;
      max-height: 36px;
      color: #a6a6a6;
      text-decoration: none;
      outline: 0;
    }
  }

  ul {
    padding-left: 0;
  }

  .lang {
    font-family: FZZCYSK;
    font-weight: 400;
    transition: color .3s,background-color .3s;
    &:hover {
      color: $my-red;
      cursor: pointer;
    }
  }
}

.intro-detail {
  display: block;
  width: 990px;
  margin: 25px auto 10px auto;
  padding-left: calc(100vw - 100%);

  .intro-word {
    text-indent: 2em;
    word-wrap: break-word;
    word-break: break-all;
    font: 18px/32px PingFangSC-Regular,'-apple-system',Simsun;
    overflow: hidden;
  }
}

.block {
  line-height: 20px;
  display: flex;
  justify-content: flex-start;
  min-height: 220px;
  height: 300px;
  min-width: 990px;
  margin: 15px 0 15px 0;
  border-radius: 3px;
  box-shadow: 0 1px 6px rgb(0 0 0 / 35%), 0 0 5px #f9f2e9 inset;
  .book-css {
    left: 0px;
    margin: 20px 20px 20px 20px;
    width: 200px;
    box-shadow: 0 1px 6px rgb(0 0 0 / 35%), 0 0 5px #f9f2e9 inset;
  }

  .book-intro {
    top: 0px;
    margin-left: 20px;

    h3 {
      margin-top: 30px;
      height: 38px;
      width: $intro-width;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
      text-align: left;
    }

    .book-intro-detail {
      height: 180px;
      width: $intro-width;
      flex: grid;

      .book-info {
      margin: 15px 0 15px 0;
      text-align: left;
      font-size: 16px;
      }

      .el-button {
        margin-top: 8px;
        width: 120px;
      }
    }
  }
}

$head-baseline: #e5e5e5;
$head-color1: rgb(140, 140, 140);
$baseline: #666;
.catalog {
  display: flex;
  justify-content: center;
  min-height: 300px;

  .catalogborder {
    width: 990px;
    h2 {
      // margin-right: 8px;
      font-family: FZZCYSK;
      color: $my-red;
      border-bottom: 1px solid $baseline;
      padding-bottom: 8px;

      .head-span {
        margin-left: 5px;
        // color: $head-color1;
        font-size: 22px;
      }
    }

    h3 {
      color:#666;
      border-bottom: 1px solid $head-baseline;
      padding-bottom: 11px;
      margin: 15px 0px 0px 0px;
      display:block;
      white-space:nowrap;
      overflow:hidden;
      text-overflow:ellipsis; // 省略号显示
      &:hover {
        color:$my-red;
        cursor: pointer;
      }
    }

    .volumn {
      li, ul {
        list-style: none outside none;
        margin: 0;
        padding: 0;
        border: 0;
      }

      .info{
        display: grid;
        grid-template-columns: repeat(3, 330px);

        .cf {
          color: $head-color1;
          border-bottom: 1px solid $head-baseline;
          width: 330px;
          height: 40px;
          line-height: 40px;

          .book-name {
            width: 250px;
            display:block;
            white-space:nowrap;
            overflow:hidden;
            text-overflow:ellipsis; // 省略号显示
            font-size: 15px;
            &:hover {
              color:$my-red;
              cursor: pointer;
            }
          }
        }
      }

      .no-baseline {
        color: rgb(128, 128, 128);
        border-bottom: 0px solid white;
        margin-top: 25px;
      }
    }
  }
}
</style>
