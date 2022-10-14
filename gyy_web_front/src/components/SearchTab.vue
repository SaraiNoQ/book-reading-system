<template>
  <div class="searchBox" v-show="show">
     <div class="searchBoxs">
        <el-input
          placeholder="请输入关键字"
          @keyup.enter.native="searchKeyup"
          v-model.trim="searchValue"
          class="input-with-select"
        >
        <el-button slot="append" icon="el-icon-close" @click="searchClose()"></el-button>
        </el-input>
        <el-button icon="el-icon-search" @click="search()" class="refreshBtn" :loading="quickSearchLoading"></el-button>
        <el-button class="refreshBtn" @click="toSuperSearch">高级搜索</el-button>
      </div>
  </div>
</template>

<script>
export default {
  props: ['show'],
  data () {
    return {
      nameValue: '',
      searchValue: '',
      quickSearchLoading: false
    }
  },
  methods: {
    isEntry () {
      if (!localStorage.getItem('token') && !sessionStorage.getItem('token')) {
        this.$message({
          type: 'error',
          message: '您还未登陆，请登录！',
          duration: 1200
        })
        return false
      } else {
        return true
      }
    },
    async search () {
      if (!this.isEntry()) {
        return
      }
      const filters = {}
      filters.name = this.nameValue // 'name' || 'id'
      filters.value = this.searchValue // 具体值
      if (filters.value === '') {
        this.$message.warning('查询条件不能为空！')
        return null
      } else {
        if (this.$route.path === '/search') {
          this.$store.commit('setSupterKeyword', filters.value)
          this.searchValue = ''
          this.$router.push('/supersearch')
          return
        }
        this.quickSearchLoading = true
        const formData = new FormData()
        formData.append('isExport', '否')
        formData.append('searchType', '不分')
        formData.append('keyword', filters.value)
        formData.append('bookName', ['ALL'])
        formData.append('page', 1)
        formData.append('size', 100)
        formData.append('type', ['ALL'])
        try {
          const resp = await this.$axios.post('/Search/content', formData)
          const respData = resp.data ? resp.data : resp
          let nums = 0
          const content = []
          respData.forEach(e => {
            const obj = {
              content: e.content,
              // 两个数据库中的字段不一样
              chapter: e.ts_book_chapter_id ? e.ts_book_chapter_id : e.chapterid,
              bookname: e.bookname
            }
            content.push(obj)
            nums++
          })
          this.quickSearchLoading = false
          this.$store.commit('setSearchContent', content)
          this.$store.commit('setSearchNums', nums)
          this.$store.commit('setSearchKeyword', filters.value)
          this.searchValue = ''
          this.$router.push('/search')
        } catch (error) {
          this.quickSearchLoading = false
        }
      }
    },
    async searchKeyup () {
      if (!this.isEntry()) {
        return
      }
      const filters = {}
      this.statusValue = ''
      filters.name = this.nameValue // 'name' || 'id'
      filters.value = this.searchValue // 具体值
      if (filters.value === '') {
        this.$message.warning('查询条件不能为空！')
        return null
      } else {
        if (this.$route.path === '/search') {
          this.$store.commit('setSupterKeyword', filters.value)
          this.searchValue = ''
          this.$router.push('/supersearch')
          return
        }

        this.quickSearchLoading = true
        const formData = new FormData()
        formData.append('isExport', '否')
        formData.append('searchType', '不分')
        formData.append('keyword', filters.value)
        formData.append('bookName', ['ALL'])
        formData.append('page', 1)
        formData.append('size', 100)
        formData.append('type', ['ALL'])
        const resp = await this.$axios.post('/Search/content', formData)
        // console.log('searchOutputs', resp)

        const respData = resp.data ? resp.data : resp
        let nums = 0
        const content = []
        respData.forEach(e => {
          const obj = {
            content: e.content,
            // 两个数据库中的字段不一样
            chapter: e.ts_book_chapter_id ? e.ts_book_chapter_id : e.chapterid,
            bookname: e.bookname
          }
          content.push(obj)
          nums++
        })
        this.quickSearchLoading = false
        this.$store.commit('setSearchContent', content)
        this.$store.commit('setSearchNums', nums)
        this.$store.commit('setSearchKeyword', filters.value)
        // console.log('searchOutputs', this.$store.state.searchKeyword)
        // console.log('searchOutputs', this.$store.state.searchNums)
        // console.log('searchOutputs', this.$store.state.searchContent)
        this.searchValue = ''
        this.$router.push('/search')
      }
    },
    searchClose () {
      this.searchValue = ''
    },
    toSuperSearch () {
      if (!this.isEntry()) {
        return
      }
      this.$router.push('/search/similar')
    }
  },
  computed: {
  },
  created () {
  },
  mounted () {
  },
  watch: {
  }
}
</script>

<style lang="scss" scoped="scoped">
.searchBox {
  .refreshBtn {
    vertical-align: middle;
    padding: 0;
    border: 0;
    width: 90px;
    height: 47px;
    margin-left: 10px;
    background: #3c72ca;
    color: #fff;
  }
  ::v-deep .el-select .el-input {
    width: 190px;
  }
  ::v-deep .el-select-dropdown {
    width: 270px !important;
  }
}
.btntext {
  font-size: 12px;
  line-height: 3;
}
.btnBox {
  text-align: left;
  button {
    font-size: 12px;
    padding: 8px 20px;
    color: #666666;
    background: #f0f2f7;
    border: 1px solid #f0f2f7;
    white-space: nowrap;
    cursor: pointer;
    text-align: center;
    box-sizing: border-box;
    outline: 0;
    margin: 0;
    font-weight: 500;
    border-radius: 0px;
    margin-right: 10px;
  }
  .active {
    border: 1px solid #56d6c4;
    background: #56d6c4;
    color: #ffffff;
  }
}
</style>
<style lang="scss">
.searchBox {
  .el-input__inner {
    height: 40px;
  }
  .searchBoxs {
    width: 500px;
    min-width: 380px;
    height: 48px;
    padding-right: 10px;
    text-align: right;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    .el-input-group > .el-input__inner {
      vertical-align: baseline;
    }
    .input-with-select {
      .el-select {
        width: 110px;
        .el-input {
          width: 110px;
          border: none;
        }
        .el-select__caret {
          color: #000;
        }
      }
      .el-input-group__append {
        background: #fff;
        border: none;

        .el-button {
          vertical-align: middle;
          padding: 0;
          width: 30px;
          height: 30px;
        }
      }
    }
    .el-input-group__prepend {
      background: #fff;
      border: none;
      position: relative;
      &::before {
        position: absolute;
        top: 8px;
        right: 0px;
        width: 1px;
        height: 16px;
        content: '';
        background: #bebebe;
      }
    }
    .input-with-select > .el-input__inner {
      width: 291px;
      border: none;
      background: #fff;
    }
    .el-select .el-input.is-focus .el-input__inner {
      border-color: transparent;
    }
  }
  .el-select {
    width: 150px;
    .el-input {
      width: 150px;
    }
  }
  .el-input {
    width: 400px;
    height: 45px;
    border: 0px solid #e5e5e5;
    box-sizing: border-box;
    &__inner {
      height: 45px;
      width: 200px;
    }
  }
}
#input-select-place {
  text-align: center;
  color: #000;
}
</style>
