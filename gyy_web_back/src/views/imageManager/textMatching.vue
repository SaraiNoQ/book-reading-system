<template>
<div>
    <el-container>
      <el-header style="background-color: #f4cf98; padding-top: 10px">
        <el-button icon="el-icon-refresh" circle @click="chapterGet" title="刷新目录"></el-button>
        <!-- <el-button type="primary" icon="el-icon-upload2" circle></el-button> -->
      </el-header>
      <el-container>
        <el-aside width="300px" style="overflow: auto">
          <!-- 目录树 -->
          <el-tree
            v-loading="loading"
            :data="chapterData"
            :props="defaultProps"
            class="tree"
            highlight-current
            @node-click="handleNodeClick"
            ></el-tree>
        </el-aside>
        <el-main style="background-color: white; height: calc(100vh - 200px)" v-loading="tableLoading">
          <!-- 匹配悬浮框 -->
          <el-dialog
            title="原文预览" 
            :visible.sync="dialogVisible"
            width="65%"
          >
            <div class="match">
              <div class="body">
                {{this.displayCon}}
              </div>
              <div class="dis">
                <div style="width: 30%">
                  <h3 class="head">本章节原文图片:</h3>
                  <div class="match-img">
                    <el-checkbox-group v-model="checkedImgs" @change="handleChecked">
                      <el-checkbox v-for="(img, index) in imgLists" :label="img.id" :key="img.id+'put'+index">
                        {{img.imgname}}</el-checkbox>
                    </el-checkbox-group>
                  </div>
                </div>
                <div class="pre-img">
                  <div class="move">
                    <el-button type="primary" class="el-icon-d-arrow-left" @click="removeChange"></el-button>
                    <el-button type="primary" class="el-icon-d-arrow-right" @click="putChange"></el-button></div>
                  <el-carousel trigger="click" style="width: 250px; height: 300px" :autoplay="false">
                    <el-carousel-item v-for="item in carousels" :key="item">
                      <el-image
                        style="width: 100%; height: 100%"
                        :src="imgUrlPrefix + item"></el-image>
                    </el-carousel-item>
                  </el-carousel>
                </div>
                <div style="width: 30%">
                  <div class="button-head">
                    <h3 class="head">当前匹配原文:</h3>
                    <div class="head-button">
                      <el-switch
                        v-model="value"
                        active-color="#13ce66"
                        inactive-color="#ff4949"
                        @change="valueChange"
                        active-text="全选"
                        inactive-text="全不选">
                      </el-switch>
                    </div>
                  </div>

                  <div class="finish-img">
                    <el-checkbox-group v-model="checkedImgsFinish" @change="handleCheckedFinish">
                      <el-checkbox v-for="(img, index) in imgListsFinish" :label="img.id" :key="img.id+'get'+index">
                        {{img.imgname}}</el-checkbox>
                    </el-checkbox-group>
                  </div>
                </div>
              </div>
            </div>
            
            <span slot="footer" class="dialog-footer">
              <el-button @click="dialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="match" :loading="matching">匹配</el-button>
            </span>
          </el-dialog>

          <!-- 预览图 -->
          <el-dialog title="原图预览" :visible.sync="previewImgsVisible" width="45%">
            <div class="preview-img">
              <el-image
                v-for="(img, index) in previewImgs"
                :key="index"
                style="paddingBottom: 10px"
                :src="img"
                :preview-src-list="previewImgs"></el-image>
            </div>
              <span slot="footer" class="dialog-footer">
                <el-button @click="previewImgsVisible = false">取 消</el-button>
                <el-button type="primary">确 定</el-button>
              </span>
          </el-dialog>


          <!-- 内容搜索框 -->
          <!-- <el-card class="box-card">
            <el-form  :model="contentForm" class="chapform" style="">
            <el-form-item label="内容">
              <el-input v-model="contentForm.content" style="width: 250px;" placeholder="请输入内容"></el-input>
            </el-form-item>
            </el-form>
          </el-card> -->
          <!-- 原文内容列表 -->
          <el-table
              :data="urlData"
              style="width: 100%;"
              highlight-current-row
              @current-change="tableHandleCurrentChange">
             <!-- :header-cell-style="tableHeaderColor"-->

            <el-table-column
              label="序号"
              align="center"
              width="50"
              height="60"
              header-align="center"
              type="index"
            >
              <template slot-scope="scope">
                {{ (scope.$index + 1) + (currentPage - 1) * size }}
              </template>
            </el-table-column>
            <el-table-column
              prop="sub"
              label="内容"
              height="60"
              align="center"
              header-align="center"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="imgnum"
              label="图片数量"
              width="90"
              height="60"
              align="center"
              header-align="center"
            >
            </el-table-column>
            <el-table-column
              prop="other"
              label="操作"
              height="60"
              width="210"
              align="center"
              header-align="center"
            >
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="primary"
                  @click="handlePreview(scope.$index, scope.row)"
                  >预览</el-button>
                <el-button
                  size="mini"
                  type="primary"
                  @click="handleMatch(scope.$index, scope.row)"
                  >匹配</el-button>
                <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <!-- <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="currentPage"
              :page-sizes="[ 5, 10, 15]"
              :page-size="size"
              layout="total, sizes, prev, pager, next, jumper"
              :total="total">
          </el-pagination> -->
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import httpAPI from '../../utis/port.js'
export default {
  name: "textMatching",
  data () {
    return {
      chapterData: [],
      defaultProps: {
        children: 'cataLog',
        label: 'chapter'
      },
      imageForm:{
        imgName:'',
        imgUrl:''
      },
      contentForm:{
        content: ''
      },
      demoData:[],
      loading: false,
      urlData:[],
      size:5,
      currentPage:1,
      total:1,
      dialogVisible: false,
      imgLists: [],
      checkedImgs: [],
      checkedImgsFinish: [],
      imgListsFinish: [],
      matching: false,
      // dialog展示的原文段落内容
      displayCon: '',
      // 原文匹配时需要的sequence
      clickSequence: '',
      clickId: '',
      value: false,
      tableLoading: false,
      // 获取到的所有段落图片信息
      allImg: {},
      previewImgs: [],
      previewImgsVisible: false,
      imgUrlPrefix: ''
    }
  },
  computed: {
    carousels () {
      const list = []
      this.imgLists.forEach(e => {
        this.checkedImgs.forEach(e1 => {
          if (e1 === e.id) {
            list.push(e.imgurl)
          }
        })
      })
      console.log('carousel', list);
      return list
    }
  },
  created () {
    this.imgUrlPrefix = httpAPI()
  },
  mounted () {
    this.chapterGet()
  },
  methods: {
    chapterGet() {
      // 初始化设置
      this.chapterData = []
      this.demoData = []
      this.loading = true
      this.$axios.get('/chapter/getChapter').then(resp=>{
        if (resp) {
          const bookData = resp.data ? resp.data : resp
          bookData.forEach(e => {
            e.forEach(e1 => {
              this.demoData.push(e1)
            })
          })
          this.chapterData = this.demoData
          // console.log('chapterData', this.chapterData)
          // for(var key in bookData)
          // {
          // let op={
          //     chapter:'',
          //     cataLog:[]
          // }
          // op.chapter=key;
          // op.cataLog=resp[key];
          // //console.log(op);
          // this.demoData.push(op);
          // }
          // this.demoData.forEach(e => {
          //   // e 为某一本书的全部内容
          //   const chapterData = e.chapter
          //   const idReg = /id\=.*?\,/
          //   const booknameReg = /bookname\=.*?\,/
          //   // 获得到bookId
          //   const obj = {}
          //   obj.bookId = idReg.exec(chapterData)[0].replace(',', '').slice(3)
          //   obj.chapter = booknameReg.exec(chapterData)[0].replace(',', '').slice(9)
          //   obj.cataLog = e.cataLog
          //   this.chapterData.push(obj)
          // })
        }
        this.loading = false
      })
    },
    handleMatch(e,scope){
      console.log('e, scope', e, scope);
      const scopeData = this.allImg[e]
      console.log('imgall', scopeData);
      // 清空imgListsFinish和check列表(finish列表可以获取)
      this.imgListsFinish = []
      this.checkedImgsFinish = []
      if (scopeData) {
        scopeData.data.forEach(e => {
          const obj = {
            id: e.inId,
            imgurl: e.imgurl,
            imgname: e.imgname
          }
          this.imgListsFinish.push(obj)
        })
      }
      this.checkedImgs = []
      
      this.dialogVisible = true
      this.displayCon = scope.sub
      this.clickSequence = e
      this.$axios.post('/original/getImgUrl/' + this.$store.state.currentChapterId).then(resp => {
        // resp 为图片数组
        if (resp !== 'error') {
          console.log('rrr', resp);
          this.imgLists = resp
        }
      })
    },
    handlePreview(e,scope){
      const scopeData = this.allImg[e]
      if (scopeData == null) {
        this.$message.warning('该段没有匹配图片')
        return
      }
      if (scopeData.data.length > 0) {
        this.previewImgs = []
        scopeData.data.forEach(e => {
          this.previewImgs.push(this.imgUrlPrefix + e.imgurl)
        })
        this.previewImgsVisible = true
      }
    },
    handleDelete (e, scope) {
      this.$confirm('此操作将删除该段匹配图片, 是否删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const chapId = this.$store.state.currentChapterId
        const formData = new FormData()
        formData.append('chapterId', chapId)
        formData.append('sequence', e)
        this.$axios.post('/original/delteMatch', formData).then(resp => {
          return resp
        }).then((resp)=>{
            if(resp ==='success'){
              this.$message({
                type: 'success',
                message: '删除成功!'
              })
              this.refreshDis(this.clickId)
              // 清空预览和数量
              this.urlData[e].imgnum = 0
              this.previewImgs = []
              this.imgListsFinish = []
            }else
            {
              this.$message({
                type:'error',
                message: '删除失败!'
              })
            }
          })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    refreshDis (id) {
      let demoData = [];
      this.tableLoading = true
      const formData = new FormData()
      formData.append('chapterId', id)
      this.$axios.post('/original/getMatch', formData).then(resp => {
        this.allImg = resp.data ? resp.data : resp
        return this.allImg
      }).then(d => {
        this.$axios.post('/book/bookContent?chapterId=' + id).then(resp=>{
          const respData = resp[0] ? resp[0] : 'error'
          this.urlData = []
          // 当响应体中content不为空时
          if (respData !== 'error') {
            // 设置当前章节的id，预览时使用
            this.$store.commit('setChapterId', id)
            let infoArray = respData.content.split('\r\n');
            // console.log('array', infoArray);
            // let index=0;
            this.total = infoArray.length;
            for (const i in infoArray) {
              if (infoArray[i] === '') {
                continue
              }
              let op = {
                sub: '',
                type: '',
                sequence: 0,
                imgnum: 0
              }
              op.sub = infoArray[i];
              op.type = '内容';
              op.sequence = i;
              op.imgnum = d[i] ? d[i].count : 0
              demoData.push(op)
            }
            this.urlData = demoData;
            console.log('urlData', this.urlData);
          } else {
            this.$message({
              type: 'warning',
              message: '本章节原文缺失',
              duration: 1200
            })
          }
          this.tableLoading = false
        })
      })
    },
    handleNodeClick(data) {
      let id = data.id;
      if (id) {
        this.clickId = id
        // this.tableLoading = true
        // const formData = new FormData()
        // formData.append('chapterId', id)
        // this.$axios.post('/original/getMatch', formData).then(resp => {
        //   this.allImg = resp.data ? resp.data : resp
        //   return this.allImg
        // }).then(d => {
        //   getRequest('/book/bookContent/'+ id).then(resp=>{
        //     const respData = resp[0] ? resp[0] : 'error'
        //     // 当响应体中content不为空时
        //     if (respData !== 'error') {
        //       // 设置当前章节的id，预览时使用
        //       this.$store.commit('setChapterId', data.id)
        //       let infoArray = respData.content.split('\r\n');
        //       // console.log('array', infoArray);
        //       // let index=0;
        //       this.total = infoArray.length;
        //       for (const i in infoArray) {
        //         let op = {
        //           sub: '',
        //           type: '',
        //           sequence: 0,
        //           imgnum: 0
        //         }
        //         op.sub = infoArray[i];
        //         op.type = '内容';
        //         op.sequence = i;
        //         op.imgnum = d[i] ? d[i].count : 0
        //         demoData.push(op)
        //       }
        //       this.urlData=demoData;
        //       console.log('urlData', this.urlData);
        //     } else {
        //       this.$message.warning('该章没有内容!')
        //     }
        //     this.tableLoading = false
        //   })
        // })
        this.refreshDis(this.clickId)
      }
    },
    handleSizeChange:function(size){
      this.size=size;
    },
    handleCurrentChange:function(currentPage){
      this.currentPage=currentPage;
    },
    handleChecked () {
      console.log('change', this.checkedImgs);
    },
    handleCheckedFinish () {
      console.log('change', this.checkedImgsFinish);
    },
    // 对象数组去重
    unique (arr1) {
      const res = new Map();
      return arr1.filter((a) => !res.has(a.id) && res.set(a.id, 1))
    },
    putChange () {
      // 初始化value
      this.value = false
      this.checkedImgsFinish = []
      this.checkedImgs.forEach(e => {
        this.imgLists.forEach(e1 => {
          if (e === e1.id && this.imgListsFinish.indexOf(e1) === -1) {
            this.imgListsFinish.push(e1)
          }
        })
      })
      this.imgListsFinish = this.unique(this.imgListsFinish)
      console.log('imgsss', this.imgListsFinish);
      this.checkedImgs = []
    },
    removeChange() {
      this.checkedImgsFinish.forEach(e => {
        for (let i = 0; i < this.imgListsFinish.length; i++) {
          if (this.imgListsFinish[i].id === e) {
            this.imgListsFinish.splice(i, 1)
          }
        }
      })
      this.checkedImgsFinish = []
    },
    match () {
      if (this.checkedImgsFinish.length > 0) {
        this.matching = true
        const id = this.$store.state.currentChapterId

        const formData = new FormData()
        formData.append('chapterId', id)
        formData.append('content', this.displayCon)
        console.log('matching', this.checkedImgsFinish);
        for (let i in this.checkedImgsFinish) {
          formData.append('imgId', this.checkedImgsFinish[i])
        }
        formData.append('sequence', this.clickSequence)
        this.$axios.post('/original/addMathch', formData).then(resp => {
          if (resp === 'success') {
            this.$message.success('匹配成功!')
            this.refreshDis(this.clickId)
            this.dialogVisible = false
            this.clickSequence = ''
          } else {
            this.$message.error('匹配失败!')
          }
          this.matching = false
        })
      } else {
        this.$message.error('请选择待匹配的图片!')
      }
    },
    // 点击列表元素
    tableHandleCurrentChange(scope) {
      console.log('scope', scope)
    },
    valueChange(val) {
      if (val) {
        this.checkedImgsFinish = []
        this.imgListsFinish.forEach(e => {
          this.checkedImgsFinish.push(e.id)
        })
      } else {
        this.checkedImgsFinish = []
      }
    }
  }
}
</script>

<style scoped lang="scss">
.el-aside {
  margin-top: 20px;
  background-color: white;
  color: #333;
  text-align: center;
  .tree {
    height: calc(100vh - 205px);
    font-size: 15px;
    ::v-deep .el-tree-node__label {
      overflow: hidden;
      text-overflow:ellipsis;
      white-space: nowrap;
    }
  }
}

.chapform {
    ::v-deep .el-form-item {
        margin-bottom: 0px;
    }
    ::v-deep .el-form-item__label {
        font: 16px;
    }
    margin-left: calc((100vw - 580px)/2 - 200px);
}

::v-deep .el-dialog__body {
  padding: 5px 20px 30px 20px;
}

.preview-img {
  margin: 0 auto 20px auto;
  width: 80%;
  max-height: 450px;
  overflow-y: auto;
}



.match {
  .body {
    font-size: 18px;
    margin: 3px 20px 10px 20px;
    text-indent: 1.5em;
  }
  .dis { 
    display: flex;
    justify-content: space-around;
    .head {
      margin-top: 0px;
      margin-bottom: 10px;
    }

    .match-img {
      width: 100%;
      height: 400px;
      overflow-y: scroll;
      border: 1px solid rgb(80, 80, 80);
      padding: 3px;
    }
    
    .pre-img {
      max-width: 30%;
      .icon {
        display: block;
        text-align: center;
        font-size: 50px;
        font-weight: 700;
        color: rgb(244, 207, 152);
        margin-top: 20px;
        margin-bottom: 15px;
      }
      .move {
        display: flex;
        justify-content: center;
        margin-top: 70px;
        margin-bottom: 15px;
      }
    }

    .button-head {
      display: flex;
      align-items: flex-start;
      align-content: center;
      justify-content: space-between;
    }

    .finish-img {
      width: 100%;
      height: 400px;
      overflow-y: scroll;
      border: 1px solid rgb(80, 80, 80);
      padding: 3px;
    }
  }

  ::v-deep .el-checkbox {
    display: block;
  }
}
</style>