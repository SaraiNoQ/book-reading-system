<template>
  <div v-loading="loading">
    <div class="pagination">
      <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page.sync="currentPage"
        :page-sizes="[20, 50, 100, 200]"
        :page-size="pageSize"
        layout="sizes, prev, pager, next"
        :total="totalitems">
      </el-pagination>
    </div>
    <div class="box"></div>
    <div class="header">
      <span>
        找到{{outcomes.bookname}}中包含“{{outcomes.keyword}}”,类别“
        <span v-for="(item, index) in outcomes.category" :key="index">
          {{ item }}
        </span>”的搜索结果{{outcomes.nums}}条
      </span>
    </div>
    <div class="content">
      <div v-for="(item, index) in content" :key="index" class="outputs">
        <!-- <div class="info"></div><span style="float: left; fontWeight: 700;">全文：</span> -->
        <div>
          <span style="float: left; fontWeight: 700" v-if="disIndex.includes(index)">全文：</span>
          <div
            class="out-content"
            v-html="handleBr(item.content)"
            @click="toChapter(item, index)"
            :class="{ deny: !cursor, allow: cursor, iscollapse: !disIndex.includes(index)  }"
          ></div>
          <div class="out-footer">
            <span class="origin">来源:《{{item.bookname}}》</span>
            <span class="read-all" v-if="!disIndex.includes(index)" @click="changeDis(index)">阅读全文</span>
            <span class="read-all" v-if="disIndex.includes(index)" @click="removeDis(index)">收起</span>
            <span class="read-all" style="marginRight: 20px;" @click="outputItemDiff(item)">关联异文</span>
          </div>
        </div>
      </div>
    </div>

    <div class="bottom-info">
        <span>北京中医药大学XXXX团队&nbsp; &nbsp;&nbsp; All Rights Reserved 版权所有&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 技术支持：成都信息工程大学软件工程学院</span>
    </div>
  </div>
</template>

