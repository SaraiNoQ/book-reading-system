<template>
  <div
    class="chapter-wrapper"
    :style="bodyTheme"
    :class="{ night: isNight, day: !isNight }"
  >
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
          <div class="tool-icon" slot="reference">
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

        <div class="tool-icon" @click="toYuanWen">
          <div class="iconfont">
            &#58902;
          </div>
          <div class="icon-text">原文</div>
        </div>
        <div class="tool-icon" @click="toYiWen">
          <div class="iconfont">
            &#58896;
          </div>
          <div class="icon-text">异文</div>
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
    <div class="chapter" ref="content" :style="chapterTheme">
      <div class="content">
        <div class="top-bar" ref="top"></div>
        <div class="title" ref="title">{{this.chapter}}</div>
        <!-- <v-if="this.content[0]"> -->
        <Pcontent
          :carray="content"
          @getWenContent="getWenContent"
          @getMouseOver="getMouseOver"
          @getMouseOut="getMouseOut"
          ref="bookreadcon"
        />
        <div class="bottom-bar" ref="bottom"></div>
      </div>
      <div class="compare-content" v-if="this.discompare" :style="body">
        <el-button @click="closeCompare" class="closebutton" type="danger" icon="el-icon-close" circle></el-button>
        <!-- <el-button @click="closeCompare" class="closebutton">X</el-button> -->
        <div class="diff-bookname">
          <h4>异文典籍:</h4>
          <div class="diffbook-arr">
            <el-checkbox-group v-model="checkedBooks">
              <el-checkbox v-for="book in diffbook" :label="book" :key="book">{{book}}</el-checkbox>
            </el-checkbox-group>
          </div>
        </div>
        <div class="diff-read">
          <p
            v-for="(item, index) in diffRead"
            :key="index"
            class="diff-p"
            :class="{ red: mouseOverBool(item) }"
            :style="diffCss"
            @click="disDiff(item)"
            :ref="item"
          >{{item}}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import PopCata from '../../components/chapter/PopCatalog.vue'
