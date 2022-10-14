<template>
  <div v-loading="pageLoading" class="container">
    <div class="search-form">
      <el-form label-position="left" label-width="80px" :model="searchForm" class="search-tab">

        <el-form-item label="关键字" class="keyword-line">
          <div class="search-input">
            <el-input v-model="searchForm.key" placeholder="请输入关键字"></el-input>
            <!-- <el-checkbox v-model="checkedFont" class="diff-font">查询异体字</el-checkbox> -->
          </div>
        </el-form-item>
        <el-form-item class="optional">
          <el-checkbox v-model="checkedMatch">模 糊 匹 配</el-checkbox>
          <el-checkbox v-model="checkedFont">查询异体字</el-checkbox>
        </el-form-item>

        <el-form-item label="书  名" style="marginBottom: 10px;">
            <div class="search-input">
              <el-select v-model="choosedBookName" placeholder="不选则为搜索全部书籍" clearable>
                <el-option class="check"
                  v-for="(item, index) in bookArr"
                  :key="index"
                  :label="item"
                  :value="item"
                ></el-option>
              </el-select>
              <!-- <el-input v-model="searchForm.name" placeholder="请输入正确书名"></el-input> -->
              <!-- <el-checkbox v-model="checkedMatch" class="diff-font">模 糊 匹 配</el-checkbox> -->
            </div>
        </el-form-item>

        <el-form-item label="类  别" style="marginBottom:0px;">
          <div class="check-books">
            <!-- <el-checkbox
              :indeterminate="isIndeterminate"
              v-model="checkAll"
              @change="handleCheckAllChange"
              class="first-check"
            >全部内容</el-checkbox> -->

            <el-checkbox-group v-model="checkedBooks" @change="handleCheckedBooksChange">
              <el-checkbox
                v-for="book in searchBooks"
                :key="book"
                :label="book">{{book}}</el-checkbox>
            </el-checkbox-group>
          </div>
        </el-form-item>
      </el-form>
      <div class="button-flex">
        <el-button class="search-button" type="primary" @click="search" :loading="searchLoading">🔍 搜 索</el-button>
        <el-button class="search-button" type="primary" @click="resetForm">重 置</el-button>
        <span class="output-button" @click="outputSearch" :class="{ deny: outputLoading }">导出结果</span>
        <!-- <span class="output-diff" @click="connectDiff">搜索异文</span> -->
      </div>
    </div>

    <search-outcomes
      :outcomes="outcomes"
      :content="content"
      :pageSize="pageSize"
      :currentPage="currentPage"
      :totalItems="totalItems"
      @changePageSize="changePageSize"
      @changeCurrentPage="changeCurrentPage"
      :outcomesLoading="outcomesLoading"
      @changePageLoading="changePageLoading"
      v-if="showOutcome"
    />

    <div><el-backtop target=".el-main"></el-backtop></div>
  </div>
</template>