<script>
export default {
  props: ['outcomes', 'content', 'totalItems', 'outcomesLoading'],
  data () {
    return {
      catalogArr: [],
      cursor: true,
      disIndex: [],
      bookArr: [], // 所有书籍的名称
      pageSize: 20, // 每页显示的条数
      currentPage: 1, // 当前页数
      outputLoading: false,
      changedContent: [] // 修改过后的内容
    }
  },
  computed: {
    totalitems () {
      return this.totalItems
    },
    loading () {
      return this.outcomesLoading
    }
  },
  created () {
  },
  methods: {
    toChapter (val, index) {
      // if (!this.disIndex.includes(index)) {
      //   this.changeDis(index)
      //   return
      // }
      if (this.catalogArr.length === 0) {
        this.cursor = false
        this.$axios.post(`/bookChapter/getChapter?bookName=${val.bookname}`)
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
              this.catalogArr = arrIdChapter
              // console.log('catalog', cataArr)
              let flag = 0
              for (let i = 0; i < arrIdChapter.length; i++) {
                if (arrIdChapter[i].id === val.chapter) {
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
            }
            this.cursor = true
          })
      } else {
        // console.log('test', this.catalogArr, val)
        this.cursor = false
        let flag = 0
        for (let i = 0; i < this.catalogArr.length; i++) {
          if (this.catalogArr[i].id === val.chapter) {
            flag++
            this.$store.commit('setCurrentChapter', this.catalogArr[i].chapter)
            this.$store.commit('setChapterId', this.catalogArr[i].id)
            // this.$store.commit('setCatalogArr', this.catalogArr)
            window.sessionStorage.setItem('store', JSON.stringify(this.$store.state))
            this.$router.push('/chapter')
            break
          }
        }
        if (flag === 0) {
          this.$message({ type: 'warning', message: '原章节丢失' })
        }
        this.cursor = true
      }
    },
    changeDis (index) {
      this.disIndex.push(index)
    },
    removeDis (index) {
      for (let i = 0; i < this.disIndex.length; i++) {
        if (this.disIndex[i] === index) {
          this.disIndex.splice(i, 1)
          break
        }
      }
    },
    async outputItemDiff (item) {
      this.bookArr = []
      this.$emit('changePageLoading', true)
      const res = await this.$axios.get('/book/getBookInfo/1/100')
      const resData = res.data.data ? res.data.data : res.data
      if (resData) {
        resData.forEach(e => {
          if (e.bookname !== item.bookname) {
            this.bookArr.push(e.bookname)
          }
        })
        const totalBook = []
        resData.forEach(e => {
          totalBook.push(e.bookname)
        })
        if (item.chapter) {
          if (!totalBook.includes(item.bookname)) {
            this.$message({ type: 'error', message: '该章节丢失！' })
            this.$emit('changePageLoading', false)
            return
          }
          this.bookArr.forEach(async e => {
            const formData = new FormData()
            formData.append('chapterId', item.chapter)
            formData.append('tarBook', e)
            const resp = await this.$axios.post('/content/diff/' + item.chapter, formData)
            const respData = resp.data || resp
            if (respData.length) {
              const res = await this.$axios.post('/diff/export', formData, { responseType: 'blob' })
              if (res.status === 200) {
                const link = document.createElement('a')
                link.href = window.URL.createObjectURL(new Blob([res.data]))
                link.target = '_blank'
                // 文件名和格式
                link.download = `${item.chapter}_${e}.docx`
                document.body.appendChild(link)
                link.click()
                document.body.removeChild(link)
                this.$message({ type: 'success', message: `《${e}》对应异文导出成功！` })
              }
            } else {
              this.$message({ type: 'warning', message: `《${e}》无对应异文！` })
            }
            this.$emit('changePageLoading', false)
          })
        }
      } else {
        this.$message({ type: 'error', message: '网络异常，暂无数据！' })
        this.$emit('changePageLoading', false)
      }
    },
    handleSizeChange (val) {
      this.pageSize = val
      this.$emit('changePageSize', this.pageSize)
    },
    handleCurrentChange (val) {
      this.currentPage = val
      this.$emit('changeCurrentPage', this.currentPage)
    },
    handleBr (val) {
      if (val.substring(0, 4) === '\\r\\n') {
        return val.substr(4).replaceAll('\\r\\n', '<br>') || val
      } else {
        return val.replaceAll('\\r\\n', '<br>') || val
      }
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-input__inner {
  width: 100px !important;
}

.box {
  display: block;
  height: 40px;
  margin: 0px;
  width: 100%;
}

.pagination {
  position: relative;
  line-height: 20px;
  margin: -20px auto;
  ::v-deep .el-pagination {
    display: inline-block;
    position: absolute;
    top: 10px;
    left: 35vw;
  }
}

.header {
    margin: 10px 13vw 0 13vw;
    border-bottom: 1px solid #333;
    line-height: 24px;
    text-align: left;
}

.content {
    margin: 0px 13vw 10px 13vw;
    padding: 10px 0px 5px 0px;
    border-bottom: 1px solid #333;
    .outputs {
        margin: 5px 0px 8px 0px;
        line-height: 24px;
        text-align: left;
        &:hover {
            color: rgb(100, 100, 100);
            .read-all {
                color: rgb(100, 100, 100);
            }
        }

        .out-content {
            word-wrap: break-word;
        }

        .iscollapse {
            word-wrap: break-word;
            display: -webkit-inline-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 2;
            overflow: hidden;
        }

        .out-footer{
            margin: 0px 5px 0px 0px;
            .origin {
                font-weight: 600;
            }
        }

        .read-all {
            float: right;
            color: #175199;
            &:hover {
                cursor: pointer;
                color: red;
            }
        }
    }
    .info {
        display: flex;
        justify-content: flex-start;

        .image {
            height: 172px;
            width: 120px;
        }

        .information {
            line-height: 30px;
            margin: 0px 0 0 10px;
            text-align: left;

            h3 {
                margin: 5px 0 0 0;
            }

            p {
                margin: 5px 0 0 0;
            }
        }

    }
}

.deny {
    &:hover { cursor: not-allowed; }
}
.allow {
    &:hover { cursor: pointer; }
}
</style>
