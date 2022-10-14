<template>
  <div v-loading="loading">
      <div class="head2" style="margin-bottom: 20px;height: 60px">
        <el-card style="display: flex;background-color: #f4cf98">
          <el-row style="">
            <el-button icon="el-icon-refresh-left" circle @click="initTableData" title="刷新列表"></el-button>
            <el-button type="danger" icon="el-icon-delete-solid" circle @click="display1" title="删除书籍"></el-button>
            <el-button type="warning" icon="el-icon-edit" @click="updateBook" circle title="修改书籍"></el-button>
            <el-button type="primary" icon="el-icon-folder-add" @click="dynastyListGet" circle title="添加书籍"></el-button>
            <!-- <el-button type="success" icon="el-icon-view" @click="dialogCEVisible = true" circle title="预览"></el-button> -->
          </el-row>
        </el-card>
      </div>

    <!-- <div>
      <el-card style="margin-bottom: 10px;background-color: white">

        <el-form :inline="true" :model="searchInfoList" class="demo-form-inline">
          <el-form-item label="典籍名称">
            <el-input v-model="searchInfoList.bookname" placeholder="请输入内容"></el-input>
          </el-form-item>
          <el-form-item label="作者">
            <el-input v-model="searchInfoList.author" placeholder="请输入内容"></el-input>
          </el-form-item>
          <el-form-item label="朝代" style="">
            <el-select v-model="searchInfoList.value" placeholder="请选择">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </el-card>
    </div> -->

    <div>
      <el-table
          :data="table"
          border
          style="width: 100%"
          highlight-current-row
          @current-change="handleCurrentChangeTable">
        <el-table-column
            label="序号"
            align="center"
            width="60">
          <template slot-scope="scope">
            {{ (scope.$index+1)+(currentPage-1)*size }}
          </template>
        </el-table-column>
        <el-table-column
            prop="bookname"
            label="典籍名称"
            align="center"
            width="270px">
        </el-table-column>
        <el-table-column
            prop="author"
            label="作者"
            align="center"
            width="210px">
        </el-table-column>
        <el-table-column
            prop="dynasty"
            label="朝代"
            align="center"
            width="180px">
        </el-table-column>
        <el-table-column
            prop="createtime"
            label="录入时间"
            align="center">
        </el-table-column>
        <el-table-column
            prop="creater"
            label="录入人员"
            align="center"
            width="180px">
        </el-table-column>
      </el-table>
      </div>

      <div style="display:flex; justifyContent:center; marginTop:10px;">
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


    <!--功能区-->

    <!--添加区-->
    <el-dialog title="添加典籍信息" :visible.sync="dialogFormVisible"  class="dialog" width="40%">

      <div class="el-dialog-div">
      <el-form :model="addForm" ref="addForm" >
        <el-form-item label="图书名" :label-width="formLabelWidth" prop="bookName">
          <el-input style="width:200px;" type="text" v-model="addForm.bookName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="作者" :label-width="formLabelWidth" prop="author">
          <el-input style="width:200px;" v-model="addForm.author" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="朝代" :label-width="formLabelWidth"  prop="dynasty">
          <el-select v-model="addForm.dynasty" placeholder="请选择">
            <el-option
                v-for="item in optionsList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="典籍介绍" :label-width="formLabelWidth" prop="introduction" >
          <el-input
            type="textarea"
            style="width:280px;"
            v-model="addForm.introduction"
            :rows="6"
          ></el-input>
        </el-form-item>

        <el-form-item label="图片上传" :label-width="formLabelWidth">
          <div class="viewPhoto">
            <!-- 实时显示图片的容器 -->
            <img src alt="未选择图片" ref="portrait" style="width: 230px ;height: 250px"/>
          </div>
          <!-- 提交图片文件的按钮 -->
          <input type="file" ref="saveImage" @click="great()" @change="getUrl($event)" />
        </el-form-item>

      </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('addForm')">重 置</el-button>
        <el-button type="primary" @click="addBook()" :loading="addBookLoading">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 删除区 -->
    <el-dialog
        title="删除典籍"
        :visible.sync="dialogOneVisible"
        width="50%"
    >

      <div class="table">
        <!--此行代码用于，控制列的样式。-->
        <div class="table-column-group">
          <div class="table-column"></div>
          <div class="table-column"></div>
          <div class="table-column"></div>
        </div>
        <div class="table-header-group">
          <ul class="table-row">
            <li class="table-cell">书籍名称</li>
          </ul>
        </div>
        <div class="table-row-group">
          <ul class="table-row">
            <li class="table-cell">{{ searchInfoList.bookname }}</li>
          </ul>
        </div>
      </div>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogOneVisible = false">取 消 删 除</el-button>
        <el-button type="primary" @click="bookDelete">确 定 删 除</el-button>
      </span>
    </el-dialog>

    <!-- 修改典籍信息 -->
    <el-dialog
      title="修改典籍"
      :visible.sync="dialogUpdateVisible"
      destroy-on-close
    >
      <div class="el-dialog-div">
        <el-form :model="updateBookInfo" ref="updateForm" >
        <el-form-item label="排序号" :label-width="formLabelWidth1" prop="sequence">
          <el-input style="width: 70%" type="text" v-model="updateBookInfo.sequence" autocomplete="off"></el-input>
        </el-form-item>
          <el-form-item label="图书名" :label-width="formLabelWidth1" prop="bookname">
            <el-input style="width: 70%" type="text" v-model="updateBookInfo.bookname" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="作者" :label-width="formLabelWidth1" prop="author">
            <el-input  style="width: 70%" v-model="updateBookInfo.author" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="朝代" :label-width="formLabelWidth1" prop="dynasty">
            <el-select v-model="updateBookInfo.dynasty" placeholder="请选择">
              <el-option
                v-for="(item, index) in dynastyList"
                :key="index"
                :label="item"
                :value="item"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="典籍介绍" :label-width="formLabelWidth1" prop="introduction" >
            <el-input
              type="textarea"
              style="width: 70%"
              v-model="updateBookInfo.introduction"
              :rows="6"
            ></el-input>
          </el-form-item>

          <el-form-item label="封面" :label-width="formLabelWidth1">
            <div class="view-photo">
              <!-- 实时显示图片的容器 -->
              <img src alt="未选择图片" ref="updatePortrait" style="width: 30%; marginRight: 20px"/>
              <!-- 提交图片文件的按钮 -->
              <input type="file" ref="updateImage" @click="updateGreat()" @change="updateUrl($event)" />
            </div>
          </el-form-item>

        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelUpdate" :loading="loadingUpdate">取 消</el-button>
        <el-button type="primary" @click="confirmUpdateBook" :loading="loadingUpdate">修 改</el-button>
      </div>
    </el-dialog>

    <!-- 测试区 -->
    <!-- <el-dialog
        title="提示"
        :visible.sync="dialogCEVisible"
        width="50%"
        >
      <span style="margin: 15px">
        {{searchInfoList.bookname}}
      </span>
      <span style="margin: 15px">
        {{searchInfoList.author}}
      </span>
      <span style="margin: 15px">
        {{searchInfoList.value}}
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogCEVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogCEVisible = false">确 定</el-button>
      </span>
    </el-dialog> -->


  </div>
