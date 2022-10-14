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
      <!-- <span
        class="output-button"
        @click="chooseAll"
        v-if="isEdit"
      >全选</span> -->
      <span
        class="output-button"
        @change="handleCheckAllChange"
        @click="toggleEdit"
      >{{isEdit ? '退出选择模式' : '进入选择模式'}}</span>
      <span
        class="output-button-right"
        @click="outputAll"
      >
      导出全部结果</span>
      <el-button
        type="text"
        class="output-button-el"
        :disabled="!ready || !isEdit"
        @click="output"
      >
      导出结果</el-button>
    </div>
    <div class="content">
      <div v-for="(item, index) in content" :key="index" class="outputs" >
        <div class="item-flex">
          <div class="item-flex-no" v-if="isEdit">
            <el-checkbox
              @change="handleCheckChange(index)"
            >{{index + 1}}</el-checkbox>
          </div>
          <div class="item-flex-left">
            <span style="float: left; fontWeight: 700" v-if="disIndexLeft.includes(index)">全文：</span>
            <div
              class="out-content"
              v-html="item.difffrom"
              :class="{ iscollapse: !disIndexLeft.includes(index) }"
            ></div>
            <div class="out-footer">
              <span class="origin">来源:《{{item.bookfrom}}》</span>
              <span class="read-all" v-if="!disIndexLeft.includes(index)" @click="changeLeftDis(index)">阅读全文</span>
              <span class="read-all" v-if="disIndexLeft.includes(index)" @click="removeLeftDis(index)">收起</span>
            </div>
          </div>
          <div class="item-flex-right">
            <span style="float: left; fontWeight: 700" v-if="disIndexRight.includes(index)">全文：</span>
            <div
              class="out-content"
              v-html="item.diffto"
              :class="{ iscollapse: !disIndexRight.includes(index) }"
            ></div>
            <div class="out-footer">
              <span class="origin">来源:《{{item.bookto}}》</span>
              <span class="read-all" v-if="!disIndexRight.includes(index)" @click="changeRightDis(index)">阅读全文</span>
              <span class="read-all" v-if="disIndexRight.includes(index)" @click="removeRightDis(index)">收起</span>
            </div>
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
  props: ['content', 'totalItems', 'outcomesLoading', 'search'],
  data () {
    return {
      pageSize: 20, // 每页显示的条数
      currentPage: 1, // 当前页数
      outputLoading: false,
      disIndexLeft: [],
      disIndexRight: [],
      checkedItems: [],
      testIndex: -1,
      isEdit: false,
      ready: false
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
  watch: {
    search (val) {
      // 清除选择模式
      this.isEdit = false
      this.checkedItems = []
      this.ready = false
    }
  },
  methods: {
    handleSizeChange (val) {
      this.pageSize = val
      this.$emit('changePageSize', this.pageSize)
    },
    handleCurrentChange (val) {
      this.currentPage = val
      this.$emit('changeCurrentPage', this.currentPage)
    },
    toggleEdit () {
      if (this.isEdit) {
        this.$confirm('退出编辑界面将不会保存当前选中元素，是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.isEdit = false
          this.ready = false
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消退出'
          })
        })
      } else {
        this.isEdit = true
      }
    },
    handleCheckChange (index) {
      if (!this.checkedItems.includes(index)) {
        console.log('push')
        this.checkedItems.push(index)
      } else {
        console.log('remove')
        this.checkedItems.splice(this.checkedItems.indexOf(index), 1)
      }
      // 导出结果按钮的样式
      if (this.checkedItems.length === 0) {
        this.ready = false
      } else {
        this.ready = true
      }
    },
    handleCheckAllChange (val) {
      const lenght = this.content.length
      const arr = []
      for (let i = 0; i < lenght; i++) {
        arr.push(i)
      }
      this.checkedItems = val ? arr : []
      this.checkAll = val
    },
    async output () {
      const arr = []
      try {
        this.checkedItems.forEach((e, index) => {
          const item = []
          item.push(index + 1)
          item.push(this.content[e].bookfrom)
          item.push(this.content[e].chapterfrom)
          item.push(this.content[e].difffrom.replace(/<\/?.+?>/g, '').replace(/ /g, ''))
          item.push(this.content[e].diffto.replace(/<\/?.+?>/g, '').replace(/ /g, ''))
          item.push(this.content[e].bookto)
          item.push(this.content[e].chapterto)
          item.push(this.content[e].jsonfrom)
          item.push(this.content[e].jsonto)
          arr.push(item)
        })
        const fd = new FormData()
        fd.append('list', JSON.stringify(arr))
        // console.log('diff', JSON.parse(JSON.stringify(arr)))
        const res = await this.$axios({
          url: '/Search/Export',
          method: 'POST',
          data: fd,
          responseType: 'blob'
        })
        if (res.status === 200) {
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(new Blob([res.data]))
          link.target = '_blank'
          // 文件名和格式
          link.download = `${this.checkedItems.length}条异文搜索结果.docx`
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
      } catch (error) {
        console.log('output error', error)
      }
    },
    async outputAll () {
      const arr = []
      try {
        this.content.forEach((e, index) => {
          const item = []
          item.push(index + 1)
          item.push(e.bookfrom)
          item.push(e.chapterfrom)
          item.push(e.difffrom.replace(/<\/?.+?>/g, '').replace(/ /g, ''))
          item.push(e.diffto.replace(/<\/?.+?>/g, '').replace(/ /g, ''))
          item.push(e.bookto)
          item.push(e.chapterto)
          item.push(e.jsonfrom)
          item.push(e.jsonto)
          arr.push(item)
        })
        const fd = new FormData()
        fd.append('list', JSON.stringify(arr))
        const res = await this.$axios({
          url: '/Search/Export',
          method: 'POST',
          data: fd,
          responseType: 'blob'
        })
        if (res.status === 200) {
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(new Blob([res.data]))
          link.target = '_blank'
          // 文件名和格式
          link.download = '全部异文搜索结果.docx'
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
      } catch (error) {
        console.log('output error', error)
      }
    },
    // left
    changeLeftDis (index) {
      this.disIndexLeft.push(index)
    },
    removeLeftDis (index) {
      for (let i = 0; i < this.disIndexLeft.length; i++) {
        if (this.disIndexLeft[i] === index) {
          this.disIndexLeft.splice(i, 1)
          break
        }
      }
    },
    // right
    changeRightDis (index) {
      this.disIndexRight.push(index)
    },
    removeRightDis (index) {
      for (let i = 0; i < this.disIndexRight.length; i++) {
        if (this.disIndexRight[i] === index) {
          this.disIndexRight.splice(i, 1)
          break
        }
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
    left: 30vw;
  }
}

.header {
    margin: 10px 13vw 0 13vw;
    border-bottom: 1px solid #333;
    line-height: 24px;
    text-align: left;
    position: relative;

    .output-button {
        color: #409EFF;
        position: absolute;
        top: -34px;
        left: 15px;
        cursor: pointer;
        &:hover {
            color: red;
        }
    }

    .output-button-right {
        color: #409EFF;
        position: absolute;
        top: -34px;
        right: 42px;
        cursor: pointer;
        &:hover {
            color: red;
        }
    }

    .output-button-el {
        position: absolute;
        top: -43px;
        right: 160px;
    }
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

        .item-flex {
            margin-top: 25px;
            display: flex;
            position: relative;

            .item-flex-no {
                position: absolute;
                left: 0px;
            }

            .item-flex-left {
                width: 45%;
                margin-right: 25px;
                margin-left: 50px;
            }

            .item-flex-right {
                width: 45%
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
.bottom-info {
    margin-top: 40px;
}

::v-deep .el-button {
    font-size: 16px;
}
</style>