import ReadSettings from '../../components/chapter/ReadSettings.vue'
import Pcontent from '../../components/chapter/Content.vue'
import Axios from 'axios'
import jump from '../../plugins/jump'
import config from '../../plugins/config'
export default {
  components: {
    PopCata,
    Pcontent,
    ReadSettings
  },
  created () {
    this.title = JSON.parse(sessionStorage.getItem('store')).currentChapter
    var config = JSON.parse(localStorage.getItem('config'))
    // 如果宽度为异文宽度（异常宽度），则将宽度重置
    if (config.readWidth === 1280) config.readWidth = 800
    if (config != null) this.$store.commit('setConfig', config)
    // console.log('settings', this.$store.state.config)
  },
  beforeCreate () {
    const config = JSON.parse(localStorage.getItem('config'))
    if (config != null) this.$store.commit('setConfig', config)
    // console.log('setting', this.$store.state.config)
  },
  mounted () {
    // console.log('/book/bookContent/' + this.$store.state.currentBookName + '/' + this.$store.state.currentChapter)
    Axios.get('/book/bookContent/' + JSON.parse(sessionStorage.getItem('store')).chapterId)
      .then((resp) => {
        console.log('resp', resp)
        // const contentEle = resp.data.content || resp.content 有问题
        const contentEle = resp.data ? resp.data : resp
        console.log('element', contentEle)
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
        // console.log('split', arr)
        const arr = []
        contentEle.forEach(e => {
          e = e.filter((x) => x !== '')
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
        case 'ArrowUp':
          event.stopPropagation()
          event.preventDefault()
          if (document.documentElement.scrollTop === 0) {
            that.$message.warning('已到达页面顶部')
          } else {
            jump(0 - document.documentElement.clientHeight + 100)
          }
          break
        case 'ArrowDown':
          event.stopPropagation()
          event.preventDefault()
          if (
            document.documentElement.clientHeight +
              document.documentElement.scrollTop ===
            document.documentElement.scrollHeight
          ) {
            that.$message.warning('已到达页面底部')
          } else {
            jump(document.documentElement.clientHeight - 100)
          }
          break
      }
    }

    // 获取书籍目录
    // this.getCatalog(bookUrl).then(
    //   res => {
    //     let catalog = res.data.data;
    //     book.catalog = catalog;
    //     that.$store.commit("setReadingBook", book);
    //     var index = that.$store.state.readingBook.index || 0;
    //     this.getContent(index);
    //     window.addEventListener("keyup", this.func_keyup);
    //   },
    //   err => {
    //     that.loading.close();
    //     that.$message.error("获取书籍目录失败");
    //     throw err;
    //   }
    // );
  },
  destroyed () {
    window.removeEventListener('keyup', this.func_keyup)
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
      // 记录阅读设置（宽度）
      readConfigWidth: '',
      checked: true,
      checkedBooks: [],
      diffbook: [],
      diffRead: [],
      diffCon: [],
      mouseOver: [],
      transYi: '',
      diffCss: {}
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
        // console.log('set')
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
    // show () {
    //   return this.$store.state.showContent
    // }
  },
  methods: {
    getCatalog (bookUrl) {
      return Axios.get('/getChapterList?url=' + encodeURIComponent(bookUrl))
    },
    // getContent (index) {
    // 展示进度条
    // this.$store.commit('setShowContent', false)
    // if (!this.loading.visible) {
    //   this.loading = this.$loading({
    //     target: this.$refs.content,
    //     lock: true,
    //     text: '正在获取内容',
    //     spinner: 'el-icon-loading',
    //     background: 'rgba(0,0,0,0)'
    //   })
    // }
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
      // console.log('/book/Content/' + this.$store.state.currentBookName + '/' + chapter)
      Axios.get('/book/bookContent/' + JSON.parse(sessionStorage.getItem('store')).chapterId)
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
            // console.log(this.$store.state.popCataVisable)
            this.title = this.$store.state.currentChapter
            this.content = respData || ''
            // 刷新页面，重新渲染content
            this.$router.go(0)
          }
        })
    },
    closePopever () {
      // console.log('close')
      this.$store.commit('setPopCataVisible', false)
    },
    // 传入原文，返回异文
    transYiWen (item) {
      this.transYi = ''
      if (this.diffCon.length > 0) {
        this.diffCon.forEach((element) => {
          // console.log('diffCon1', this.diffCon.length)
          if (item.indexOf(element.contentFrom) !== -1) {
            this.transYi = element.contentTo
          }
        })
      }
    },
    toYiWen () {
      // 判断原文和异文相等
      Axios.get('/content/diff/' + JSON.parse(sessionStorage.getItem('store')).chapterId)
        .then((resp) => {
          this.diffCon = resp.data || resp
          console.log('diff', this.diffCon)
          this.diffRead = []
          this.diffCss = {}
          const list = []
          const reg = /[\u3002|\uff1f|\uff01|\uff0c|\u3001|\uff1b|\uff1a|\u201c|\u201d|\u2018|\u2019|\uff08|\uff09|\u300a|\u300b|\u3008|\u3009|\u3010|\u3011|\u300e|\u300f|\u300c|\u300d|\ufe43|\ufe44|\u3014|\u3015|\u2026|\u2014|\uff5e|\ufe4f|\uffe5]/g
          if (this.diffCon.length > 0) {
            this.diffCon.forEach((element) => {
              // element -> yiwen
              // console.log('diffCon', element)
              this.content.forEach(e => {
                e.forEach(e1 => {
                  // e1为原文的段落
                  // console.log('final', str1 === str2)
                  if (e1.replace(reg, '').indexOf(element.contentFrom.replace(reg, '').replace('\r\n', '')) !== -1) {
                  // 获取到异文列表
                    // console.log('pipei', e, e1, element)
                    list.push(element)
                  }
                })
              })
              // if (this.content.indexOf(element.contentFrom) !== -1) {
              //   // 获取到异文列表
              //   list.push(element)
              // }
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
                  if (e1.replace(reg, '').indexOf(e.contentFrom.replace(reg, '').replace('\r\n', '')) !== -1) {
                    this.diffRead.push(e.contentTo)
                  }
                })
              })
            })
            // 当没有展开对比项时才记录config，关闭时可以还原原宽度
            if (this.diffRead.length > 0) {
              if (this.config.readWidth !== 1280) {
                // 不使用session就不会持久化（刷新即无）
                const config = this.config
                this.readConfigWidth = config.readWidth
                config.readWidth = 1280
                this.$store.commit('setConfig', config)
                this.discompare = true
                // 异文书籍数组
                this.diffbook = JSON.parse(sessionStorage.getItem('store')).diffbook.filter(function (item) {
                  return item !== JSON.parse(sessionStorage.getItem('store')).currentBookName
                })
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
      // this.$router.go(0)
    },
    getWenContent (item, pos) {
      console.log('clickEvent', item)
      const abPos = pos - 10 + 'px'
      this.diffCss = { position: 'absolute', top: abPos }
      // 清空异文列表
      this.diffRead = []
      // 判断原文和异文相等
      Axios.get('/content/diff/' + JSON.parse(sessionStorage.getItem('store')).chapterId)
        .then((resp) => {
          // console.log('getWen', resp)
          // diffCon 为异文数据
          this.diffCon = resp.data || resp
          // 设置this.transYi成当前段落的异文
          this.transYiWen(item)
          const reg = /[\u3002|\uff1f|\uff01|\uff0c|\u3001|\uff1b|\uff1a|\u201c|\u201d|\u2018|\u2019|\uff08|\uff09|\u300a|\u300b|\u3008|\u3009|\u3010|\u3011|\u300e|\u300f|\u300c|\u300d|\ufe43|\ufe44|\u3014|\u3015|\u2026|\u2014|\uff5e|\ufe4f|\uffe5]/g
          this.diffCon.forEach(element => {
            console.log('click-diffCon', item)
            // item.forEach(e => {
            //   if (e.replace(reg, '').indexOf(element.contentFrom.replace(reg, '').replace('\r\n', '')) !== -1) {
            //   // console.log('push', this.diffRead)
            //     this.diffRead.push(element.contentTo)
            //   }
            if (item.replace(reg, '').indexOf(element.contentFrom.replace(reg, '').replace('\r\n', '')) !== -1) {
            // console.log('push', this.diffRead)
              this.diffRead.push(element.contentTo)
            }
          })
          if (this.diffRead.length > 0) {
            if (this.config.readWidth !== 1280) {
              // 不使用session就不会持久化（刷新即无）
              const config = this.config
              this.readConfigWidth = config.readWidth
              config.readWidth = 1280
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
          } else {
            this.$message({
              type: 'warning',
              message: '本段没有异文!',
              duration: '700'
            })
            // this.closeCompare()
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
    getMouseOver (item) {
      // item是异文映射的原文
      const reg = /[\u3002|\uff1f|\uff01|\uff0c|\u3001|\uff1b|\uff1a|\u201c|\u201d|\u2018|\u2019|\uff08|\uff09|\u300a|\u300b|\u3008|\u3009|\u3010|\u3011|\u300e|\u300f|\u300c|\u300d|\ufe43|\ufe44|\u3014|\u3015|\u2026|\u2014|\uff5e|\ufe4f|\uffe5]/g
      if (this.config.readWidth === 1280) {
        this.diffCon.forEach((element) => {
          if (item.replace(reg, '').indexOf(element.contentFrom.replace(reg, '').replace('\r\n', '')) !== -1) {
            // console.log('push', this.diffRead)
            this.mouseOver.push(element.contentTo)
          }
        })
      }
      // 两个思路，一个是给diffRead封装数据结构，在存放内容的同时存放进样式
      // 第二个是写布尔类型的方法，然后加进去
    },
    getMouseOut () {
      if (this.config.readWidth === 1280) {
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
      console.log('mouseoveDiff', item)
      console.log('diff', item === this.transYi)
    },
    toYuanWen () {
      const config = this.config
      config.readWidth = 800
      this.$store.commit('setConfig', config)
    },
    closeCompare () {
      // 清空异文数组
      this.diffRead = []
      // 使点击的段落颜色恢复
      const config = this.config
      // 恢复至原来的宽度
      config.readWidth = this.readConfigWidth ? this.readConfigWidth : 800
      // console.log(config.readWidth)
      this.$store.commit('setConfig', config)
      this.discompare = false
      this.$router.go(0)
      // console.log(this.$store.state.config)
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
      Axios.get('/book/bookContent/' + JSON.parse(sessionStorage.getItem('store')).chapterId)
        .then((resp) => {
          console.log('next', resp)
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
      Axios.get('/book/bookContent/' + JSON.parse(sessionStorage.getItem('store')).chapterId)
        .then((resp) => {
          console.log('last', resp)
        })
    },
    toShelf () {
      this.$router.push('/')
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

  .tool-bar {
    position: fixed;
    top: 0;
    left: 50%;
    z-index: 100;

    .tools {
      display: flex;
      flex-direction: column;

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
      min-width: 670px;
      font-size: 18px;
      line-height: 1.8;
      overflow: hidden;
      font-family: 'Microsoft YaHei', PingFangSC-Regular, HelveticaNeue-Light, 'Helvetica Neue Light', sans-serif;

      .title {
        margin-bottom: 38px;
        font: 24px / 32px PingFangSC-Regular, HelveticaNeue-Light, 'Helvetica Neue Light', 'Microsoft YaHei', sans-serif;
      }

      .bottom-bar, .top-bar {
        height: 48px;
      }
    }

    .compare-content {
      z-index: 999;
      min-width: 480px; // this.$store.commit('setConfig', config.readWidth=1280)
      // background-color: rgba(230, 242, 230, 0.8);
      border-left: rgb(160,160,160) 2px dashed;
      padding: 48px 0 0 20px;
      position: relative;

      .closebutton {
        position: absolute;
        top: 3px;
        right: 30px;
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
        .diff-p {
          margin: 7px 0 0 0;
          padding-bottom: 10px;
          border-bottom: 2px #666 dashed;
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
</style>
