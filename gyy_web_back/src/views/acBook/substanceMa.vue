<template>
  <div>
    <!-- 导入悬浮框 -->
    <el-dialog
      title="导入正文内容"
      :visible.sync="dialogFormVisible"
      v-loading="uploadLoading"
      width="39%"
    >
      <el-form :model="importForm">
        <el-form-item label="选择书籍" :label-width="formLabelWidth">
          <el-select v-model="chooseBook" placeholder="请选择导入书籍">
            <el-option
              v-for="(item, index) in importForm.bookArr"
              :key="index"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
          <el-button type="primary" style="marginLeft: 25px;" size="mini" @click="formatFile" :disabled="formatDisabled">目录大纲</el-button>
        </el-form-item>
        <el-form-item label="内容文件" :label-width="formLabelWidth">
          <el-upload
            action=""
            ref="upload_excel"
            :auto-upload="false"
            :on-change="handleChange"
            :on-remove="handleRemove"
            :http-request="handleTestSuccess"
            :on-exceed="handleExceed"
            multiple
            :limit="1">
            <el-button size="small" type="primary">点击上传</el-button>
            <span slot="tip" class="el-upload__tip tip">只能上传.xlsx格式文件，请将excel中换行符全部替换成\r\n</span>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="uploadExcel">上 传</el-button>
      </div>
    </el-dialog>

    <el-container>
      <el-header class="el-header">
        <div class="import-parent">
          <el-button icon="el-icon-refresh" circle @click="chapterGet" title="刷新目录"></el-button>
          <el-button icon="el-icon-delete-solid" circle @click="textDelet()" title="删除内容" type="danger"></el-button>
          <el-button icon="el-icon-plus" circle @click="textAdd()" title="添加内容" type="primary"></el-button>
          <el-button icon="el-icon-edit-outline" circle @click="textUpdate()" title="修改内容" type="success"></el-button>
          <div class="import">
            <el-button icon="el-icon-upload2" circle @click="dialogFormVisible = true" title="导入正文内容"></el-button>
          </div>
        </div>
      </el-header>

      <el-container>
        <el-aside width="300px" style="overflow: auto">
          <el-tree class="tree" v-loading="loading" :data="chapterdata" :props="defaultProps" @node-click="handleNodeClick"></el-tree>
        </el-aside>
        <el-main class="content-info" v-loading="mainLoading">
          <el-form ref="chapterInfo" :model="chapterInfo"  label-width="80px" >
            <el-form-item label="内容"  prop="content" >
              <el-input v-model="chapterInfo.content" placeholder="" type="textarea" :rows="8"></el-input>
            </el-form-item >
            <el-row :gutter="200">
              <el-col :span="6">
                <el-form-item label="序列"  prop="sequence" >
                  <el-input style="width: 200px" v-model="chapterInfo.sequence" placeholder=""></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="类型"  prop="type">
                  <el-select  v-model="chapterInfo.type" placeholder="" style="float: left;width: 200px">
                    <el-option
                        v-for="item in chapterType"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>

            </el-row>


          </el-form>


           <div>
            <el-table
              :data="subData.slice((currentPage-1)*size,currentPage*size)"
              style="width: 100%"
              @row-click="rowGet"
              :row-style="{ height:'50px' }"
              :cell-style="{ padding:'10px 0 10px 0' }"
              highlight-current-row
            >
              <el-table-column
                label="序号"
                align="center"
                width="50"
                header-align="center"
              >
                <template slot-scope="scope">
                  {{ (scope.$index+1)+(currentPage-1)*size }}
                </template>
              </el-table-column>
              <el-table-column
                prop="sub"
                label="内容"
                align="left"
                header-align="center">
              </el-table-column>
              <el-table-column
                prop="type"
                label="类型"
                align="center"
                width="170"
                header-align="center">
                <template slot-scope="scope">
                  <el-select
                    v-model="subData[scope.$index + (currentPage - 1) * size].type"
                    style="float: center; width: 160px"
                    multiple placeholder="内容"
                  >
                    <el-option
                      v-for="item in typeList"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column
                label="操作"
                align="center"
                width="90"
              >
                <template slot-scope="scope">
                <!-- <el-button
                  size="mini"
                  type="primary"
                  @click="editLabel(scope.$index)">编辑</el-button> -->
                  <el-button
                  size="mini"
                  type="success"
                  @click="saveLabel(scope.$index)">保存</el-button>
                </template>
              </el-table-column>

            </el-table>
             <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="currentPage"
                :page-sizes="[ 5, 10, 15]"
                :page-size="size"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total">
             </el-pagination>
          </div>
        </el-main>
      </el-container>
    </el-container>

    <!-- 添加-->


    <!--删除-->


    <!--更新-->



  </div>
