<template>
  <div>
    <div class="search-inner">
      <el-input
        v-model="searchValue"
        placeholder="请输入搜索内容"
        clearable
        @keyup.enter.native="getSearch"
        :autofocus="true"
      ></el-input>
      <el-button
        type="primary"
        icon="el-icon-search"
        @click="getSearch"
        :loading="searchLoading"
      ></el-button>
    </div>
    <div class="search-output" v-if="showOutput">
      <div
        class="item"
        v-for="(item, index) in searchData"
        :key="index"
        v-html="item.content.replaceAll('\\r\\n', '</br>')"
        @click="toChapter(item)"
      ></div>
    </div>
    <div v-show="showNo" class="search-output">
      <div class="item" style="cursor: default">暂无相关记录！</div>
    </div>
  </div>
</template>

<script>
import Axios from '../../../utils/api'

export default {
  data () {
    return {
      searchValue: '',
      searchLoading: false,
      searchData: [], // {content: '', id: ''}
      showOutput: false,
      showNo: false
    }
  },
  methods: {
    replaceBr (str) {
      return str.replaceAll('\\r\\n', '</br>')
    },
    async getSearch () {
      this.searchLoading = true
      try {
        const fd = new FormData()
        fd.append('isExport', '否')
        fd.append('searchType', '不分')
        fd.append('bookName', ['ALL'])
        fd.append('page', 1)
        fd.append('size', 10)
        fd.append('type', ['ALL'])
        fd.append('keyword', this.searchValue)
        const res = await Axios.post('/Search/content', fd)
        if (res.status === 200) {
          const resData = res.data
          if (resData.length > 0) {
            // const arr = []
            // resData.forEach(item => {
            //   item.content = this.replaceBr(item.content)
            //   arr.push(item)
            // })
            this.searchData = [...resData]
            this.showNo = false
            this.showOutput = true
          } else {
            this.searchData = []
            this.showNo = true
            this.showOutput = false
          }
          console.log(this.searchData)
        }
      } catch (error) {
        console.log(error)
      } finally {
        this.searchLoading = false
      }
    },
    toChapter (val) {
      Axios.post(`/bookChapter/getChapter?bookName=${val.bookname}`)
        .then(resp => {
          try {
            const cataArr = resp.data ? resp.data[0].cataLog : resp[0].cataLog
            if (cataArr == null) {
              this.$message({ type: 'error', message: '连接失败' })
              return
            }
            const arrIdChapter = []
            cataArr.forEach(element => {
              arrIdChapter.push({ id: element.id, chapter: element.chapter })
              if (element.cataLog.length) {
                element.cataLog.forEach(children => {
                  arrIdChapter.push({ id: children.id, chapter: children.chapter })
                })
              }
            })
            let flag = 0
            for (let i = 0; i < arrIdChapter.length; i++) {
              if (arrIdChapter[i].id === val.ts_book_chapter_id) {
                flag++
                this.$store.commit('setCurrentChapter', arrIdChapter[i].chapter)
                this.$store.commit('setChapterId', arrIdChapter[i].id)
                this.$store.commit('setCatalogArr', cataArr)
                window.sessionStorage.setItem('store', JSON.stringify(this.$store.state))
                this.$router.push('/chapter')
                break
              }
            }
            if (flag === 0) {
              this.$message({ type: 'warning', message: '原章节丢失！' })
            }
          } catch (error) {
            this.$message({ type: 'error', message: '原书籍丢失！' })
          } finally {
            this.$router.go(0)
          }
        })
    }
  }
}
</script>

<style lang="scss" scoped>
$borderColor: #f4cf98;

::v-deep .el-input__inner {
  border-radius: 0px;

  &:focus {
    border-color: $borderColor;
  }
}

::v-deep .el-button {
  border-radius: 0px;
  height: 41px;
  z-index: 1000;
  border-color: $borderColor;
  background-color: $borderColor;
}

.search-inner {
  display: flex;
  flex-wrap: nowrap;
  align-items: center;
  gap: 0px;
}

.search-output {
  margin: 0px 0px 0px 11px;
  max-height: 400px;
  overflow-y: auto;
  overflow-x: hidden;

  .item {
    width: 96%;
    padding-left: 15px;
    padding-top: 8px;
    padding-bottom: 4px;
    padding-right: 10px;
    margin-top: -5px;
    border-bottom: 1.5px solid rgba(0,0,0, 0.4);
    background-color: white;
    font-size: 16px;
    line-height: 24px;
    letter-spacing: 0.5px;
    font-weight: 500;
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
      "serif";
      cursor: pointer;
      word-wrap: break-word;
      display: -webkit-inline-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 2;
      overflow: hidden;

      &:last-child {
        border-bottom: 3px solid white;
      }
  }
}
</style>
