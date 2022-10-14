<template>
  <div class="container">
    <div class="search-form">
      <el-form
        label-position="left"
        label-width="80px"
        :model="searchForm"
        class="search-tab"
        @keydown.enter.native="search"
      >
      <el-form-item label="关键字" class="keyword-line">
        <div class="search-input">
          <el-input v-model="searchForm.key" placeholder="请输入关键字"></el-input>
        </div>
      </el-form-item>
      <el-form-item class="optional">
        <el-checkbox v-model="checkedMatch">模 糊 匹 配</el-checkbox>
        <el-checkbox v-model="checkedLimited">限 制 来 源</el-checkbox>
      </el-form-item>

      <el-form-item label="来 源">
          <div class="search-input">
            <el-select
              v-model="choosedFrom"
              placeholder="勾选限制来源启用此功能"
              clearable
              :disabled="!checkedLimited"
            >
              <el-option class="check"
                v-for="(item, index) in bookArr"
                :key="index"
                :label="item"
                :value="item"
              ></el-option>
            </el-select>
            <el-select
              v-model="choosedTo"
              placeholder="书籍"
              clearable
              :disabled="!checkedLimited"
            >
              <el-option class="check"
                v-for="(item, index) in bookArr"
                :key="index"
                :label="item"
                :value="item"
              ></el-option>
            </el-select>
          </div>
      </el-form-item>
      </el-form>
      <div class="button-flex">
        <el-button class="search-button" type="primary" @click="search" :loading="searchLoading">🔍 搜 索</el-button>
        <el-button class="search-button" type="primary" @click="resetForm">取 消</el-button>
      </div>
    </div>

    <diff-outcomes
      :content="content"
      :totalItems="totalItems"
      :outcomesLoading="outcomesLoading"
      :search="comSearch"
      @changePageSize="changePageSize"
      @changeCurrentPage="changeCurrentPage"
      v-if="showOutcomes"
    ></diff-outcomes>

    <div><el-backtop target=".el-main"></el-backtop></div>
  </div>
</template>

<script>
import Axios from '../../utils/api'
import DiffOutcomes from './components/DiffOutcomes.vue'
export default {
  name: 'ConnectDiff',
  components: {
    DiffOutcomes
  },
  data () {
    return {
      searchForm: {
        key: ''
      },
      checkedMatch: false,
      checkedLimited: false,
      searchLoading: false,
      outputLoading: false,
      choosedFrom: '',
      choosedTo: '',
      bookArr: ['备急千金要方', '孙真人千金方'],
      content: [],
      totalItems: 0,
      pageSize: 20,
      currentPage: 1,
      showOutcomes: false,
      outcomesLoading: false,
      outputAll: false,
      comSearch: false
    }
  },
  methods: {
    async search () {
      this.searchLoading = true
      if (this.searchForm.key === '') {
        this.$message({
          message: '请输入要搜索的关键字！',
          type: 'warning'
        })
        return null
      }
      const fd = new FormData()
      if (this.checkedLimited) {
        fd.append('bookFrom', this.choosedFrom)
        fd.append('bookTo', this.choosedTo)
      }
      fd.append('keyword', this.searchForm.key)
      fd.append('page', this.currentPage)
      fd.append('size', this.pageSize)
      fd.append('searchType', this.checkedMatch ? '分词' : '不分')
      try {
        const res = await Axios.post('/Search/diff', fd)
        if (res.status === 200) {
          const resData = res.data
          this.content = []
          this.comSearch = !this.comSearch
          // 没有搜索结果，不展示搜索结果
          if (res.data.length === 0) {
            this.$message.warning('没有搜索到相关结果！')
            this.showOutcomes = false
            return
          }
          this.totalItems = resData[0].total
          resData.forEach(e => {
            this.content.push(e)
          })
        }
        this.showOutcomes = true // 展示搜索结果
        console.log('resData', this.content)
      } catch (error) {
        console.log('search error', error)
      } finally {
        this.searchLoading = false // 将按钮loading设为默认值
      }
    },
    resetForm () {
      this.$router.go(-1)
    },
    chooseAll () {
      if (!this.showOutcomes || this.content.length === 0) {
        this.$message({
          message: '没有搜索结果可供导出！',
          type: 'warning'
        })
        return
      }
      this.outputAll = true
    },
    async changePageSize (val) {
      this.outcomesLoading = true
      // 修改pageSize的值
      this.pageSize = val
      const formData = new FormData()
      formData.append('searchType', this.checkedMatch ? '分词' : '不分')
      if (this.checkedLimited) {
        formData.append('bookFrom', this.choosedFrom)
        formData.append('bookTo', this.choosedTo)
      }
      formData.append('keyword', this.searchForm.key)
      formData.append('page', this.currentPage)
      formData.append('size', this.pageSize)
      try {
        const res = await Axios.post('/Search/diff', formData)
        if (res.status === 200) {
          const resData = res.data
          this.content = []
          // 没有搜索结果，不展示搜索结果
          if (res.data.length === 0) {
            this.$message.warning('没有搜索到相关结果！')
            this.showOutcomes = false
            return
          }
          this.totalItems = resData[0].total
          resData.forEach(e => {
            this.content.push(e)
          })
        }
        this.showOutcomes = true // 展示搜索结果
      } catch (error) {
        console.log('search error', error)
      } finally {
        this.outcomesLoading = false
      }
    },
    async changeCurrentPage (val) {
      // 设置组件loading状态
      this.outcomesLoading = true
      this.currentPage = val
      const formData = new FormData()
      formData.append('searchType', this.checkedMatch ? '分词' : '不分')
      if (this.checkedLimited) {
        formData.append('bookFrom', this.choosedFrom)
        formData.append('bookTo', this.choosedTo)
      }
      formData.append('keyword', this.searchForm.key)
      formData.append('page', this.currentPage)
      formData.append('size', this.pageSize)
      try {
        const res = await Axios.post('/Search/diff', formData)
        if (res.status === 200) {
          const resData = res.data
          this.content = []
          // 没有搜索结果，不展示搜索结果
          if (res.data.length === 0) {
            this.$message.warning('没有搜索到相关结果！')
            this.showOutcomes = false
            return
          }
          this.totalItems = resData[0].total
          resData.forEach(e => {
            this.content.push(e)
          })
        }
        this.showOutcomes = true // 展示搜索结果
      } catch (error) {
        console.log('search error', error)
      } finally {
        this.outcomesLoading = false
      }
    },
    changeOutputLoading (outputLoading) {
      this.outputLoading = outputLoading
    }
  },
  async mounted () {
    const res = await Axios.get('/book/getBookInfo/1/100')
    const resData = res.data.data ? res.data.data : res.data
    if (resData) {
      this.bookArr = []
      resData.forEach(e => {
        this.bookArr.push(e.bookname)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.container {
  height: auto;
  overflow-x: hidden;
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

  .edit-button {
    position: absolute;
    top: 12px;
    margin-left: 55px;
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

::v-deep .el-input {
  width: 30vw;
}

::v-deep .el-input--suffix {
    width: 90%;
}

</style>
