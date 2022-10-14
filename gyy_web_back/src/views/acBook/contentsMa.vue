<template>
<div>
  <!-- 导入悬浮框 -->
  <el-dialog title="导入目录" :visible.sync="dialogFormVisible" v-loading="uploadLoading">
    <el-form :model="importForm">
      <el-form-item label="选择书籍" :label-width="formLabelWidth">
        <el-select v-model="chooseBook" placeholder="请选择导入书籍">
          <el-option v-for="(item, index) in importForm.bookArr" :key="index" :label="item" :value="item"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="目录文件" :label-width="formLabelWidth">
        <el-upload
          class="upload-demo"
          action=""
          ref="upload_excel"
          :auto-upload="false"
          :on-change="handleChange"
          :on-remove="handleRemove"
          :http-request="handleTestSuccess"
          :on-exceed="handleExceed"
          multiple
          :limit="1"
          :file-list="fileList">
          <el-button size="small" type="primary">点击上传</el-button>
          <span slot="tip" class="el-upload__tip tip">只能上传.xlsx格式文件</span>
        </el-upload>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisible = false">取 消</el-button>
      <el-button type="primary" @click="uploadExcel">上 传</el-button>
    </div>
  </el-dialog>

  <el-container>
    <el-header class="headstyle">
      <div class="import-parent">
        <el-row>
          <el-button icon="el-icon-refresh" circle @click="chapterRefesh" title="刷新目录"></el-button>
          <el-button icon="el-icon-delete-solid" circle @click="chapterDelete" title="删除章节" type="danger"></el-button>
          <el-button icon="el-icon-document-add" circle @click="dialogTottle" title="添加章节" type="primary"></el-button>
          <el-button icon="el-icon-edit-outline" circle @click="chapterUpdata" title="更新章节" type="success"></el-button>
        </el-row>
        <div class="import">
          <el-button icon="el-icon-upload2" circle @click="importChapter" title="导入章节"></el-button>
        </div>
      </div>
    </el-header>
    <el-container>
      <el-aside width="300px" style="overflow: auto">
        <el-tree
          class="tree"
          v-loading="loading"
          :data="data"
          :props="defaultProps"
          @node-click="handleNodeClick"
          highlight-current></el-tree>
      </el-aside>
      <el-main class="chapter-info">
        <el-dialog title="新增目录" :visible.sync="dialogTableVisible" class="dialog-header" width="45%">
          <el-form ref="chapterInfo" :model="dialogData" label-width="80px">
            <el-form-item label="父章节" prop="pname">
              <el-input v-model="dialogData.pname" placeholder="" :disabled="true"></el-input>
            </el-form-item>
            <el-form-item label="名称" prop="chapterName" >
              <el-input v-model="dialogData.chapterName" placeholder=""></el-input>
            </el-form-item>
            <el-form-item label="序列" prop="sequence" >
              <el-input v-model="dialogData.sequence" placeholder=""></el-input>
            </el-form-item>
            <el-form-item label="级别"  prop="grade" >
              <el-input v-model="dialogData.grade" placeholder="" :disabled="true"></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogTableVisible = false">取 消</el-button>
            <el-button type="primary" @click="chapterAdd()">新 增</el-button>
          </div>
        </el-dialog>
        <h4>章节详情
        </h4>
        <el-form ref="chapterInfo" :model="chapterInfo" label-width="80px">
          <el-form-item label="章节ID" prop="pid">
            <el-input v-model="chapterInfo.pid" placeholder="" :disabled="true"></el-input>
          </el-form-item>
            <el-form-item label="章节名称" prop="chapterName" >
              <el-input v-model="chapterInfo.chapterName" placeholder="" ></el-input>
            </el-form-item>
            <el-form-item label="序列" prop="sequence" >
              <el-input v-model="chapterInfo.sequence" placeholder="" ></el-input>
            </el-form-item>
            <el-form-item label="级别"  prop="grade">
              <el-input v-model="chapterInfo.grade" placeholder="" :disabled="true"></el-input>
            </el-form-item>
          <el-form-item label="类型" prop="type" >
            <el-select v-model="chapterInfo.type" placeholder="" style="float: left" :disabled="true">
              <el-option
                  v-for="item in typeList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
              >
              </el-option>
            </el-select>
            <!-- <el-select v-model="chapterInfo.itype" placeholder="" style="">
              <el-option
                  v-for="item in itypeList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
              >
              </el-option>
            </el-select> -->
          </el-form-item>
        </el-form>
      </el-main>
    </el-container>
  </el-container>
