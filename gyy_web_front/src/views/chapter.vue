<template>
  <div
    class="chapter-wrapper"
    :style="bodyTheme"
    :class="{ night: isNight, day: !isNight }"
  >

    <!-- 搜索框 -->
    <div
      class="search-item"
      v-if="showSearchInput"
    >
      <Search />
    </div>

    <!-- 选择标记书籍废弃 -->

    <!-- 查看原文悬浮框 -->
    <el-dialog
      :title="originalHead"
      :visible.sync="dialogVisible"
      width="45%">
      <div class="original">
        <p
          v-for="(item, index) in originalContent"
          :key="index"
          style="marginTop: 0px"
        >{{item}}</p>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
      </span>
    </el-dialog>

    <div class="tool-bar" :style="leftBarTheme">
      <div class="tools">
        <el-popover
          placement="right"
          :width="cataWidth"
          height="1000"
          trigger="click"
          :visible-arrow="false"
          v-model="popCataVisible"
          popper-class="pop-cata"
        >
          <PopCata @getChapContent="getChapContent" ref="popCata" class="popup" />
          <div class="tool-icon" slot="reference">
            <div class="iconfont">
              &#58905;
            </div>
            <div class="icon-text">目录</div>
          </div>
        </el-popover>

        <el-popover
          placement="bottom-right"
          width="470"
          trigger="click"
          :visible-arrow="false"
          v-model="readSettingsVisible"
          popper-class="pop-setting"
        >
          <ReadSettings class="popup" />
          <div class="tool-icon" slot="reference" :class="{ isDeny: toolLoading }">
            <div class="iconfont">
              &#58971;
            </div>
            <div class="icon-text">设置</div>
          </div>
        </el-popover>

        <div class="tool-icon" @click="toShelf">
          <div class="iconfont">
            &#58892;
          </div>
          <div class="icon-text">书架</div>
        </div>

        <div class="tool-icon" @click="toYuanWen" :class="{ isDeny: toolLoading }">
          <div class="iconfont">
            &#58902;
          </div>
          <div class="icon-text">原文</div>
        </div>
        <div class="tool-icon" @click="yiwenOperate" :class="{ isDeny: toolLoading }">
          <div class="iconfont">
            &#58896;
          </div>
          <div class="icon-text">异文</div>
        </div>

        <div class="tool-icon" @click="labelOperate" :class="{ isDeny: toolLoading }">
          <div class="iconfont">
            &#58957;
          </div>
          <div class="icon-text">标注</div>
        </div>

        <div class="tool-icon" @click="setSearchVisible(!showSearchInput)">
          <div class="iconfont">
            &#58893;
          </div>
          <div class="icon-text">搜索</div>
        </div>

        <div class="tool-icon" @click="toTop">
          <div class="iconfont">
            &#58914;
          </div>
          <div class="icon-text">顶部</div>
        </div>
        <div class="tool-icon" @click="toBottom">
          <div class="iconfont">
            &#58915;
          </div>
          <div class="icon-text">底部</div>
        </div>

      </div>
    </div>
    <div class="read-bar" :style="rightBarTheme">
      <div class="tools">
        <div class="tool-icon" @click="toLastChapter">
          <div class="iconfont">
            &#58920;
          </div>
        </div>
        <div class="tool-icon" @click="toNextChapter">
          <div class="iconfont">
            &#58913;
          </div>
        </div>
      </div>
    </div>
    <!-- <div class="chapter-bar"></div> -->
    <div class="chapter" ref="content" :style="chapterTheme" id="pdfDom">
      <!-- 原文展示 -->
      <div class="content">
        <div class="top-bar" ref="top"></div>
        <div class="title" ref="title">{{this.chapter}}</div>
        <!-- <v-if="this.content[0]"> -->
        <Pcontent
          :carray="content"
          :label="lcontent"
          :disDiffCon="disDiffCon"
          @getWenContent="getWenContent"
          @getMouseOver="getMouseOver"
          @getMouseOut="getMouseOut"
          @getOriginal="getOriginal"
          @clickToImg="clickToImg"
          ref="bookreadcon"
        />
        <div class="bottom-bar" ref="bottom"></div>
      </div>

      <!-- 异文展示 -->
      <div class="compare-content" v-if="this.discompare" :style="body">
        <div class="button">
          <el-button
            class="closebutton"
            type="primary"
            icon="el-icon-download"
            circle
            @click="outputWord"
            :class="{ isDeny: downloading }"
          ></el-button>
          <el-button @click="closeCompare" class="closebutton" type="danger" icon="el-icon-close" circle></el-button>
        </div>
        <!-- <el-button @click="closeCompare" class="closebutton">X</el-button> -->
        <div class="diff-bookname">
          <h4>异文典籍:</h4>
          <div class="diffbook-arr">
            <el-select v-model="radio" placeholder="请选择" size="mini" @change="changeTarBook">
              <el-option
                v-for="(item, index) in bookOptions"
                :key="index"
                :label="item"
                :value="item"
              ></el-option>
            </el-select>
          </div>
        </div>
        <div class="diff-read">
          <div
              v-for="(item, index) in diffRead"
              :key="index"
              :style="diffCss"
              class="diff-con"
              v-show="showDiff(item)">
            <p
              class="diff-p"
              :class="{ red: mouseOverBool(item.contentTo) }"
              @click="disDiff(item)"
              :ref="item"
              v-html="item.contentTo"
            ></p>
            <div class="diff-footer">
              <span class="diff-span">《{{item.bookName}}》<br/>{{item.chapterName}}</span>
              <span class="diff-span-right" @click="findOriginal(item)">查看原文</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 原文图片展示 -->
      <div class="compare-content" v-if="this.disoriginal" id="preview" v-loading="originalDiv">
        <div class="button">
          <el-button type="danger" icon="el-icon-close" circle @click="closeOriginal"></el-button>
        </div>
        <div class="after"></div>

        <div
          class="demo-image__preview"
          :class="{ sfix: searchBarFixed }"
          >
          <el-image
            v-loading="imgLoading"
            v-for="(url, index) in imgUrl"
            :key="index"
            :src="url"
            :preview-src-list="imgUrl"
            lazy>
          </el-image>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import PopCata from '../components/chapter/PopCatalog.vue'
import ReadSettings from '../components/chapter/ReadSettings.vue'
import Pcontent from '../components/chapter/Content.vue'
import Search from './navigation/components/search.vue'
import Axios from '../utils/api'
import jump from '../plugins/jump'
import config from '../plugins/config'
import httpAPI from '../utils/port'

const usedWidth = window.screen.width * 0.86 || 1280