<script>
import Axios from '../../utils/api'
import searchOutcomes from './components/SearchOutcomes'
const books = ['方剂', '注', '医论', '针灸', '其他疗法']
export default {
  components: { searchOutcomes },
  async mounted () {
    const res = await Axios.get('/book/getBookInfo/1/100')
    const resData = res.data.data ? res.data.data : res.data
    if (resData) {
      resData.forEach(e => {
        this.bookArr.push(e.bookname)
      })
    }
  },
  data () {
    return {
      checkAll: true,
      searchForm: {
        name: '',
        key: this.$store.state.supterKeyword
      },
      searchBooks: books,
      checkedBooks: [...books],
      // isIndeterminate: false,
      checkedMatch: false,
      checkedFont: false,
      outcomes: {
        keyword: '',
        category: '',
        nums: 0,
        bookname: ''
      },
      content: [],
      showOutcome: false,
      outputLoading: false,
      searchLoading: false,
      // 存放图书名的数组
      bookArr: [],
      choosedBookName: '',
      pageSize: 20, // 每页显示的条数
      currentPage: 1, // 当前页数
      totalItems: 0, // 总条数
      pageLoading: false, // 加载中
      outcomesLoading: false, // 组件加载状态
      contentReg: new RegExp(/(((<[a-zA-Z-]+?){0,1}>))([\s\S]+)(([\s]{0,1}<\/[a-zA-Z-]+(>{0,1})))/g),
      labelReg: new RegExp(/[\u3002|\uff1f|\uff01|\uff0c|\u3001|\uff1b|\uff1a|\u201c|\u201d|\u2018|\u2019|\uff08|\uff09|\u300a|\u300b|\u3008|\u3009|\u3010|\u3011|\u300e|\u300f|\u300c|\u300d|\ufe43|\ufe44|\u3014|\u3015|\u2026|\u2014|\uff5e|\ufe4f|\uffe5|?|!|,|.|/|'|"|;|:|{|}|<|>|\r\n|(?<!\r\n)\n(?!\r\n)|(?<!\r\n|\n)\r(?!\r\n|\n)]/g)
    }
  },
  computed: {
    keywordCom () {
      return this.$store.state.supterKeyword
    }
  },
  watch: {
    keywordCom (val) {
      this.searchForm.key = val
    }
  },
  methods: {
    // handleCheckAllChange (val) {
    //   this.checkedBooks = val ? books : []
    //   this.isIndeterminate = false
    // },
    handleCheckedBooksChange (value) {
      const checkedCount = value.length
      this.checkAll = checkedCount === this.searchBooks.length
      // this.isIndeterminate = checkedCount > 0 && checkedCount < this.searchBooks.length
    },
    resetForm () {
      this.searchForm = {}
      this.choosedBookName = ''
      this.showOutcome = false
    },
    changed (item) {
      let contentIndex = item.content.indexOf(item.content.match(this.contentReg))
      let flag = 0
      if (contentIndex > 23) {
        for (let i = contentIndex - 24; i >= 0; i--) {
          if (this.labelReg.test(item.content[i])) {
            contentIndex = i + 1
            flag = 1
            break
          }
        }
      }
      if (flag) {
        this.content.push({
          content: item.content.substring(contentIndex, item.content.length),
          bookname: item?.bookname,
          chapter: item.ts_book_chapter_id ? item.ts_book_chapter_id : item.chapterid
        })
      } else {
        this.content.push({
          content: item.content.substring(contentIndex - 23, item.content.length),
          bookname: item?.bookname,
          chapter: item.ts_book_chapter_id ? item.ts_book_chapter_id : item.chapterid
        })
      }
    },
    search () {
      if (!this.searchForm.key || this.checkedBooks.length === 0) {
        this.$message.error('请勾选查询内容的类别！')
        return
      }
      this.searchLoading = true
      this.outcomes.keyword = this.searchForm.key
      this.outcomes.category = this.checkAll ? ['全部内容'] : this.checkedBooks
      this.outcomes.bookname = this.choosedBookName ? this.choosedBookName : '全部书籍'
      this.outcomes.nums = 0
      const formData = new FormData()
      formData.append('isExport', '否')
      if (this.checkedMatch) {
        formData.append('searchType', '分词')
      } else {
        formData.append('searchType', '不分')
      }
      formData.append('keyword', this.searchForm.key)
      formData.append('bookName', this.choosedBookName ? [this.choosedBookName] : ['ALL'])
      formData.append('page', this.currentPage)
      formData.append('size', this.pageSize)
      formData.append('type', this.checkAll ? ['ALL'] : this.checkedBooks)
      // console.log('checkbooks', this.checkedBooks)
      this.$axios.post('/Search/content', formData)
        .then(resp => {
          const respData = resp.data ? resp.data : resp
          this.content = []
          respData.forEach(e => {
            this.changed(e)
          })
          console.log('search', this.content)
          this.outcomes.nums = respData[0].total
          this.totalItems = respData[0].total
          this.searchLoading = false
          this.showOutcome = true
        }).catch(() => {
          this.showOutcome = true
          this.searchLoading = false
        })
    },
    async outputSearch () {
      if (this.showOutcome) {
        this.outputLoading = true
        const formData = new FormData()
        // export
        formData.append('isExport', '是')
        if (this.checkedMatch) {
          formData.append('searchType', '分词')
        } else {
          formData.append('searchType', '不分')
        }
        formData.append('keyword', this.searchForm.key)
        formData.append('bookName', this.choosedBookName ? [this.choosedBookName] : ['ALL'])
        formData.append('page', 1)
        formData.append('size', 100)
        formData.append('type', this.checkAll ? ['ALL'] : this.checkedBooks)
        const res = await this.$axios.post('/Search/content', formData, { responseType: 'blob' })
        if (res.status === 200) {
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(new Blob([res.data]))
          link.target = '_blank'
          // 文件名和格式
          link.download = `${this.searchForm.name ? this.searchForm.name : '全部书籍'}中包含‘${this.searchForm.key}’类别为${this.checkAll ? '全部' : this.checkedBooks}.docx`
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          this.$message({
            type: 'success',
            message: '导出成功!'
          })
        } else {
          this.$message({ type: 'error', message: '导出失败!' })
        }
        this.outputLoading = false
      } else {
        this.$message({ type: 'error', message: '请先搜索内容!' })
      }
    },
    changePageSize (val) {
      this.outcomesLoading = true
      // 修改pageSize的值
      this.pageSize = val
      this.outcomes.keyword = this.searchForm.key
      this.outcomes.category = this.checkAll ? ['全部内容'] : this.checkedBooks
      this.outcomes.bookname = this.choosedBookName ? this.choosedBookName : '全部书籍'
      const formData = new FormData()
      formData.append('isExport', '否')
      if (this.checkedMatch) {
        formData.append('searchType', '分词')
      } else {
        formData.append('searchType', '不分')
      }
      formData.append('keyword', this.searchForm.key)
      formData.append('bookName', this.choosedBookName ? [this.choosedBookName] : ['ALL'])
      formData.append('page', this.currentPage)
      formData.append('size', this.pageSize)
      formData.append('type', this.checkAll ? ['ALL'] : this.checkedBooks)
      this.$axios.post('/Search/content', formData)
        .then(resp => {
          const respData = resp.data ? resp.data : resp
          this.content = []
          respData.forEach(e => {
            this.changed(e)
            // const obj = {
            //   content: e.content,
            //   // 两个数据库中的字段不一样
            //   chapter: e.ts_book_chapter_id ? e.ts_book_chapter_id : e.chapterid,
            //   bookname: e.bookname
            // // 段落id，可能为空
            // }
            // this.content.push(obj)
          })
          this.totalItems = respData[0].total
          this.showOutcome = true
          this.outcomesLoading = false
        }).catch(() => {
          this.outcomesLoading = false
        })
    },
    changeCurrentPage (val) {
      // 设置组件loading状态
      this.outcomesLoading = true
      this.currentPage = val
      this.outcomes.keyword = this.searchForm.key
      this.outcomes.category = this.checkAll ? ['全部内容'] : this.checkedBooks
      this.outcomes.bookname = this.choosedBookName ? this.choosedBookName : '全部书籍'
      const formData = new FormData()
      formData.append('isExport', '否')
      if (this.checkedMatch) {
        formData.append('searchType', '分词')
      } else {
        formData.append('searchType', '不分')
      }
      formData.append('keyword', this.searchForm.key)
      formData.append('bookName', this.choosedBookName ? [this.choosedBookName] : ['ALL'])
      formData.append('page', this.currentPage)
      formData.append('size', this.pageSize)
      formData.append('type', this.checkAll ? ['ALL'] : this.checkedBooks)
      // console.log('checkbooks', this.checkedBooks)
      this.$axios.post('/Search/content', formData).then(resp => {
        const respData = resp.data ? resp.data : resp
        this.content = []
        respData.forEach(e => {
          this.changed(e)
        })
        this.totalItems = respData[0].total
        this.showOutcome = true
        this.outcomesLoading = false
      }).catch(() => {
        this.outcomesLoading = false
      })
    },
    changePageLoading (val) {
      this.pageLoading = val
    },
    connectDiff () {
      this.$router.push('/search/diff')
    }
  },
  beforeDestroy () {
    this.showOutcome = false
    this.$store.commit('setSupterKeyword', '')
  }
}
</script>

<style lang="scss" scoped>
.container {
  height: auto;
  overflow-x: hidden;
}
.deny {
  cursor: not-allowed !important;
}
.search-form {
  width: 70vw;
  margin: 0px auto 20px auto;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 30px 0px 20px 0px;

  .search-tab {
    margin: 0px 20%;

    .search-input {
      display: flex;
      .diff-font {
          display: inline-block;
          margin-left: 10px;
      }

      .diff-match {
          display: inline-block;
          margin-left: 6%;
      }
    }

    .check-books {
      display: flex;
    }

    .keyword-line {
      margin-bottom: 0px;
    }

    .optional {
      display: flex;
      margin-bottom: 10px;
    }

    @media (max-width: 1240px) {
      .check-books {
        display: block;
      }
    }
    .first-check {
      display: inline-block;
      margin-right: 20px;
    }
  }

  .search-button {
    width: 120px;
    margin-right: 35px;
  }

  .button-flex {
    line-height: 80px;
    position: relative;
    margin-left: -50px;
    // margin-top: -20px;
  }

  .output-button {
    position: absolute;
    top: 12px;
    margin-left: -25px;
    cursor: pointer;
    color: #409EFF;
    &:hover {
      color: red;
      text-decoration: underline;
    }
  }

  .output-diff {
    position: absolute;
    top: 12px;
    margin-left: 50px;
    cursor: pointer;
    color: #409EFF;
    &:hover {
      color: red;
      text-decoration: underline;
    }
  }
}

// ::v-deep .el-input--suffix {
//   width: 30vw;
// }

::v-deep .el-input__inner {
  display: inline-block;
  width: 30vw;
}

::v-deep .el-input {
  width: 30vw;
}

::v-deep .el-checkbox {
  margin-right: 20px;
}

</style>