</div>
</template>

<script>
import XLSX from 'xlsx'
export default {
  name: "contentsMa",
  data() {
    return {
      data: [],
      demoData:[],
      defaultProps: {
        children: 'cataLog',
        label: 'chapter'
      },
      chapterInfo:{
        id:'',
        pid:'',
        chapterName:'',
        sequence:'',
        grade:'',
        type:'',
        itype: '方剂',
      },
      typeList:[
        {
          value: '卷',
          label: '卷'
        },
        {
          value: '节',
          label: '节'
        },
        {
          value: '章',
          label:'章'
        },
        {
          value: '书',
          label: '书'
        }
      ],
      itypeList:[
        {
          value: '方剂',
          label: '方剂'
        },
        {
          value: '注',
          label: '注'
        },
        {
          value: '医论',
          label:'医论'
        },
        {
          value: '针灸',
          label: '针灸'
        },
        {
          value: '其他',
          label: '其他'
        }
      ],
      loading: false,
      clickBookId: '',
      // 递归返回值
      returnVal: '',
      dialogTableVisible: false,
      dialogData: {
        pid: '',
        pname: '',
        grade: '',
        chapterName: '',
        sequence: ''
      },
      dialogFormVisible: false,
      formLabelWidth: '120px',
      importForm: {
        bookArr: []
      },
      chooseBook: '',
      fileList: [],
      uploadFile: '',
      // 点击的节点信息
      clickNode: {},
      // 写入excel数据时
      uploadLoading: false
    }
  },
  methods: {
    handleNodeClick(data) {
      this.clickNode = data
      console.log('ckick', data);
      // 设置增加章节的信息
      this.dialogData.pid = data.id ? data.id : data.cataLog[0].id.slice(0,3)
      this.dialogData.grade = data.grade ? data.grade + 1 : 1
      this.dialogData.chapterName = ''
      this.dialogData.sequence = ''
      this.dialogData.pname = data.chapter

      this.chapterInfo.chapterName = data.chapter
      this.chapterInfo.sequence = data.sequence
      // 点击事件触发获取bookId
      if (data.bookId) {
        this.clickBookId = data.bookId
      }
      if (data.id == null) {
        this.chapterInfo.id = data.cataLog[0].id.slice(0,3)
        // console.log('!', data.cataLog[0].id.slice(0,2));
      } else {
        this.chapterInfo.id=data.id;
      }
      if(data.grade == null) // 选择书名时
      {
        this.chapterInfo.grade = 0
        this.chapterInfo.type='书'
      }
      else if(data.grade===1)
      {
        this.chapterInfo.grade = data.grade
        this.chapterInfo.type='卷'
      } else if (data.grade === 2) {
        this.chapterInfo.grade = data.grade
        this.chapterInfo.type='章'
      } else {
        this.chapterInfo.grade = data.grade
      }
      if(this.chapterInfo.id) { 
        this.chapterInfo.pid = this.chapterInfo.id
      }
      // this.data.forEach(e => {
      //   if(e.id !== data.id && e.cataLog.length > 0) {
      //     e.cataLog.forEach(e1 => {

      //     })
      //   }
      // })
      // 递归遍历 this.data 通过点击id获取到parentId
      this.returnVal = ''
      const dataLoop = (dataArr) => {
        for (let i = 0; i < dataArr.length; i++) {
          if (dataArr[i].id === data.id) {
            this.returnVal = dataArr[i].parentid
            return dataArr[i].parentid
          } else {
            if (dataArr[i].cataLog != null && dataArr[i].cataLog.length > 0) {
              dataLoop(dataArr[i].cataLog)
            }
          }
        }
      }
      dataLoop(this.data)
    },
    // treeToArr (data, ) {
    //   this.data.forEach(e => {
    //     if (e.cataLog != null && e.cataLog.length > 0) {
    //       this.treeToArr(e.cataLog)
    //     }
    //   })
    // },
    chapterGet() {
      // 初始化设置
      this.data = []
      this.demoData = []
      this.loading = true
      this.$axios.get('/chapter/getChapter').then(resp=>{
        // console.log('chapter!', resp);
        if (resp) {
          // const cha = localStorage.getItem('contentdata');
          const bookData = resp.data ? resp.data : resp
          bookData.forEach(e => {
            e.forEach(e1 => {
              this.demoData.push(e1)
            })
          })
          this.data = this.demoData
          
          // for (var key in bookData) {
          //   let op = {
          //     chapter:'',
          //     cataLog:[]
          //   }
          //   op.chapter = key;
          //   op.cataLog = resp[key];
          //   console.log('op', op);
          //   this.demoData.push(op);
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
          //   console.log('obj', obj);
          //   this.data.push(obj)
          // })
          // console.log('thisData', this.data);
        }
        this.loading = false
      })
    },
    chapterAdd () {
      // console.log('add', this.dialogData);
      // console.log('bookId', this.clickBookId);
      this.$confirm('此操作将添加章节, 是否添加?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const bookId = this.clickBookId;
        const parentId = this.dialogData.pid;
        const chapterName = this.dialogData.chapterName;
        const sequence = this.dialogData.sequence;
        const grade = this.dialogData.grade;
        // console.log('test', parentId);

        const formData = new FormData();
        formData.append('bookId', bookId);
        formData.append('parentId', parentId);
        formData.append('chapterName', chapterName);
        formData.append('sequence', sequence);
        formData.append('grade', grade);
        this.$axios.post('/chapter/add', formData).then((resp)=>{
          // console.log(resp);
          if(resp ==='success'){
            this.$message({
              type: 'success',
              message: '添加成功!'
            })
            this.chapterGet()
            // 关闭悬浮框
            this.dialogTableVisible = false
          }else
          {
            this.$message({
              type:'error',
              message: '添加失败!'
            })
          }
        })
      }).catch((err) => {
        console.log(err)
        this.$message({
          type: 'info',
          message: '已取消添加'
        });
      });
    },
    chapterDelete () {
      try {
        if (this.clickNode.grade === 0) {
          this.$message({
            type: 'error',
            message: '不能删除根目录！'
          })
          return
        }
      } catch (error) {
        this.$message({
          type: 'error',
          message: '请选择要删除的章节！'
        })
        return
      }
      
      this.$confirm('此操作将删除章节, 是否删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const id= this.chapterInfo.id;
        const formData = new FormData();
        formData.append('id', id);
        this.$axios.post('/chapter/delete', formData).then((resp)=>{
          console.log(resp);
          if(resp ==='success'){
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            this.chapterGet()
          }else
          {
            this.$message({
              type:'error',
              message: '删除失败!'
            })
          }
        })
      }).catch((err) => {
        console.log(err)
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    chapterUpdata(){
      // console.log('update', this.chapterInfo);
      // console.log('digui', this.returnVal);
      try {
        if (this.clickNode.grade === 0) {
          this.$message({
            type: 'error',
            message: '不能修改根目录！'
          })
          return
        }
      } catch (error) {
        this.$message({
          type: 'error',
          message: '请选择要修改的章节！'
        })
        return
      }
      this.$confirm('此操作将更新章节信息, 是否更新?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const id = this.chapterInfo.id;
        // pid需要进行修改，去掉id后三位
        const parentId = this.returnVal;
        const chapterName = this.chapterInfo.chapterName;
        const sequence = this.chapterInfo.sequence;
        const grade = this.chapterInfo.grade;
        const formData = new FormData();
        formData.append('id', id);
        formData.append('parentId', parentId);
        formData.append('chapterName', chapterName);
        formData.append('sequence', sequence);
        formData.append('grade', grade);
        this.$axios.post('/chapter/update', formData).then((resp)=>{
          if(resp ==='success'){
            this.$message({
              type: 'success',
              message: '更新成功!'
            })
            // this.chapterGet()
          }else
          {
            this.$message({
              type:'error',
              message: '更新失败!'
            })
          }
        })
      }).catch((err) => {
        console.log(err)
        this.$message({
          type: 'info',
          message: '已取消更新'
        });
      });
    },
    chapterRefesh() {
      this.chapterGet()
    },
    dialogTottle () {
      this.dialogTableVisible = true
    },
    async importChapter() {
      const res = await this.$axios.post('/code/Books')
      this.importForm.bookArr = res
      this.dialogFormVisible = true
    },
    handleChange (file, fileList) {
      console.log('file', file, fileList, this.fileList, this.uploadFile)
      if (file.raw.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet') {
        this.uploadFile = file
      } else {
        this.$message({
          type: 'error',
          message: '文件类型错误，请上传xlsx格式的文件！'
        })
        this.$refs.upload_excel.clearFiles()
      }
    },
    handleTestSuccess (file) {
      console.log('handle', file)
    },
    handleRemove() {
      this.uploadFile = ''
    },
    async uploadExcel() {
      if (this.uploadFile == '') {
        this.$message({
          type: 'error',
          message: '请上传excel文件！'
        })
        return
      }

      if (this.chooseBook == '') {
        this.$message({
          type: 'error',
          message: '请选择对应导入的书籍！'
        })
        return
      }

      const fd = new FormData()
      const config = {
        headers: { "Content-Type": "multipart/form-data" }, //跟后端约定发送的数据类型为form-data对象类型
      }
      const reader = new FileReader()
      // obj转base64
      reader.readAsDataURL(this.uploadFile.raw)
      // reader.readAsBinaryString(this.uploadFile.raw)
      reader.onload = async (ev) => {
        const f = ev.target.result
        // base64编码转成二进制
        var arr = f.split(','), mime = arr[0].match(/:(.*?);/)[1],
        bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
        while(n--){
          u8arr[n] = bstr.charCodeAt(n);
        }
        // application/vnd.ms-excel
        const file = new File([u8arr], 'aaa.xlsx', { type:'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        fd.append('filename', file)
        fd.append('bookName', this.chooseBook)
        this.uploadLoading = true
        const res = await this.$axios({
          method: 'post',
          url: '/chapter/import',
          data: fd,
          headers: config.headers
        })
        // console.log('uploadOutcome', res.substring(0, 4))
        if (res.substring(0, 4) === '成功添加') {
          this.$message({
            message: res,
            type: 'success'
          })
          this.chapterRefesh() // 刷新目录树
          this.dialogFormVisible = false
          this.chooseBook = ''
          this.$refs.upload_excel.clearFiles()
        } else {
          this.$message({
            message: res,
            type: 'error'
          })
          this.dialogFormVisible = false
        }
        this.uploadLoading = false
      }
    },
    // 只允许有一张照片
    handleExceed (files) {
      this.$refs.upload_excel.clearFiles()
      const file = files[0]
      this.$refs.upload_excel.handleStart(file)
    },
  },
  async created() {
    this.chapterGet();
    const res = await this.$axios.post('/code/Books')
    this.importForm.bookArr = res
  }
}
</script>

<style scoped lang="scss">
.el-header, .el-footer {
  background-color: #B3C0D1;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.el-aside {
  background-color: white;
  color: #333;
  text-align: center;
  
  .tree {
    margin-top: 5px;
    height: calc(100vh - 205px);
    font-size: 15px;
    ::v-deep .el-tree-node__label {
      overflow: hidden;
      text-overflow:ellipsis;
      white-space: nowrap;
    }
  }
}

.el-main {
  background-color: #E9EEF3;
  color: #333;
  text-align: center;
  line-height: 160px;
  h4 {
    line-height: 20px;
  }

  ::v-deep .el-dialog__header {
    line-height: 30px;
  }
  ::v-deep .el-dialog__footer {
    line-height: 30px;
  }
}
.headstyle{
  text-align:left;
  background-color: #f4cf98;
}

.chapter-info {
  padding: 20px 100px;
  height: calc(100vh - 180px);
}

.import-parent {
  position: relative;
  .import {
    position: absolute;
    right: 10px;
    top: 0px;
  }
}

.tip {
  display: inline-block;
  margin: 0 0 0 20px;
  line-height: 25px;
  vertical-align: bottom;
}
</style>