export default {
  components: {
    PopCata,
    Pcontent,
    ReadSettings,
    Search
  },
  async created () {
    this.title = JSON.parse(sessionStorage.getItem('store')).currentChapter
    var config = JSON.parse(localStorage.getItem('config'))
    // 如果宽度为异文宽度（异常宽度），则将宽度重置
    // if (config.readWidth === 1280) config.readWidth = 800
    if (config.readWidth > 1120) config.readWidth = 800
    if (config != null) this.$store.commit('setConfig', config)
    console.log('width', window.screen.width)
    this.imgUrlPrefix = httpAPI() // 获取图片前缀
  },
  beforeCreate () {
    const config = JSON.parse(localStorage.getItem('config'))
    if (config != null) this.$store.commit('setConfig', config)
    // console.log('setting', this.$store.state.config)
  },
  async mounted () {
    const res = await Axios.get('/book/getBookInfo/1/100')
    const resData = res.data.data ? res.data.data : res.data
    if (resData) {
      this.bookOptions = []
      resData.forEach(e => {
        this.bookOptions.push(e.bookname)
      })
    }
    Axios.post('/book/bookContent?chapterId=' + JSON.parse(sessionStorage.getItem('store')).chapterId + '&tarBook=' + this.radio)
      .then((resp) => {
        // const contentEle = resp.data.content || resp.content 有问题
        const contentEle = resp.data ? resp.data : resp
        // console.log('content', contentEle)
        // let arr = contentEle.split(/[*/]/)
        // for (let i = 0; i < arr.length; i++) {
        //   console.log('line', arr[i])
        //   if (arr[i].indexOf('\r\n') !== -1) {
        //     // arr.splice(i, 0, '\r\n')
        //     // console.log('splice', arr[i])
        //   }
        // }
        // console.log('split', arr.filter((item) => {
        //   return item.indexOf('\r\n') !== -1
        // }))
        // arr = arr.filter((x) => x !== '')
        const arr = []
        console.log(contentEle)
        contentEle.forEach(e => {
          e = e.filter((x) => x !== '')
          console.log('>', e)
          // 批量导入时换行符为\n\n
          // if (e.length === 1 && e[0].indexOf('\n') !== -1) {
          //   e[0].split('\n').forEach(i => {
          //     arr.push(i)
          //   })
          // } else {
          // }
          arr.push(e)
        })
        this.content = arr
      })

    // this.loading = this.$loading({
    //   target: this.$refs.content,
    //   lock: true,
    //   text: "正在获取内容",
    //   spinner: "el-icon-loading",
    //   background: "rgba(0,0,0,0)"
    // });
    // 获取书籍数据
    // const bookUrl = sessionStorage.getItem('bookUrl')
    // const bookName = sessionStorage.getItem('bookName')
    // const chapterIndex = sessionStorage.getItem('chapterIndex') || 0
    // var book = JSON.parse(localStorage.getItem(bookUrl))
    // if (book == null || chapterIndex > 0) {
    //   book = {
    //     bookName: bookName,
    //     bookUrl: bookUrl,
    //     index: chapterIndex
    //   }
    //   localStorage.setItem(bookUrl, JSON.stringify(book))
    // }
    // 打印输出pcontent的位置

    // window.addEventListener keyup 声明函数
    // 键盘上下左右键控制页面移动
    const that = this
    this.func_keyup = function (event) {
      switch (event.key) {
        case 'ArrowLeft':
          event.stopPropagation()
          event.preventDefault()
          that.toLastChapter()
          break
        case 'ArrowRight':
          event.stopPropagation()
          event.preventDefault()
          that.toNextChapter()
          break
        // case 'ArrowUp':
        //   event.stopPropagation()
        //   event.preventDefault()
        //   if (document.documentElement.scrollTop === 0) {
        //     that.$message.warning('已到达页面顶部')
        //   } else {
        //     jump(0 - document.documentElement.clientHeight + 100)
        //   }
        //   break
        // case 'ArrowDown':
        //   event.stopPropagation()
        //   event.preventDefault()
        //   if (
        //     document.documentElement.clientHeight +
        //       document.documentElement.scrollTop ===
        //     document.documentElement.scrollHeight
        //   ) {
        //     that.$message.warning('已到达页面底部')
        //   } else {
        //     jump(document.documentElement.clientHeight - 100)
        //   }
        //   break
      }
    }
    window.addEventListener('keyup', this.func_keyup)

    // 滚动事件监听
    window.addEventListener('scroll', this.handleScroll)
  },
  destroyed () {
    window.removeEventListener('keyup', this.func_keyup)
    window.removeEventListener('scroll', this.handleScroll)
  },
  watch: {
    chapterName () {
      this.title = this.$store.state.currentChapter
    },
    content () {
      this.$store.commit('setContentLoading', false)
    },
    theme (theme) {
      if (theme === 6) {
        this.isNight = true
      } else {
        this.isNight = false
      }
    },
    bodyColor (color) {
      this.bodyTheme.background = color
    },
    chapterColor (color) {
      this.chapterTheme.background = color
    },
    readWidth (width) {
      this.chapterTheme.width = width
      const leftToolMargin = -((parseInt(width) + 130) / 2 + 68) + 'px'
      const rightToolMargin = -((parseInt(width) + 130) / 2 + 52) + 'px'
      this.leftBarTheme.marginLeft = leftToolMargin
      this.rightBarTheme.marginRight = rightToolMargin
    },
    popupColor (color) {
      this.leftBarTheme.background = color
      this.rightBarTheme.background = color
    },
    readSettingsVisible (visible) {
      if (!visible) {
        const configText = JSON.stringify(this.$store.state.config)
        localStorage.setItem('config', configText)
      }
    }
  },
  data () {
    return {
      title: '',
      content: [],
      lcontent: [],
      disDiffCon: false,
      isNight: this.$store.state.config.theme === 6,
      bodyTheme: {
        background: config.themes[this.$store.state.config.theme].body
      },
      chapterTheme: {
        background: config.themes[this.$store.state.config.theme].content,
        width: this.$store.state.config.readWidth - 130 + 'px'
      },
      leftBarTheme: {
        background: config.themes[this.$store.state.config.theme].popup,
        marginLeft: -(this.$store.state.config.readWidth / 2 + 68) + 'px'
      },
      rightBarTheme: {
        background: config.themes[this.$store.state.config.theme].popup,
        marginRight: -(this.$store.state.config.readWidth / 2 + 52) + 'px'
      },
      discompare: false,
      disoriginal: false,
      // 记录阅读设置（宽度）
      readConfigWidth: '',
      checked: true,
      checkedBooks: [],
      diffbook: [],
      diffRead: [],
      diffCon: [],
      mouseOver: [],
      transYi: '',
      diffCss: {},
      imgUrl: [],
      imgLoading: false,
      searchBarFixed: false,
      originalChapter: '',
      dialogVisible: false,
      // 查看原文得内容
      originalContent: [],
      originalHead: '',
      // 原文功能获得得图片列表
      originalImgList: [],
      diffR: ['备急千金药方', '孙真人千金方'],
      label2: [],
      toolLoading: false,
      radio: '孙真人千金方',
      downloading: false,
      // 判断当前是否在label功能
      thisLabel: false,
      imgUrlPrefix: '',
      // 原文加载
      originalDiv: false,
      // 码表--全部书籍
      bookOptions: [],
      // 弹框可视
      dialogTarBook: false,
      // 异文目标书籍
      tarBook: window.sessionStorage.getItem('tobook') ? window.sessionStorage.getItem('tobook') : '',
      // 目标标记的书籍
      dialogTarLabel: false,
      currentBookName: JSON.parse(sessionStorage.getItem('store')).currentBookName || this.$store.state.currentBookName,
      // 搜索框
      showSearchInput: false,
      // 浏览器最大宽度
      maxPageWidth: window.screen.width
      // usedWidth: 1200 * 0.86 || 1280
    }
  },
  computed: {
    chapter () {
      return JSON.parse(sessionStorage.getItem('store')).currentChapter
    },
    windowHeight () {
      return window.innerHeight
    },
    contentHeight () {
      return this.$refs.content.offsetHeight
    },
    popCataVisible: {
      get () {
        return this.$store.state.popCataVisible
      },
      set (value) {
        this.$store.commit('setPopCataVisible', value)
      }
    },
    readSettingsVisible: {
      get () {
        return this.$store.state.readSettingsVisible
      },
      set (value) {
        this.$store.commit('setReadSettingsVisible', value)
      }
    },
    config () {
      return this.$store.state.config
    },
    theme () {
      return this.config.theme
    },
    body () {
      return { color: this.bodyColor }
    },
    bodyColor () {
      return config.themes[this.config.theme].body
    },
    chapterColor () {
      return config.themes[this.config.theme].content
    },
    popupColor () {
      return config.themes[this.config.theme].popup
    },
    readWidth () {
      return this.$store.state.config.readWidth - 130 + 'px'
    },
    cataWidth () {
      return 700 // this.$store.state.config.readWidth - 28
    },
    position () {
      return this.$refs.bookreadcon.$el.getBoundingClientRect()
    }
  },
  methods: {
    getCatalog (bookUrl) {
      return Axios.post('/getChapterList?url=' + encodeURIComponent(bookUrl))
    },
    // 保存阅读进度
    // const bookUrl = sessionStorage.getItem('bookUrl')
    // const book = JSON.parse(localStorage.getItem(bookUrl))
    // book.index = index
    // localStorage.setItem(bookUrl, JSON.stringify(book))
    // this.$store.state.readingBook.index = index
    // // let chapterUrl = this.$store.state.readingBook.catalog[index].url;
    // const chapterName = this.$store.state.readingBook.catalog[index].title
    // // const chapterIndex = this.$store.state.readingBook.catalog[index].index
    // this.title = chapterName
    // // 强制滚回顶层
    // jump(this.$refs.top, { duration: 0 })
    // const that = this
    // Axios.get(
    //   '/book/bookContent/324fddf24/324324'
    // ).then(
    //   res => {
    //     const data = res.data.data
    //     that.content = data.split(/\n+/)
    //     this.$store.commit('setContentLoading', true)
    //     that.loading.close()
    //     that.noPoint = false
    //     // that.$store.commit('setShowContent', true)
    //   },
    //   err => {
    //     that.$message.error('获取章节内容失败')
    //     that.content = '获取章节内容失败！'
    //     throw err
    //   }
    // )
    // },
    // 滚动事件
    handleScroll () {
      if (this.disoriginal) {
        var scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop
        var offsetTop = document.querySelector('#preview').offsetTop + 40
        if (scrollTop > offsetTop) {
          this.searchBarFixed = true
        } else {
          this.searchBarFixed = false
        }
      }
    },
    getChapContent (chapter) {
      // session中的 封装的目录
      const a = JSON.parse(sessionStorage.getItem('store')).chapterArr
      // 找到跳转章节在目录中的位置
      const objArr = a.filter(function (item) {
        return item.chapter === chapter
      })
      for (let i = 0; i < a.length; i++) {
        if (a[i] === objArr[0]) {
          // console.log(a[i + 1].chapter)
          this.$store.commit('setCurrentChapter', a[i].chapter)
          this.$store.commit('setChapterId', a[i].id)
          window.sessionStorage.setItem('store', JSON.stringify(this.$store.state))
        }
      }
      Axios.post('/book/bookContent?chapterId=' + JSON.parse(sessionStorage.getItem('store')).chapterId + '&tarBook=' + this.radio)
        .then((resp) => {
          const respData = resp.data ? resp.data : resp
          // respData为原文数组（按自然段分）
          if (respData == null || respData === 'error') {
            this.$message({
              type: 'error',
              message: '获取章节失败!'
            })
          } else {
            this.closePopever()
            this.title = this.$store.state.currentChapter
            this.content = respData || ''
            // 刷新页面，重新渲染content
            this.$router.go(0)
          }
        })
    },
    closePopever () {
      this.$store.commit('setPopCataVisible', false)
    },
    // 传入原文，返回异文
    transYiWen (item) {
      this.transYi = ''
      if (this.diffCon.length > 0) {
        this.diffCon.forEach((element) => {
          if (item.indexOf(element.contentFrom) !== -1) {
            this.transYi = element.contentTo
          }
        })
      }
    },
    toYiWen () {
      this.toolLoading = true
      // 修改原文结构
      Axios.post('/book/bookContent?chapterId=' + JSON.parse(sessionStorage.getItem('store')).chapterId + '&tarBook=' + this.radio)
        .then((resp) => {
          const contentEle = resp.data ? resp.data : resp
          const arr = []
          contentEle.forEach(e => {
            e = e.filter((x) => x !== '')
            arr.push(e)
          })
          this.content = arr
        })
      // 判断原文和异文相等
      const formData = new FormData()
      const postId = JSON.parse(sessionStorage.getItem('store')).chapterId
      formData.append('chapterId', postId)
      formData.append('tarBook', this.radio)
      Axios.post('/content/diff/' + postId, formData)
        .then((resp) => {
          this.diffCon = resp.data || resp
          // console.log(this.diffCon, this.content)
          this.diffRead = []
          this.diffCss = {}
          // 初始化checkedBooks
          this.checkedBooks = []
          const list = []
          const reg = /[\u3002|\uff1f|\uff01|\uff0c|\u3001|\uff1b|\uff1a|\u201c|\u201d|\u2018|\u2019|\uff08|\uff09|\u300a|\u300b|\u3008|\u3009|\u3010|\u3011|\u300e|\u300f|\u300c|\u300d|\ufe43|\ufe44|\u3014|\u3015|\u2026|\u2014|\uff5e|\ufe4f|\uffe5|?|!|,|.|/|'|"|;|:|{|}|<|>]/g
          const reg1 = /[?|!|,|.|/|'|"|;|:|{|}|<|>]/g
          if (this.diffCon.length > 0) {
            this.diffCon.forEach((element) => {
              // element -> yiwen
              this.content.forEach(e => {
                e.forEach(e1 => {
                  // e1为原文的段落
                  // console.log('e0', e1.replace(reg, '').replace(reg1, ''))
                  // console.log('e1', element.contentFrom.replace(reg, '').replace(reg1, '').replace('\r\n', ''))
                  // console.log('rep', e1.replace(reg, '').replace('?', '').indexOf(element.contentFrom.replace(reg, '').replace('\r\n', '')))
                  if (e1.replace(reg, '').replace(reg1, '').indexOf(element.contentFrom.replace(reg, '').replace(reg1, '').replace('\r\n', '')) !== -1) {
                  // 获取到异文列表
                    list.push(element)
                  }
                })
              })
            })
            // 如果异文列表中没有数据，说明虽然获取到了数据，但是contentfrom不在原文中
            // if (list.length === 0) {
            //   this.$message({
            //     type: 'warning',
            //     message: '本章没有异文!',
            //     duration: '700'
            //   })
            // }
            this.content.forEach(element => {
              element.forEach(e1 => {
                list.forEach(e => {
                // element是原文一个自然段的数组
                  if (e1.replace(reg, '').replace(reg1, '').indexOf(e.contentFrom.replace(reg, '').replace(reg1, '').replace('\r\n', '')) !== -1) {
                    this.diffRead.push(e)
                    // this.checkedBooks.push(e.bookName)
                  }
                })
              })
            })
            // 当没有展开对比项时才记录config，关闭时可以还原原宽度
            // if (this.diffRead.length > 0) {
            //   if (this.config.readWidth !== 1280) {
            //     // 不使用session就不会持久化（刷新即无）
            //     const config = this.config
            //     this.readConfigWidth = config.readWidth
            //     config.readWidth = 1280
            //     this.$store.commit('setConfig', config)
            //     this.discompare = true
            //     // 异文书籍数组
            //     this.diffbook = JSON.parse(sessionStorage.getItem('store')).diffbook.filter(function (item) {
            //       return item !== JSON.parse(sessionStorage.getItem('store')).currentBookName
            //     })
            //   } else {
            //     this.disoriginal = false
            //     this.discompare = true
            //   }
            // }
          } else {
            this.$message({
              type: 'warning',
              message: '没有找到对应异文！请尝试切换目标书籍',
              duration: '1000'
            })
          }
          // 不管是否有记录都展开
          // 替换 1280-this.usedWidth
          if (this.config.readWidth !== usedWidth) {
            // 不使用session就不会持久化（刷新即无）
            const config = this.config
            this.readConfigWidth = config.readWidth
            config.readWidth = usedWidth
            this.$store.commit('setConfig', config)
            this.discompare = true
            // 异文书籍数组
            this.diffbook = JSON.parse(sessionStorage.getItem('store')).diffbook.filter(function (item) {
              return item !== JSON.parse(sessionStorage.getItem('store')).currentBookName
            })
          } else {
            this.disoriginal = false
            this.discompare = true
          }

          this.toolLoading = false
          // console.timeEnd('异文匹配')
        })
      // this.$router.go(0)
    },
    getWenContent (item, pos) {
      // 不开启异文功能时不能点击
      if (this.config.readWidth !== usedWidth && this.discompare === false) {
        return
      }
      const abPos = pos - 10 + 'px'
      this.diffCss = { position: 'absolute', top: abPos, width: '96%' }
      // 清空异文列表
      this.diffRead = []
      // 判断原文和异文相等
      const formData = new FormData()
      const postId = JSON.parse(sessionStorage.getItem('store')).chapterId
      formData.append('chapterId', postId)
      formData.append('tarBook', this.radio)
      Axios.post('/content/diff/' + postId, formData)
        .then((resp) => {
          // diffCon 为异文数据
          this.diffCon = resp.data ? resp.data : resp
          this.label2 = []
          this.diffCon.forEach(e => {
            const jsonTo = JSON.parse(e.jsonTo)
            // console.log('pushTest', jsonTo)
            this.label2.push(jsonTo)
          })
          // 设置this.transYi成当前段落的异文
          this.transYiWen(item)
          const reg = /[\u3002|\uff1f|\uff01|\uff0c|\u3001|\uff1b|\uff1a|\u201c|\u201d|\u2018|\u2019|\uff08|\uff09|\u300a|\u300b|\u3008|\u3009|\u3010|\u3011|\u300e|\u300f|\u300c|\u300d|\ufe43|\ufe44|\u3014|\u3015|\u2026|\u2014|\uff5e|\ufe4f|\uffe5|?|!|,|.|/|'|"|;|:|{|}|<|>]/g
          const reg1 = /[?|!|,|.|/|'|"|;|:|{|}|<|>]/g
          this.diffCon.forEach(element => {
            if (item.replace(reg, '').replace(reg1, '').indexOf(element.contentFrom.replace(reg, '').replace('\r\n', '')) !== -1) {
              this.diffRead.push(element)
            }
          })
          if (this.diffRead.length > 0) {
            if (this.config.readWidth !== usedWidth) {
              // 不使用session就不会持久化（刷新即无）
              const config = this.config
              this.readConfigWidth = config.readWidth
              config.readWidth = usedWidth
              this.$store.commit('setConfig', config)
              this.discompare = true
              // 异文书籍数组
              this.diffbook = JSON.parse(sessionStorage.getItem('store')).diffbook.filter(function (item) {
                return item !== JSON.parse(sessionStorage.getItem('store')).currentBookName
              })
              // 展示从原文转成异文,等待渲染完毕才能显示
              // this.$nextTick(() => {
              //    this.$refs[this.transYi][0].offsetTop = 200
              // })
            }
            if (this.thisLabel) {
              for (let i = 0; i < this.diffRead.length; i++) {
                const indexArr = []
                let labelIndex = -1
                for (let j = 0; j < this.label2.length; j++) {
                  try {
                    if (this.label2[j].content.indexOf(this.diffRead[i].contentTo) !== -1) {
                      labelIndex = j
                      break
                    }
                  } catch (error) {
                    continue
                  }
                }
                if (labelIndex === -1) {
                  return
                }
                const startI = this.label2[labelIndex].content.indexOf(this.diffRead[i].contentTo)
                if (startI !== -1) {
                  const endI = startI + this.diffRead[i].contentTo.length - 1
                  const ans = []
                  this.label2[labelIndex].labels.forEach(element => {
                    if (element.startIndex >= startI && element.endIndex <= endI + 1) {
                      const arr = [element.startIndex, element.endIndex]
                      indexArr.push(arr)
                    }
                  })
                  if (indexArr.length) {
                    for (let k = 0; k < this.diffRead[i].contentTo.length; k++) {
                      let flag = 0
                      for (let j = 0; j < indexArr.length; j++) {
                        if (k >= indexArr[j][0] && k < indexArr[j][1]) {
                          ans.push('<span style="background-color:yellow">' + this.diffRead[i].contentTo[k] + '</span>')
                          flag++
                          break
                        }
                      }
                      if (flag === 0) {
                        ans.push('<span>' + this.diffRead[i].contentTo[k] + '</span>')
                      }
                    }
                  } else {
                    // for (let i = 0; i < this.diffRead[i].contentTo.length; i++) {
                    //   ans.push('<span>' + this.diffRead[i].contentTo[i] + '</span>')
                    // }
                    ans.push(this.diffRead[i].contentTo)
                  }
                  ans.unshift('<span>')
                  ans.push('</span>')
                  this.diffRead[i].contentTo = ans.join('')
                } else {
                  this.diffRead[i].contentTo = '<span>' + this.diffRead[i].contentTo + '</span>'
                }
              }
            }
          } else {
            if (this.discompare) {
              this.$message({
                type: 'warning',
                message: '本段没有异文!',
                duration: '700'
              })
            }
          }
        })

      // 如果没有打开异文界面
      // if (this.config.readWidth !== 1280) {
      //   // 不使用session就不会持久化（刷新即无）
      //   const config = this.config
      //   this.readConfigWidth = config.readWidth
      //   config.readWidth = 1280
      //   this.$store.commit('setConfig', config)
      //   this.discompare = true
      //   // 异文书籍数组
      //   this.diffbook = JSON.parse(sessionStorage.getItem('store')).diffbook.filter(function (item) {
      //     return item !== JSON.parse(sessionStorage.getItem('store')).currentBookName
      //   })
      //   // 判断原文和异文相等
      //   Axios.get('/content/diff/' + JSON.parse(sessionStorage.getItem('store')).chapterId)
      //     .then((resp) => {
      //       // diffCon 为异文数据
      //       this.diffCon = resp.data || resp
      //       this.diffCon.forEach(element => {
      //         if (item === element.contentFrom) {
      //           this.diffRead.push(element.contentTo)
      //         }
      //       })
      //       if (this.diffRead.length === 0) {
      //         this.$message({
      //           type: 'warning',
      //           message: '该段没有异文'
      //         })
      //       }
      //     })
      // } else { // 已经打开了异文界面
      //   if (this.diffCon) {
      //     this.diffCon.forEach(element => {
      //       if (item === element.contentFrom) {
      //         // 如果异文列表中没有该元素，添加进异文列表中
      //         if (this.diffRead.indexOf(element.contentTo) === -1) {
      //           this.diffRead.push(element.contentTo)
      //         }
      //       }
      //     })
      //   }
      // }
    },
    getOriginal (item) {
      if (this.disoriginal) {
        console.log('click-orignal', item)
      }
    },
    getMouseOver (item) {
      // 异文展示界面的点击逻辑
      if (this.discompare) {
        // item是异文映射的原文
        const reg = /[\u3002|\uff1f|\uff01|\uff0c|\u3001|\uff1b|\uff1a|\u201c|\u201d|\u2018|\u2019|\uff08|\uff09|\u300a|\u300b|\u3008|\u3009|\u3010|\u3011|\u300e|\u300f|\u300c|\u300d|\ufe43|\ufe44|\u3014|\u3015|\u2026|\u2014|\uff5e|\ufe4f|\uffe5|?|!|,|.|/|'|"|;|:|{|}|<|>]/g
        const reg1 = /[?|!|,|.|/|'|"|;|:|{|}|<|>]/g
        if (this.config.readWidth === usedWidth) {
          this.diffCon.forEach((element) => {
            if (item.replace(reg, '').replace(reg1, '').indexOf(element.contentFrom.replace(reg, '').replace('\r\n', '')) !== -1) {
              this.mouseOver.push(element.contentTo)
            }
          })
        }
        // 两个思路，一个是给diffRead封装数据结构，在存放内容的同时存放进样式
        // 第二个是写布尔类型的方法，然后加进去
      }
    },
    getMouseOut () {
      if (this.config.readWidth === usedWidth) {
        this.mouseOver = []
      }
    },
    // 动态改变异文样式
    mouseOverBool (item) {
      if (this.mouseOver.indexOf(item) !== -1) {
        return true
      } else {
        return false
      }
    },
    disDiff (item) {
      // console.log('mouseoveDiff', item)
      // console.log('diff', item === this.transYi)
    },
    toYuanWen () {
      this.toolLoading = true
      this.originalDiv = true
      if (this.config.readWidth === usedWidth) {
        // 清空异文数组
        this.diffRead = []
        // 使点击的段落颜色恢复
        const config = this.config
        // 恢复至原来的宽度
        config.readWidth = this.readConfigWidth ? this.readConfigWidth : 800
        this.$store.commit('setConfig', config)
        this.discompare = false
      }
      const id = JSON.parse(sessionStorage.getItem('store')).chapterId

      Axios.post('/book/bookContent?chapterId=' + id + '&tarBook=' + this.radio)
        .then(resp => {
          const contentEle = resp.data ? resp.data : resp
          // 修改段落结构
          for (const i in contentEle) {
            let str = ''
            contentEle[i].forEach(e => {
              str += e
            })
            contentEle[i] = [str]
          }
          this.content = contentEle
        })
        .then(resp => {
          const formData = new FormData()
          formData.append('chapterId', id)
          Axios.post('/getOriginal', formData).then(resp => {
            const respData = resp.data ? resp.data : resp
            this.originalImgList = respData
            respData.forEach(e => {
              e.forEach(e1 => {
                this.imgUrl.push(this.imgUrlPrefix + e1.imgurl)
              })
            })
            if (this.imgUrl.length === 0) {
              this.$message({
                type: 'warning',
                message: '该章节没有匹配的原文图片！'
              })
              this.originalDiv = false
              this.toolLoading = false
              return
            }

            const config = this.config
            this.readConfigWidth = config.readWidth
            config.readWidth = usedWidth
            this.$store.commit('setConfig', config)
            this.disoriginal = true
            this.toolLoading = false
            this.originalDiv = false
          })
        })
    },
    clickToImg (item) {
      // 原文界面的点击逻辑
      if (this.disoriginal) {
        const formData = new FormData()
        formData.append('chapterId', JSON.parse(sessionStorage.getItem('store')).chapterId)
        // 清空图片
        this.imgUrl = []
        this.imgLoading = true
        this.originalImgList.forEach(e => {
          if (e.length > 0) {
            if (e[0].content.indexOf(item) !== -1) {
              e.forEach(elem => {
                this.imgUrl.push(this.imgUrlPrefix + elem.imgurl)
              })
            }
          }
        })
        this.imgLoading = false
        if (this.imgUrl.length === 0) {
          this.$message.warning('该段缺少原图!')
        }
      }
    },

    // const config = this.config
    // config.readWidth = 800
    // this.$store.commit('setConfig', config)
    closeCompare () {
      // 清空异文数组
      this.diffRead = []
      // 使点击的段落颜色恢复
      const config = this.config
      // 恢复至原来的宽度
      config.readWidth = this.readConfigWidth ? this.readConfigWidth : 800
      this.disDiffCon = false
      this.$store.commit('setConfig', config)
      this.discompare = false
      // 将处在label状态的标签初始化
      this.thisLabel = false
      // 清楚加载框
      this.originalDiv = false
      this.$router.go(0)
    },
    closeOriginal () {
      this.imgUrl = []
      // 使点击的段落颜色恢复
      const config = this.config
      // 恢复至原来的宽度
      config.readWidth = this.readConfigWidth ? this.readConfigWidth : 800
      this.$store.commit('setConfig', config)
      this.disoriginal = false
    },
    toTop () {
      jump(this.$refs.top)
    },
    toBottom () {
      jump(this.$refs.bottom)
    },
    toNextChapter () {
      // session中的 封装的目录
      const a = JSON.parse(sessionStorage.getItem('store')).chapterArr
      // 找到当前章节在目录中的位置
      const objArr = a.filter(function (item) {
        return item.chapter === (JSON.parse(sessionStorage.getItem('store')).currentChapter)
      })
      for (let i = 0; i < a.length; i++) {
        if (a[i] === objArr[0]) {
          if (i < a.length - 1) {
            // console.log(a[i + 1].chapter)
            this.$store.commit('setCurrentChapter', a[i + 1].chapter)
            this.$store.commit('setChapterId', a[i + 1].id)
            window.sessionStorage.setItem('store', JSON.stringify(this.$store.state))
            this.$router.go(0)
          } else {
            this.$message({
              type: 'warning',
              message: '这是最后一章!'
            })
            return
          }
        }
      }
      Axios.post('/book/bookContent?chapterId=' + JSON.parse(sessionStorage.getItem('store')).chapterId + '&tarBook=' + this.radio)
        .then((resp) => {
          console.log('next')
        })
    },
    toLastChapter () {
      // session中的 封装的目录
      const a = JSON.parse(sessionStorage.getItem('store')).chapterArr
      // 找到当前章节在目录中的位置
      const objArr = a.filter(function (item) {
        return item.chapter === (JSON.parse(sessionStorage.getItem('store')).currentChapter)
      })
      for (let i = 0; i < a.length; i++) {
        if (a[i] === objArr[0]) {
          if (i > 0) {
            this.$store.commit('setCurrentChapter', a[i - 1].chapter)
            this.$store.commit('setChapterId', a[i - 1].id)
            window.sessionStorage.setItem('store', JSON.stringify(this.$store.state))
            this.$router.go(0)
          } else {
            this.$message({
              type: 'warning',
              message: '这是第一章!'
            })
            return
          }
        }
      }
      Axios.post('/book/bookContent?chapterId=' + JSON.parse(sessionStorage.getItem('store')).chapterId + '&tarBook=' + this.radio)
        .then((resp) => {
          console.log('last')
        })
    },
    toShelf () {
      this.$router.push('/')
    },
    findOriginal (item) {
      // const a = JSON.parse(sessionStorage.getItem('store')).chapterArr
      // this.originalChapter = ''
      // this.originalHead = ''
      // a.forEach(e => {
      //   console.log('thise', e.chapter)
      //   if (e.chapter === item.chapterName) {
      //     this.originalChapter = e.id
      //     this.originalHead = item.chapterName + '  原文'
      //   }
      // })
      // if (this.originalChapter) {
      //   this.originalContent = []
      //   Axios.get('/book/bookContent/' + this.originalChapter).then(resp => {
      //     const respData = resp.data ? resp.data : resp
      //     for (const i in respData) {
      //       if (respData[i] instanceof Array) {
      //         let str = ''
      //         respData[i].forEach(e1 => {
      //           str += e1
      //         })
      //         this.originalContent.push(str)
      //         continue
      //       }
      //       this.originalContent.push(respData[i])
      //     }
      //     this.dialogVisible = true
      //   })
      // }
      Axios.post('/book/bookContent?chapterId=' + item.chapterId + '&tarBook=' + this.radio)
        .then(resp => {
          const respData = resp.data ? resp.data : resp
          if (respData.length === 0) {
            this.$message.warning('原文内容为空!')
            return
          }
          this.originalHead = item.chapterName + ' 原文'
          this.originalContent = [] // 初始化
          respData.forEach(e => {
            e.forEach(e1 => {
              this.originalContent.push(e1)
            })
          })
          this.dialogVisible = true
        })
    },
    booksChange (val) {
      console.log('booksChangeHandler', this.checkedBooks)
    },
    showDiff (item) {
      return this.radio === item.bookName
    },
    async outputWord () {
      this.downloading = true
      const formData = new FormData()
      formData.append('chapterId', JSON.parse(sessionStorage.getItem('store')).chapterId)
      formData.append('tarBook', this.radio)
      const res = await this.$axios.post('/diff/export', formData, { responseType: 'blob' })
      if (res.status === 200) {
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(new Blob([res.data]))
        link.target = '_blank'
        // 文件名和格式
        link.download = `${JSON.parse(sessionStorage.getItem('store')).currentChapter}_${this.radio}.docx`
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        this.$message({
          type: 'success',
          message: '导出成功!'
        })
      } else {
        this.$message({
          type: 'error',
          message: '导出失败!'
        })
      }
      this.downloading = false
    },
    async toLabel () {
      this.toolLoading = true
      this.thisLabel = true

      const formData = new FormData()
      const postId = JSON.parse(sessionStorage.getItem('store')).chapterId
      formData.append('chapterId', postId)
      formData.append('tarBook', this.radio)
      const res = await Axios.post('/content/diff/' + postId, formData)

      const totalLabel = res.data ? res.data : res
      const label = []
      const label2 = []
      totalLabel.forEach(e => {
        const jsonFrom = JSON.parse(e.jsonFrom)
        const jsonTo = JSON.parse(e.jsonTo)
        label.push(jsonFrom)
        label2.push(jsonTo)
      })
      // 异步方法
      const thisYiWen = async () => {
        // 修改原文结构
        // Axios.post('/book/bookContent?chapterId=' + JSON.parse(sessionStorage.getItem('store')).chapterId + '&tarBook=' + this.radio)
        //   .then((resp) => {
        //     const contentEle = resp.data ? resp.data : resp
        //     const arr = []
        //     contentEle.forEach(e => {
        //       e = e.filter((x) => x !== '')
        //       arr.push(e)
        //     })
        //     this.content = arr
        //   })
        const resp = await Axios.post('/book/bookContent?chapterId=' + JSON.parse(sessionStorage.getItem('store')).chapterId + '&tarBook=' + this.radio)
        const contentEle = resp.data || resp
        const arr = []
        contentEle.forEach(e => {
          e = e.filter((x) => x !== '')
          arr.push(e)
        })

        this.content = arr
        // 判断原文和异文相等
        const formData = new FormData()
        const postId = JSON.parse(sessionStorage.getItem('store')).chapterId
        formData.append('chapterId', postId)
        formData.append('tarBook', this.radio)
        Axios.post('/content/diff/' + postId, formData)
          .then((resp) => {
            this.diffCon = resp.data || resp
            // console.log('diff', this.diffCon)
            this.diffRead = []
            this.diffCss = {}
            // 初始化checkedBooks
            this.checkedBooks = []
            const list = []
            const reg = /[\u3002|\uff1f|\uff01|\uff0c|\u3001|\uff1b|\uff1a|\u201c|\u201d|\u2018|\u2019|\uff08|\uff09|\u300a|\u300b|\u3008|\u3009|\u3010|\u3011|\u300e|\u300f|\u300c|\u300d|\ufe43|\ufe44|\u3014|\u3015|\u2026|\u2014|\uff5e|\ufe4f|\uffe5|?|!|,|.|/|'|"|;|:|{|}|<|>]/g
            const reg1 = /[?|!|,|.|/|'|"|;|:|{|}|<|>]/g
            if (this.diffCon.length > 0) {
              this.diffCon.forEach((element) => {
                this.content.forEach(e => {
                  e.forEach(e1 => {
                    if (e1.replace(reg, '').replace(reg1, '').indexOf(element.contentFrom.replace(reg, '').replace('\r\n', '')) !== -1) {
                      // 获取到异文列表
                      list.push(element)
                    }
                  })
                })
              })
              this.content.forEach(element => {
                element.forEach(e1 => {
                  list.forEach(e => {
                    // element是原文一个自然段的数组
                    if (e1.replace(reg, '').replace(reg1, '').indexOf(e.contentFrom.replace(reg, '').replace('\r\n', '')) !== -1) {
                      this.diffRead.push(e)
                      this.checkedBooks.push(e.bookName)
                    }
                  })
                })
              })
              // 当没有展开对比项时才记录config，关闭时可以还原原宽度
              if (this.diffRead.length > 0) {
                // console.log('diffReadXXX', this.diffRead)
                if (this.config.readWidth !== usedWidth) {
                  // 不使用session就不会持久化（刷新即无）
                  const config = this.config
                  this.readConfigWidth = config.readWidth
                  config.readWidth = usedWidth
                  this.$store.commit('setConfig', config)
                  this.discompare = true
                  // 异文书籍数组
                  this.diffbook = JSON.parse(sessionStorage.getItem('store')).diffbook.filter(function (item) {
                    return item !== JSON.parse(sessionStorage.getItem('store')).currentBookName
                  })
                } else {
                  this.disoriginal = false
                  this.discompare = true
                }

                for (let i = 0; i < this.diffRead.length; i++) {
                  const indexArr = []
                  let labelIndex = -1

                  for (let j = 0; j < label2.length; j++) {
                    try {
                      // console.log('testDii', label2[j], this.diffRead[i])
                      if (label2[j].content.indexOf(this.diffRead[i].contentTo) !== -1) {
                        labelIndex = j
                        break
                      }
                    } catch (error) {
                      continue
                    }
                  }
                  if (labelIndex === -1) {
                    continue
                  }
                  const startI = label2[labelIndex].content.indexOf(this.diffRead[i].contentTo)
                  // console.log('startI111', startI, this.diffRead[i].contentTo.length)
                  if (startI !== -1) {
                    const endI = startI + this.diffRead[i].contentTo.length - 1
                    const ans = []
                    label2[labelIndex].labels.forEach(element => {
                      // console.log('labels111', element)
                      if (element.startIndex >= startI && element.endIndex <= endI + 1) {
                        const arr = [element.startIndex, element.endIndex]
                        indexArr.push(arr)
                      }
                    })
                    // console.log('findLabel111', indexArr)
                    if (indexArr.length) {
                      for (let k = 0; k < this.diffRead[i].contentTo.length; k++) {
                        let flag = 0
                        for (let j = 0; j < indexArr.length; j++) {
                          if (k >= indexArr[j][0] && k < indexArr[j][1]) {
                            ans.push('<span style="background-color:yellow">' + this.diffRead[i].contentTo[k] + '</span>')
                            flag++
                            break
                          }
                        }
                        if (flag === 0) {
                          ans.push('<span>' + this.diffRead[i].contentTo[k] + '</span>')
                        }
                      }
                    } else {
                      try {
                        // for (let i = 0; i < this.diffRead[i].contentTo.length; i++) {
                        //   ans.push('<span>' + this.diffRead[i].contentTo[i] + '</span>')
                        // }
                        ans.push(this.diffRead[i].contentTo)
                      } catch (error) {
                        console.error(error)
                      }
                    }
                    ans.unshift('<span>')
                    ans.push('</span>')
                    // console.log('replaceAnsX!!!!', ans, ans.join(''))
                    this.diffRead[i].contentTo = ans.join('')
                  } else {
                    this.diffRead[i].contentTo = '<span>' + this.diffRead[i].contentTo + '</span>'
                  }
                }
              }
            } else {
              this.$message({
                type: 'warning',
                message: '本章没有异文!',
                duration: '700'
              })
            }
          })
      }
      await thisYiWen()
      this.lcontent = []

      const changeContent = [...this.content]
      changeContent.map(e => {
        if (e.length > 1) {
          e.push('\r')
        }
      })
      changeContent.forEach(e => {
        // console.log('lcontent1', this.lcontent)
        e.forEach(e1 => {
          // const reg = /[\u3002|\uff1f|\uff01|\uff0c|\u3001|\uff1b|\uff1a|\u201c|\u201d|\u2018|\u2019|\uff08|\uff09|\u300a|\u300b|\u3008|\u3009|\u3010|\u3011|\u300e|\u300f|\u300c|\u300d|\ufe43|\ufe44|\u3014|\u3015|\u2026|\u2014|\uff5e|\ufe4f|\uffe5]/g
          // const reg1 = /[?|!|,|.|/|'|"|;|:|{|}|<|>]/g
          // e1 = e1.replaceAll(reg, '').replaceAll(reg1, '')
          if (e1 === '\r') {
            this.lcontent.push('<br>')
            return
          }
          // 找到的label就删除掉
          const indexArr = []

          let labelIndex = -1
          for (let j = 0; j < label.length; j++) {
            if (!label[j]) {
              continue
            }
            try {
              if (label[j].content.indexOf(e1) !== -1) {
                // console.log('indexLabel', j)
                labelIndex = j
                break
              }
            } catch (error) {
              continue
            }
          }
          if (labelIndex === -1) {
            e1 = '<span>' + e1 + '</span>'
            this.lcontent.push(e1)
            this.lcontent.push('<br>')
            return
          }

          const startI = label[labelIndex].content.indexOf(e1)
          // console.log('startI', startI)
          if (startI !== -1) {
            const endI = startI + e1.length - 1
            const ans = []
            // const deleteArr = label.labels
            label[labelIndex].labels.forEach(element => {
              // console.log('labels', element)
              if (element.startIndex >= startI && element.endIndex <= endI + 1) {
                const arr = [element.startIndex, element.endIndex]
                indexArr.push(arr)
              }
            })
            // console.log('findLabel', indexArr)
            if (indexArr.length) {
              for (let i = 0; i < e1.length; i++) {
                let flag = 0
                for (let j = 0; j < indexArr.length; j++) {
                  if (i >= indexArr[j][0] && i < indexArr[j][1]) {
                    ans.push('<span style="background-color:yellow">' + e1[i] + '</span>')
                    flag++
                    break
                  }
                }
                if (flag === 0) {
                  ans.push('<span>' + e1[i] + '</span>')
                }
              }
            } else {
              // for (let i = 0; i < e1.length; i++) {
              //   ans.push('<span>' + e1[i] + '</span>')
              // }
              ans.push(e1)
            }
            ans.unshift('<span>')
            ans.push('</span>')
            // console.log('replaceAns', ans)
            this.disDiffCon = true
            this.lcontent.push(ans.join(''))
          }
          if (e.length === 1) {
            this.lcontent.push('<br>')
          }
        })
      })
      this.toolLoading = false
      console.log('ax', this.lcontent)
    },
    // tarbook 发生变化时
    changeTarBook () {
      this.toYiWen()
    },
    async haveYiwen (tarBook) {
      const formData = new FormData()
      const postId = JSON.parse(sessionStorage.getItem('store')).chapterId
      formData.append('chapterId', postId)
      formData.append('tarBook', tarBook)
      return Axios.post('/content/diff/' + postId, formData).then(res => res)
    },
    // 打开异文项
    async yiwenOperate () {
      try {
        let book = ''
        const booksReq = []
        const awaitToDel = []
        this.bookOptions.forEach(async e => {
          if (e !== this.currentBookName) {
            booksReq.push(this.haveYiwen(e))
            awaitToDel.push(e)
          }
        })
        const ret = await Promise.all(booksReq)
        ret.forEach((e, index) => {
          const retData = e.status === 200 ? e.data : e
          if (retData?.length > 0) {
            book = retData[0].bookName || awaitToDel[0]
          }
        })
        if (book === '') {
          this.$message({
            type: 'warning',
            message: '书库中没有找到其他书籍！'
          })
          return
        }
        this.radio = book
        this.toYiWen()
      } catch (error) {
        this.$message({
          type: 'error',
          message: '书库中没有找到其他书籍！'
        })
      }
    },
    async labelOperate () {
      try {
        let book = ''

        const booksReq = []
        const awaitToDel = []
        this.bookOptions.forEach(async e => {
          if (e !== this.currentBookName) {
            booksReq.push(this.haveYiwen(e))
            awaitToDel.push(e)
          }
        })
        const ret = await Promise.all(booksReq)
        ret.forEach((e, index) => {
          const retData = e.status === 200 ? e.data : e
          if (retData?.length > 0) {
            book = retData[0].bookName || awaitToDel[0]
          }
        })
        // this.bookOptions.forEach(e => {
        //   if (e !== this.currentBookName) {
        //     book = e
        //     return null
        //   }
        // })
        if (book === '') {
          this.$message({
            type: 'warning',
            message: '书库中没有找到其他书籍！'
          })
          return
        }
        this.radio = book
        this.toLabel()
      } catch (error) {
        this.$message({
          type: 'error',
          message: '书库中没有找到其他书籍！'
        })
      }
    },
    setSearchVisible (val) {
      this.showSearchInput = val
    }
  }
}
</script>

<style lang="scss" scoped>
.black {
  color: #262626;
}

.red {
  color: red;
}

.sfix {
  position: fixed;
  top: 0px;
}

.nodisplay {
  display: none;
}

.isDeny { cursor: not-allowed !important; }

::v-deep.pop-setting {
  margin-left: 68px;
  top: 0;
}

::v-deep.pop-cata {
  margin-left: 10px;
}

.chapter-wrapper {
  padding: 0 4%;
  flex-direction: column;
  align-items: center;

  ::v-deep.no-point {
    pointer-events: none;
  }

  .optional-check {
    margin-bottom: 10px;
  }
  .original {
    max-height: 500px;
    overflow: auto;
    font-size: 16px;
    text-indent: 1.5em;
  }

  .tool-bar {
    position: fixed;
    top: 0;
    left: 50%;
    z-index: 100;

    .tools {
      display: flex;
      flex-direction: column;

      .isDeny {
        cursor: not-allowed !important;
      }

      .tool-icon {
        font-size: 18px;
        width: 58px;
        height: 48px;
        text-align: center;
        padding-top: 12px;
        cursor: pointer;
        outline: none;

        .iconfont {
          font-family: iconfont;
          width: 16px;
          height: 16px;
          font-size: 16px;
          margin: 0 auto 6px;
        }

        .icon-text {
          font-size: 12px;
        }
      }
    }
  }

  .read-bar {
    position: fixed;
    bottom: 0;
    right: 50%;
    z-index: 100;

    .tools {
      display: flex;
      flex-direction: column;

      .tool-icon {
        font-size: 18px;
        width: 42px;
        height: 31px;
        padding-top: 12px;
        text-align: center;
        align-items: center;
        cursor: pointer;
        outline: none;
        margin-top: -1px;

        .iconfont {
          font-family: iconfont;
          width: 16px;
          height: 16px;
          font-size: 16px;
          margin: 0 auto 6px;
        }
      }
    }
  }

  .chapter-bar {
    .el-breadcrumb {
      .item {
        font-size: 14px;
        color: #606266;
      }
    }
  }

  .chapter {
    font-family: 'Microsoft YaHei', PingFangSC-Regular, HelveticaNeue-Light, 'Helvetica Neue Light', sans-serif;
    text-align: left;
    padding: 0 65px;
    min-height: 100vh;
    width: 670px;
    margin: 0 auto;

    display: flex;
    justify-content: space-around;

    ::v-deep.el-icon-loading {
      font-size: 36px;
      color: #B5B5B5;
    }

    ::v-deep.el-loading-text {
      font-weight: 500;
      color: #B5B5B5;
    }

    .content {
      // min-width: 670px;
      font-size: 18px;
      line-height: 1.8;
      overflow: hidden;
      text-align: justify;
      font-family: 'Microsoft YaHei', PingFangSC-Regular, HelveticaNeue-Light, 'Helvetica Neue Light', sans-serif;

      .title {
        margin-bottom: 38px;
        font: 24px / 32px PingFangSC-Regular, HelveticaNeue-Light, 'Helvetica Neue Light', 'Microsoft YaHei', sans-serif;
      }

      .bottom-bar, .top-bar {
        height: 48px;
      }
    }

    .original-content {
      z-index: 999;
      min-width: 350px; // this.$store.commit('setConfig', config.readWidth=1280)
      // background-color: rgba(230, 242, 230, 0.8);
      border-left: rgb(160,160,160) 2px dashed;
      padding: 48px 0 0 20px;
      position: relative;
    }

    .compare-content {
      z-index: 999;
      min-width: 380px; // this.$store.commit('setConfig', config.readWidth=1280)
      // width: 100%;
      // background-color: rgba(230, 242, 230, 0.8);
      border-left: rgb(160,160,160) 2px dashed;
      padding: 48px 0px 0px 20px;
      position: relative;
      text-align: justify;

      .demo-image__preview {
        height: 100vh;
        width: 460px;
        overflow: scroll;
        margin: 0px 40px 0px 20px;
        .el-image {
          display: block;
          min-height: 200px;
          margin-bottom: 10px;
        }
      }

      .after {
        height: 40px;
      }
      .button {
        position: absolute;
        top: 5px;
        display: block;
        margin-left: 70%;
      }

      .diff-bookname {
        min-height: 50px;
        display: flex;
        align-items: center;
        min-height: 60px;
        border-bottom: rgb(160, 160, 160) 2px dashed;

        h4 {
          display: block;
          margin-right: 15px;
          min-width: 75px;
        }

        ::v-deep .diffbook-arr {
          font-size: 15px;
          line-height: 20px;
          .el-checkbox {
            &__label {
              font-size: 16px;
            }
          }
        }
      }

      .diff-read {
        .diff-con {
          border-bottom: 2px #666 dashed;
          .diff-p {
            font: 20px PingFangSC-Regular, -apple-system, Simsun;;
            margin: 10px 0 10px 0;
            padding-bottom: 0px;
          }
          .diff-span {
            display: inline-block;
            max-width: 320px;
            font: 14px / 16px PingFangSC-Regular, '-apple-system', Simsun;
            font-weight: 700;
            padding-bottom: 5px;
          }
          .diff-span-right {
            display: inline-block;
            margin: auto 0px auto 150px;
            font: 14px / 16px PingFangSC-Regular, '-apple-system', Simsun;
            padding-bottom: 5px;
            cursor: pointer;
            &:hover {
              color: red;
            }
          }

          .diff-footer {
            margin: 0px 30px 0px 0px;
            display: flex;
            justify-content: space-between
          }
        }
      }
    }
  }
}

.day {
  ::v-deep.popup {
    box-shadow: 0 5px 20px rgb(0 0 0 / 20%);;
  }

  ::v-deep.tool-icon {
    border: 1px solid rgba(0, 0, 0, 0.1);
    margin-top: -1px;
    color: #000;

    .icon-text {
      color: rgba(0, 0, 0, 0.4);
    }
  }

  ::v-deep.chapter {
    border: 1px solid #d8d8d8;
    color: #262626;
  }
}

.night {
  ::v-deep.popup {
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.48), 0 0 6px rgba(0, 0, 0, 0.16);
  }

  ::v-deep.tool-icon {
    border: 1px solid #444;
    margin-top: -1px;
    color: #666;

    .icon-text {
      color: #666;
    }
  }

  ::v-deep.chapter {
    border: 1px solid #444;
    color: #666;
  }

  ::v-deep.popper__arrow {
    background: #666;
  }
}

::v-deep .el-popover {
  position: absolute;
  left: 68px;
  top: 0px;
}

::v-deep .el-input {
  margin-left: 10px;
  width: 100%;
}

.search-item {
  position: fixed;
  // position: -webkit-sticky;
  top: 30px;
  left: 30vw;
  z-index: 1000;
  max-width: 500px;
  width: 50vw;
}
</style>