</template>

<script>
// import Label from './components/label.vue'

export default {
  name: "substanceMa",
  data() {
    return {
     changeid:'',
      changData:'',
      chapterdata:[],
      demoData:[],
      typeList:[
        {
          value:'医论',
          label:'医论'
        },
        {
          value:'方剂',
          label:'方剂'
        },
        {
          value: '针灸',
          label: '针灸'
        },
        {
          value: '注',
          label: '注'
        },
        {
          value: '其他疗法',
          label: '其他'
        }
      ],
      chapterType: [
        {
          value: '内容',
          label: '内容'
        },
        {
          value: '章节',
          label: '章节'
        },
        {
          value: '典籍',
          label: '典籍'
        }
      ],
      chapterInfo:{
        name:'',
        chapterId:'',
        content:'',
        sequence:'',
        type:'',
        contentId:'',
      },
      paragraphType: '',
      defaultProps: {
        children: 'cataLog',
        label: 'chapter'
      },
      subData:[],
      total:1,
      size:10,
      currentPage:1,
      formLabelWidth: '80px',
      addForm:{
        name:'',
        chapterId:'',
        content:'',
        sequence:1,
        type:'1'
      },
      dialogAddFormVisible:false,
      dialogDeleteVisible:false,
      dialogUpdataVisible:false,
      loading: false,
      mainLoading: false,
      contentSplit: [],
      // label标记
      labelDialogVisible: false,
      chooseLabelFrom: '',
      chooseId: '',
      dialogFormVisible: false, // 导入对话框
      uploadLoading: false, // 导入loading
      importForm: {
        bookArr: []
      },
      chooseBook: '',
      formatDisabled: true, // 格式化按钮禁用
      spiltRowLabel: '\\f'
    }
  },
  watch: {
    chooseBook (val) {
      this.formatDisabled = false
    }
  },
  methods:{
    async splitByRow (str) {
      const strs = str[0].sub
      // \r\r\n为批量导入正文时的换行符
      // \\r\\n在前端会显示为\r\n，\r\n在前端会展示为换行符
      // \\r\\n是后台用于标识换行符的
      let arr = []
      /*
      if (strs.indexOf('\\r\\n') !== -1) {
        // arr = strs.replaceAll(/[\r\r\n]/g, '\r\n').replaceAll(/[\\r\\n]/g, '').split('\r\n')
        arr = strs.split('\\r\\n')
      } else {
        arr = strs.split('\\r\\n')
      }
      */
      arr = strs.split('\\r\\n')
      // 清除文本框中最后一行空白
      if (arr[arr.length - 1] === '') {
        arr.pop()
      }
      let obj = []
      const formData = new FormData()
      formData.append('chapterId', this.chapterInfo.chapterId)
      const res = await this.$axios.post('/content/category/get', formData)
      const respData = res.data ? res.data : res

      for (let i = 0; i < arr.length; i++) {
        obj.push({
            // 清除表格内每行的内容中的\\r\\n，防止标签匹配出错
            sub: arr[i].replaceAll(/[\\r\\n]/g, ''),
            sequence: i,
            type: []
          })
      }
      try {
        for (let i = 0; i < respData.length; i++) {
          for (let j = 0; j < obj.length; j++) {
            if (respData[i].sequence === obj[j].sequence) {
              // console.log('labelType', respData[i].label.split(',').filter(item => item !== ''));
              obj[j].type = respData[i].label.split(',').filter(item => item !== '')
            }
          }
        }
      } catch (error) {
        console.log('error')
      }
      this.total = arr.length
      this.contentSplit = obj
      return obj
    },
    handleNodeClick(data) {
      // this.subData = []
      this.changData = data
      this.chapterInfo.name = data.chapter
      this.chapterInfo.chapterId = "" + data.id
      this.chapterInfo.sequence = data.sequence
      let id = data.id
      let demoData = []
      // console.log('clickNode', data, this.chapterInfo)
      // 判断内容的层级
      if (data.grade == '1') {
        this.chapterInfo.type = '章节'
      } else if(data.grade=='2') {
        this.chapterInfo.type = '内容'
      } else {
        this.chapterInfo.type = '典籍'
      }
      // 获取内容id
      if (id) {
        this.mainLoading = true
        this.$axios.post('/book/bookContent?chapterId=' + id).then(async resp => {
          this.chapterInfo.contentId = '' // 初始化contentId
          this.total = 0 // 初始化分页条
          this.currentPage = 1 // 初始化
          if (resp.length >= 1) {
            let op = {
              sub: '',
              type: '',
              sequence:0
            }
            op.sub = resp[0].content
            op.sequence = resp[0].sequence
            demoData.push(op)
            this.chapterInfo.contentId = resp[0].id
            try {
              console.log('before', demoData)
              this.subData = await this.splitByRow(demoData)
              // console.log('thisSub', this.subData)
              // 将所有\\r\\n替换为空字符串
              // this.chapterInfo.content = resp[0].content.replaceAll(/[\\r\\n]/g, '')
              this.chapterInfo.content = resp[0].content
              this.mainLoading = false
            } catch (error) {
              console.error(error)
            }
          } else {
            this.chapterInfo.content = ''
            this.subData = []
            this.mainLoading = false
          }
        })
      }
    },
    chapterGet() {
      // 初始化设置
      this.chapterdata = []
      this.demoData = []
      this.loading = true
      this.$axios.get('/chapter/getChapter').then(resp=>{
        // console.log('chapter', resp);
        if (resp) {
          const bookData = resp.data ? resp.data : resp
          bookData.forEach(e => {
            e.forEach(e1 => {
              this.demoData.push(e1)
            })
          })
          this.chapterdata = this.demoData
          // console.log(this.data);
          // const cha=localStorage.getItem('contentdata');

          // let bookData=JSON.parse(JSON.stringify(resp))
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
          //   this.chapterdata.push(obj)
          // })
        }
        this.loading = false
      })
    },
    handleSizeChange:function(size){
      this.size=size;
    },
    handleCurrentChange:function(currentPage){
      this.currentPage=currentPage;
    },
    rowGet(val){
      // let thisRowData=this;
      // thisRowData=val;
      // console.log(val);
      //console.log(thisRowData);
      // this.chapterInfo.content=thisRowData.sub;
    },
    textRefresh() {
      let demoData=[];
      // console.log('this', this.changData);
      this.$axios.post('/book/bookContent', {
          chapterId: this.changData.id
        }).then(resp=>{
        if (resp.length === 0) {
          this.chapterInfo.content = ''
          this.subData = []
        } else {
          let op ={
            sub:'',
            type:'',
            sequence:0
          }
          op.type='内容'
          op.sub=resp[0].content;
          op.sequence=resp[0].sequence;
          demoData.push(op);
          this.chapterInfo.contentId=resp[0].id;
          this.subData=demoData;
          this.chapterInfo.content=resp[0].content;
        } 
      })
    },
    textAdd(){
      // if (this.contentSplit.length > 0) {
      //   this.$message({
      //     message: '对已有内容请使用编辑功能',
      //     type: 'warning',
      //     duration: '900'
      //   });
      //   return
      // }
      this.$confirm('此操作将添加该章节内容, 是否添加?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // let data = {
        //   'chapterId': this.addForm.chapterId,
        //   'content': this.addForm.content,
        //   'sequence':this.addForm.sequence,
        //   'type':this.addForm.type
        // }
        // const chapterId = this.chapterInfo.chapterId;
        // const content = this.chapterInfo.content;
        // const sequence = this.chapterInfo.sequence;
        const formData = new FormData()
        formData.append('chapterId', this.chapterInfo.chapterId)
        formData.append('content', this.chapterInfo.content)
        formData.append('sequence', this.chapterInfo.sequence)
        formData.append('type', this.chapterInfo.type)

        this.$axios.post('/content/add', formData).then((resp)=>{
          if(resp === 'success') {
            this.$message({
              type: 'success',
              message: '添加成功!'
            })
            // this.textRefresh()
            this.handleNodeClick(this.changData);
          } else if (resp === '请删除后重新添加') {
            this.$message({
              type: 'warning',
              message: '添加失败！已有内容时请使用修改功能！'
            })
          } else {
            this.$message({
              type: 'error',
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
    textDelet() {
      this.$confirm('此操作将永久删除该章节内容, 是否删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const contentId = this.chapterInfo.contentId;
        const formData = new FormData()
        formData.append('contentId', contentId)
        this.$axios.post('/content/delete', formData).then((resp)=>{
          console.log(resp);
          if(resp ==='success'){
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            // this.textRefresh()
            this.handleNodeClick(this.changData)
          } else {
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
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    textUpdate(){
      this.$confirm('此操作将更新该章节内容, 是否更新?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // const contentId = this.chapterInfo.contentId;
        // const sequence = this.chapterInfo.sequence;
        // const content = this.chapterInfo.content;
        // const type = this.chapterInfo.type;
        let data=this.changData;
        const formData = new FormData()
        formData.append('contentId', this.chapterInfo.contentId)
        formData.append('content', this.chapterInfo.content)
        formData.append('sequence', this.chapterInfo.sequence)
        formData.append('type', '1')

        this.$axios.post('/content/update', formData).then((resp)=>{
          console.log(resp);
          if(resp ==='success'){
            this.$message({
              type: 'success',
              message: '更新成功!'
            })
            // this.textRefresh()
            this.handleNodeClick(data);
          }else
          {
            this.$message({
              type:'error',
              message: '更新失败!'
            })
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消更新'
        });
      });
    },
    async saveLabel (index) {
      console.log('saveLabel', this.subData[index + (this.currentPage - 1) * this.size].type)
      const formData = new FormData()
      formData.append('chapter_id', this.chapterInfo.chapterId)
      // console.log('object', this.contentSplit[index]);
      formData.append('content', this.contentSplit[index].sub)
      formData.append('label', this.subData[index + (this.currentPage - 1) * this.size].type)
      formData.append('sequence', index)
      const res = await this.$axios.post('/content/category/add', formData)
      if (res === 'success') {
        this.$message({
          message: '编辑段落类型成功!',
          type: 'success',
          duration: '1200'
        })
      }
    },
    editLabel (index) {
      this.labelDialogVisible = true
      this.chooseLabelFrom = this.contentSplit[index].sub
      this.chooseId = this.chapterInfo.chapterId
      console.log('editLabel', index);
    },
    changeLabelVisible (val) {
      this.labelDialogVisible = val
    },
    handleChange (file, fileList) {
      // 批量导入正文需要使用.xls格式的文件
      if (file.raw.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' || file.raw.type === 'application/vnd.ms-excel') {
        this.uploadFile = file
      } else {
        this.$message({
          type: 'error',
          message: '文件类型错误，请上传.xls格式的文件！'
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
        fd.append('infotable', file)
        fd.append('bookid', this.chooseBook)
        this.uploadLoading = true
        const res = await this.$axios({
          method: 'post',
          url: '/content/table/in',
          data: fd,
          headers: config.headers
        })
        console.log('res', res)
        if (res === 'success') {
          this.$message({
            message: '导入成功！',
            type: 'success'
          })
          this.dialogFormVisible = false
          this.chooseBook = ''
          this.$refs.upload_excel.clearFiles()
        } else {
          this.$message({
            message: '上传失败！',
            type: 'error'
          })
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
    async formatFile() {
      if (this.chooseBook === '') {
        this.$message.error('请选择书籍！')
        return
      }
      console.log(this.chooseBook);
      const fd = new FormData()
      fd.append('bookid', this.chooseBook)
      try {
        const res = await this.$axios.post('/content/table', fd, { responseType: 'blob' })
        if (res) {
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(new Blob([res]))
          link.target = '_blank'
          // 文件名和格式
          link.download = `${this.chooseBook}.xls`
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          this.$message({
            type: 'success',
            message: '导出成功!'
          })
        }
      } catch (error) {
        this.$message({
          type: 'error',
          message: '导出失败！'
        })
        console.lof(err)
      } finally {}
    }
  },
  async created() {
    this.chapterGet()
    try {
      const res = await this.$axios.get('/book/1/100')
      const resData = res.data
      resData.forEach(e => {
        this.importForm.bookArr.push({
          label: e.bookname,
          value: e.bookId
        })
      })
    } catch (error) {
      
    }
  }
};
</script>

<style scoped lang="scss">
.text {
  font-size: 14px;
}

.item {
  padding: 18px 0;
}

.box-card {
  width: 800px;
}
.el-header, .el-footer {
  background-color: rgb(244, 207, 152);
  color: #333;
  text-align: left;
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
}

.content-info {
  max-height: calc(100vh - 180px);
  width: calc(100vw - 540px);
  overflow-y: auto;
  overflow-x: hidden;
}

.table{
  /*此元素会作为块级表格来显示（类似 <table>），表格前后带有换行符。*/
  display:table;
  /*border-collapse:collapse;*/
  border-collapse:separate;
  border:1px solid #ccc;
  width: 50%;
}
.table-caption{
  /*此元素会作为一个表格标题显示（类似 <caption>）*/
  display:table-caption;
  margin:0;
  font-size:16px;
}
.table-header-group{
  /*此元素会作为一个或多个行的分组来显示（类似 <thead>）。*/
  display:table-header-group;
  background:#eee;
  font-weight:bold;
  width: 50%;
}
.table-row-group{
  /*此元素会作为一个或多个行的分组来显示（类似 <tbody>）。*/
  display:table-row-group;
}
.table-footer-group{
  /*此元素会作为一个或多个行的分组来显示（类似 <tfoot>）。*/
  display:table-footer-group;
}
ul{
  list-style:none;
}
.table-row{
  /*此元素会作为一个表格行显示（类似 <tr>）。*/
  display: table-row;
}
.table-cell{
  /*此元素会作为一个表格单元格显示（类似 <td> 和 <th>）*/
  display: table-cell;
  padding: 0 5px;
  border: 1px solid #ccc;
  width: 50%
}
.table-row-group .table-row:hover,
.table-footer-group .table-row:hover{
  background:#f6f6f6;
  color:green;
  font-weight: bold;
}

.table-column-group{
  /*此元素会作为一个或多个列的分组来显示（类似 <colgroup>）。*/
  display:table-column-group;
}
.table-column{
  /*此元素会作为一个单元格列显示（类似 <col>）*/
  display:table-column;
  width:100px;
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
  color: red;
}
</style>