</template>

<script>
import dayjs from 'dayjs'
export default {
  name: "basicInfo",
  data(){
    return{
      table: [],
      bookInfo:[],
      total:1,
      size:10,
      currentPage:1,
      options: [],
      optionsList:[],
      addForm:{
          bookName:'',
          author:'',
          dynasty:'',
          introduction:''
      },
      formLabelWidth: '100px',
      formLabelWidth1: '140px',
      dialogFormVisible:false,
      dialogCEVisible:false,
      dialogOneVisible:false,
      searchInfoList:{
        bookname:'',
        author:'',
        value: '未选择',
      },
      imageurl:'',
      clickItem: '',
      loading: false,
      dialogUpdateVisible: false,
      // 待更新的图片信息
      updateBookInfo: {
        sequence: '',
        bookId: '',
        author: '',
        bookname: '',
        dynasty: '',
        imgurl: '',
        introduction: ''
      },
      updateImageUrl: '',
      loadingUpdate: false,
      dynastyList: [],
      addBookLoading: false
    }
  },
  mounted() {
    this.loading = true
    this.$axios.get('/book/' + this.currentPage + '/' + this.size).then(resp=>{
      if (resp) {
        this.table = resp.data
        this.table.map(e => {
          e.createtime = dayjs(e.createtime).format('YYYY-MM-DD HH:mm:ss')
        })
        this.bookInfo = resp.data
        this.total = resp.total
      }
      this.loading = false
    })
  },
  methods:{
    display1() {
      if (!this.searchInfoList.bookId) {
        this.$message.error('请先选择要删除的图书')
        return
      }
      // this.dialogOneVisible = true
      this.bookDelete(this.searchInfoList.bookname)
    },
    bookDelete(bookName) {
      this.$confirm(`此操作将永久删除《${bookName}》该书, 是否删除?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const id = this.searchInfoList.bookId
        this.$axios.post('/book/delete/' + id).then((resp)=>{
          if(resp ==='success'){
            this.$message({
              type: 'success',
              message: '删除成功！'
            })
            this.dialogOneVisible = false
            // 将点击的图书信息删除
            this.searchInfoList = {}
            this.initTableData()
          } else {
            this.$message({
              type:'error',
              message: '删除失败！'
            })
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    } ,
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    handleSizeChange(newSize){
      this.size=newSize;
      this.initTableData()
    },
    handleCurrentChange (newPage) {
      this.currentPage = newPage
      this.initTableData()
    },
    initTableData () {
      this.loading = true
      this.$axios.get('/book/' + this.currentPage + '/' + this.size).then(resp => {
        if (resp) {
          this.table = resp.data
          console.log('init', this.table)
          this.table.map(e => {
            e.createtime = dayjs(e.createtime).format('YYYY-MM-DD HH:mm:ss')
          })
          this.total = resp.total
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    addBook() {
      if (this.addForm.bookName === '' || this.addForm.author === '' || this.addForm.dynasty === '' || this.addForm.introduction === '') {
        this.$message.error('请输入完整的书籍信息！')
        return
      }
      // console.log('img', this.$refs.saveImage.files[0]);
      this.addBookLoading = true
      const formData = new FormData()
      formData.append('bookName', this.addForm.bookName)
      formData.append('author', this.addForm.author)
      formData.append('dynasty', this.addForm.dynasty)
      formData.append('introduction', this.addForm.introduction)
      if (this.imageurl) {
        formData.append('img', [this.imageurl])
      }
      // let config = {
      //   'Content-Type': 'multipart/form-data'
      // }
      this.$axios.post('/book/add', formData).then(resp => {
        this.addBookLoading = false
        if (resp === 'success') {
          this.dialogFormVisible = false
          this.$message.success('添加成功!')
          this.initTableData()
          this.resetForm('addForm')
        } else if (resp === '书籍已存在') {
          this.$message.warning('书籍已存在!')
        } else if (resp === '上传图书基本信息成功。但是封面上传失败。请手动设置封面图片') {
          this.$message.warning('上传图书基本信息成功！封面上传失败，请手动设置封面图片！')
        } else {
          this.$message.error('添加失败!')
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      // 清空input标签中file的值
      this.$refs.saveImage.value = '';
      // 清空实时显示的图片的值
      this.$refs.portrait.src = '';
    },

    // 实时显示该图片在页面
    great () {
      this.$refs.saveImage.onchange = () => {
        var imgFile = this.$refs.saveImage.files[0]
        var fr = new FileReader()
        fr.onload = () => {
          this.$refs.portrait.src = fr.result
        }
        fr.readAsDataURL(imgFile)
      }
    },
    updateGreat() {
      this.$refs.updateImage.onchange = () => {
        var imgFile = this.$refs.updateImage.files[0]
        var fr = new FileReader()
        fr.onload = () => {
          this.$refs.updatePortrait.src = fr.result
        }
        fr.readAsDataURL(imgFile)
      }
    },
    updateUrl (e) {
      let file = e.target.files[0]
      // 创建文件读取对象
      var reader = new FileReader()
      //  将文件读取为DataURL
      reader.readAsDataURL(file)
      // 读取成功调用方法
      reader.onload = e => {
        // e.target.result 获取 读取成功后的  文件DataURL
        this.updateImageUrl = e.target.result
        // 如果要将图片上传服务器，就在这里调用后台方法
      }
    },
    getUrl (e) {
      let file = e.target.files[0]
      // 创建文件读取对象
      var reader = new FileReader()
      var that = this
      //  将文件读取为DataURL
      reader.readAsDataURL(file)
      // 读取成功调用方法
      reader.onload = e => {
        // e.target.result 获取 读取成功后的  文件DataURL
        that.imageurl= e.target.result
        // 如果要将图片上传服务器，就在这里调用后台方法
      }
    },
    addEtidBannerData() {
      // 获取图片的信息
      let imgData = this.$refs.saveImage.files[0]
      console.log(imgData)
      //创建一个FormData对象
      let params = new FormData()
      // 将图片的信息存进params
      params.append('file', imgData)
      // 循环把表单的内容也装进params,obj表示"键",this.form[obj]表示"键值"
      for (var obj in this.form) {
        console.log(obj, '-', this.form[obj])
        params.append(obj, this.form[obj])
      }
      console.log(params)
      // 当提交表单请求成功时,清空缓存的数据
      // 清空表单数据
      this.form = {
        imageTitle: '',
        toUrl: ''
      }
      // 清空input标签中file的值
      this.$refs.saveImage.value = ''
      // 清空实时显示的图片的值
      this.$refs.portrait.src = ''
    },
    async getDynasty () {
      const res = await this.$axios.post('/book/getDynasty')
      return res
    },
    dynastyListGet () {
      this.$axios.post('/book/getDynasty').then((resp)=>{
        let demoData = []

        //this.optionsList=demoData;
        resp.forEach((item) => {
          let op = {
            value: '',
            label: ''
          }
          op.value = item
          op.label = item
          demoData.push(op)
        })
        this.optionsList = demoData
        this.options = demoData
        this.dialogFormVisible = true
      }).catch(() => {
        this.$message.warning('获取失败!')
      })
    },
    pageDynastyGet () {
      this.$axios.post('/book/getDynasty').then((resp)=>{
      //  console.log(resp)
        let demoData=[];

        //this.optionsList=demoData;
        resp.forEach((item) => {
         // console.log(item);
          let op = {
            value: '',
            label: ''
          }
          op.value = item;
          op.label = item;
          demoData.push(op);
        })
        this.options=demoData;
      })
    },
    handleCurrentChangeTable (val) {
      this.searchInfoList = val
      console.log('this.info', this.searchInfoList)
      if (!this.searchInfoList) {
        this.searchInfoList = {
          bookname: '',
          author: '',
          dynasty: '未选择'
        }
      }
    },
    async updateBook () {
      if (!this.searchInfoList.bookId) {
        this.$message.error('请先选择要修改的图书！')
        return
      }
      const res = await this.getDynasty()
      this.dynastyList = res
      this.updateBookInfo = this.searchInfoList
      this.dialogUpdateVisible = true
    },
    cancelUpdate () {
      // this.$refs.updatePortrait.src = ''
      // this.$refs.updateImage.files = []
      // this.updateBookInfo = {}
      this.dialogUpdateVisible = false
    },
    async confirmUpdateBook () {
      if (this.updateBookInfo) {
        const fd = new FormData()
        fd.append('sequence', this.updateBookInfo.sequence)
        fd.append('author', this.updateBookInfo.author)
        fd.append('bookName', this.updateBookInfo.bookname)
        fd.append('dynasty', this.updateBookInfo.dynasty)
        fd.append('id', this.updateBookInfo.bookId)
        fd.append('introduction', this.updateBookInfo.introduction)
        this.loadingUpdate = true
        if (this.updateImageUrl) {
          const imgfd = new FormData()
          imgfd.append('id', this.updateBookInfo.bookId)
          imgfd.append('img', [this.updateImageUrl])
          const res1 = await this.$axios.post('/book/update/img', imgfd)
          if (res1 !== 'success') {
            this.$message.success('修改失败!')
            return
          }
        }
        const res = await this.$axios.post('/book/update', fd)
        if (res === 'success') {
          this.loadingUpdate = false
          this.$message.success('修改成功!')
          this.dialogUpdateVisible = false
          this.updateBookInfo = {}
          this.initTableData()
        } else if (res === '修改后的书名已存在请修改') {
          this.loadingUpdate = false
          this.$message.warning('修改后的书名已存在，请修改！')
        } else {
          this.loadingUpdate = false
          this.$message.error('修改失败!')
        }
      }
    }
  },
  created() {
    this.pageDynastyGet();

  }

}
</script>

<style scoped>
.el-dialog-div{
  height: 60vh;
  overflow: auto;
}
/*
::v-deep .el-dialog .el-dialog__body{
  display: flex;
  justify-content: center;
  align-items: center;
}
*/
.view-photo {
  display: flex;
}
</style>