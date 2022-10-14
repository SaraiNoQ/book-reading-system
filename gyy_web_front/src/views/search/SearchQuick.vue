<template>
  <div>
    <div class="header">
      <span>
        找到包含“{{keyword}}”的搜索结果{{ nums }}条
      </span>
    </div>
    <div class="content">
      <div v-for="(item, index) in content" :key="index" class="outputs">
        <!-- <div class="info"></div><span style="float: left; fontWeight: 700;">全文：</span> -->
        <div>
          <span
            style="float: left; fontweight: 700"
            v-if="disIndex.includes(index)"
            >全文：</span
          >
          <div
            class="out-content"
            v-html="item.content"
            @click="toChapter(item, index)"
            :class="{
              deny: !cursor,
              allow: cursor,
              iscollapse: !disIndex.includes(index)
            }"
          ></div>
          <div class="out-footer">
            <span class="origin">来源:《{{ item.bookname }}》</span>
            <span
              class="read-all"
              v-if="!disIndex.includes(index)"
              @click="changeDis(index)"
              >阅读全文</span
            >
            <span
              class="read-all"
              v-if="disIndex.includes(index)"
              @click="removeDis(index)"
              >收起</span
            >
          </div>
        </div>
      </div>
    </div>

    <div class="bottom-info">
      <span
        >北京中医药大学XXXX团队&nbsp; &nbsp;&nbsp; All Rights Reserved
        版权所有&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
        &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
        技术支持：成都信息工程大学软件工程学院</span
      >
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      catalogArr: [],
      cursor: true,
      disIndex: [],
      nums: this.$store.state.searchNums,
      keyword: this.$store.state.searchKeyword,
      content: this.$store.state.searchContent
    }
  },
  computed: {
    numsCom () {
      return this.$store.state.searchNums
    },
    keywordCom () {
      return this.$store.state.searchKeyword
    },
    contentCom () {
      return this.$store.state.searchContent
    }
  },
  watch: {
    numsCom (val) {
      this.nums = val
    },
    keywordCom (val) {
      this.keyword = val
    },
    contentCom (val) {
      this.content = val
    }
  },
  methods: {
    toChapter (val, index) {
      if (this.catalogArr.length === 0) {
        this.cursor = false
        this.$axios
          .get('/bookChapter/getChapter/' + val.bookname)
          .then((resp) => {
            console.log('arrResp', resp)
            const cataArr = resp.data ? resp.data[0].cataLog : resp[0].cataLog
            if (cataArr == null) {
              this.$message({ type: 'error', message: '连接失败' })
              return
            }
            const arrIdChapter = []
            cataArr.forEach((element) => {
              arrIdChapter.push({ id: element.id, chapter: element.chapter })
              if (element.cataLog.length) {
                element.cataLog.forEach((children) => {
                  arrIdChapter.push({
                    id: children.id,
                    chapter: children.chapter
                  })
                })
              }
            })
            this.catalogArr = arrIdChapter
            let flag = 0
            for (let i = 0; i < arrIdChapter.length; i++) {
              if (arrIdChapter[i].id === val.chapter) {
                flag++
                this.$store.commit('setCurrentChapter', arrIdChapter[i].chapter)
                this.$store.commit('setChapterId', arrIdChapter[i].id)
                this.$store.commit('setCatalogArr', arrIdChapter)
                window.sessionStorage.setItem(
                  'store',
                  JSON.stringify(this.$store.state)
                )
                this.$router.push('/chapter')
                break
              }
            }
            if (flag === 0) {
              this.$message({ type: 'warning', message: '原章节丢失' })
            }
            this.cursor = true
          })
      } else {
        this.cursor = false
        let flag = 0
        for (let i = 0; i < this.catalogArr.length; i++) {
          if (this.catalogArr[i].id === val.chapter) {
            flag++
            this.$store.commit('setCurrentChapter', this.catalogArr[i].chapter)
            this.$store.commit('setChapterId', this.catalogArr[i].id)
            this.$store.commit('setCatalogArr', this.catalogArr)
            window.sessionStorage.setItem(
              'store',
              JSON.stringify(this.$store.state)
            )
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
    }
  },
  beforeDestroy () {
    // 清空vuex中数据
    this.$store.commit('setSearchNums', 0)
    this.$store.commit('setSearchKeyword', '')
    this.$store.commit('setSearchContent', [])
  }
}
</script>

<style lang="scss" scoped>
.header {
  margin: 12px 13vw 0 13vw;
  border-bottom: 1px solid #333;
  line-height: 24px;
  text-align: left;
}

.content {
  margin: 0px 13vw 0px 13vw;
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

    .out-footer {
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
  &:hover {
    cursor: not-allowed;
  }
}
.allow {
  &:hover {
    cursor: pointer;
  }
}
</style>
