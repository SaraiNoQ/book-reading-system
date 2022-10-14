<template>
  <div>
    <el-container>
      <el-header style="background-color: #f4cf98; padding-top: 10px">
        <el-button icon="el-icon-refresh" circle @click="chapterGet" title="刷新目录"></el-button>
        <el-button type="primary" icon="el-icon-upload2" circle @click="btnToUpload" title="上传图片"></el-button>
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
            @node-click="nodeClick"></el-tree>
        </el-aside>
        <el-main style="background-color: white; height: calc(100vh - 200px)">
          <!-- 上传图片悬浮框 -->
          <el-dialog
            title="图片预览"
            :visible.sync="dialogVisible"
            width="32%">
            <div>
              <el-image :src="showPhoto" 
              :preview-src-list="filesUpload"></el-image>
            </div>
            <span slot="footer" class="dialog-footer">
              <el-button @click="dialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
            </span>
          </el-dialog>

          <!-- 图片查询 -->
          <!-- <el-card class="box-card">
            <el-form  :model="imageForm" class="chapform" style="">
              <el-form-item label="图片名称">
                <el-input v-model="imageForm.imgName" style="width: 250px;" placeholder="请输入内容"></el-input>
              </el-form-item>
              <el-form-item label="图片URL">
                <el-input v-model="imageForm.imgUrl"  style="width: 250px;" placeholder="请输入内容"></el-input>
              </el-form-item>
            </el-form>
          </el-card> -->

          <!-- 图片列表预览 -->
          <el-dialog
            title="图片预览"
            :visible.sync="photoDialogVisible"
            width="32%">
            <div>
              <el-image
              :src="showListPhoto" 
              :preview-src-list="previewList">
                <div slot="placeholder" class="image-slot">
                  加载中<span class="dot">...</span>
                </div>
              </el-image>
            </div>
            <span slot="footer" class="dialog-footer">
              <el-button @click="photoDialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="photoDialogVisible = false">确 定</el-button>
            </span>
          </el-dialog>
          <!-- 图片列表 -->
          <el-table
              :data="urlData"
              style="width: 100%;"
              :header-cell-style="tableHeaderColor"
              v-loading="tableLoading">
            <el-table-column
                label="序号"
                align="center"
                width="50"
                height="60"
                header-align="center"
                type="index">
            </el-table-column>
            <el-table-column
                prop="imgname"
                label="图片名称"
                width="180"
                height="60"
                align="center"
                header-align="center">
            </el-table-column>
            <el-table-column
                prop="imgurl"
                label="图片URL"
                width="350"
                height="60"
                align="center"
                header-align="center">
            </el-table-column>
            <el-table-column
                prop="other"
                label="操作"
                height="60"
                align="center"
                header-align="center">
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    type="primary"
                    @click="handlePreview(scope.$index, scope.row)"
                    >预览</el-button>
                  <el-button
                    size="mini"
                    type="danger"
                    @click="handleDelete(scope.$index, scope.row)"
                    >删除</el-button>
                </template>
            </el-table-column>
          </el-table>
          <!-- 上传图片功能 -->
          <el-dialog
            title="图片上传"
            :visible.sync="upFunVis"
            v-loading="uploadLoading"
          >
            <div>
              <div class="submit-btn">
                <el-button size="small" type="primary" class="sub-btn" @click="clearFiles">清空上传列表</el-button>
                <el-button size="small" type="success" class="sub-btn" @click="submitUpload">上传到服务器</el-button>
              </div>
              <el-upload
                class="upload-demo"
                drag
                multiple
                ref="upload_img"
                action=""
                accept=".jpg, .jpeg, .png"
                :on-change="onChange"
                :on-preview="handlePictureCardPreview"
                :on-remove="handleRemove"
                :http-request="handleTestSuccess"
                list-type="picture"
                :auto-upload="false">
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                <div slot="tip" class="el-upload__tip">只能上传jpg/png/jepg文件，图片大小不能超过2MB</div>
              </el-upload>
            </div>
                <div slot="footer" class="dialog-footer">
                  <el-button @click="upFunVis = false">取 消</el-button>
                </div>
          </el-dialog>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import httpAPI from '../../utis/port.js'
export default {
  name: "imageTextMa",
  data(){
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
      loading: false,
      demoData:[],
      urlData:[],
      // formDate: '',
      dialogVisible: false,
      waitUpload: [],
      filesUpload: [],
      // files: [],
      // images: [],
      showPhoto: '',
      photoDialogVisible: false,
      showListPhoto: '',
      previewList: [],
      upFunVis: false,
      tableLoading: false,
      uploadLoading: false,
      // 图片服务器前缀
      imageUrlPrefix: ''
    }
  },
  methods:{
    // chapterGet(){
    //   this.loading = true
    //   this.chapterData = []
    //   if (this.$store.state.catalog.length !== 0) {
    //     this.chapterData = this.$store.state.catalog
    //     this.loading = false
    //   } else {
    //     getRequest('/chapter/getChapter').then(resp=>{
    //       console.log('post a request');
    //       if(resp)
    //       {
    //         const cha=localStorage.getItem('contentdata');

    //         let bookData=JSON.parse(JSON.stringify(resp))
    //         for(var key in bookData)
    //         {
    //           let op={
    //             chapter:'',
    //             cataLog:[]
    //           }
    //           op.chapter=key;
    //           op.cataLog=resp[key];
    //           //console.log(op);
    //           this.demoData.push(op);
    //         }
    //         this.chapterData=this.demoData;
    //         this.loading = false;
    //       }
    //     })
    //   }
    // },
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
    tableHeaderColor({ rowIndex}){
      if (rowIndex === 0) {
        return 'height=90px'
      }
    },
    // 文件状态改变时的钩子，添加文件、上传成功和上传失败时都会被调用
    // 判断用户上传的文件的类型和大小
    onChange (file, fileList) {
      /**
       * @file 当前上传文件的 name raw size status uid url
       * @fileList this.fileList属性
       */
      const isImage = file.raw.type == 'image/png' || file.raw.type == 'image/jpg' ||  file.raw.type == 'image/jpeg' || file.raw.type == 'image/bmp' || file.raw.type == 'image/gif' || file.raw.type == 'image/webp';
      const isLt10M = file.size <  1024 * 1024 * 10;
      if (!isImage) {
        // 验证图片格式
        this.$message.error('上传只能是png,jpg,jpeg,bmp,gif,webp格式!');
      }
      if (!isLt10M) {
        this.$message.error('上传图片大小不能超过 10MB!');
      }
      if (isImage && isLt10M){
        // this.$refs.upload_img.uploadFiles 已上传的照片，status判断
        // fileList 是当前待上传的图片列表
        console.log('upload', fileList);
        // 当往待上传列表中添加图片时，若格式和大小符合规定，触发
        fileList.forEach(e => {
          this.filesUpload.push(e.url)
        })
      } else{
        //删除上传失败的图片，不然会占位
        fileList.splice(-1,1);
      }
    },
    handleTestSuccess(file) {
      this.waitUpload.push(file.file)
      //删除上传成功的图片，不然会占位
      // this.$refs.upload_img.uploadFiles = this.$refs.upload_img.uploadFiles.filter(
      //   (item) => {
      //     return file.file.name != item.name
      //   }
      // )

      // 逐个上传
      // this.$axios.post('上传的url地址', formData, {
      //   headers: {
      //     //携带的参数
      //   },
      // })
      // .then((res) => {
      //   if (res.status === 200) {
      //     //数据处理
      //     this.$message.success('上传检测表附件成功!')
      //   }
      // })
      // .catch((err) => {
      //   //删除上传失败的图片，不然会占位
      //   this.$refs.upload_img.uploadFiles = this.$refs.upload_img.uploadFiles.filter(
      //     (item) => {
      //       return file.file.name != item.name
      //     }
      //   )
      //   this.$message.error('上传失败!')
      // })
    },
    clearFiles () {
      this.$refs.upload_img.uploadFiles = []
      this.waitUpload = []
      this.filesUpload = []
    },
    // 上传或删除图片后重新加载图片列表
    async refreshList () {
      const res = await this.$axios.post('/original/getImgUrl/' + this.$store.state.currentPhotoChapter.id)
      // 清空展示列表
      this.urlData = []
      const respData = res.data ? res.data : res
      respData.forEach(e => {
        this.urlData.push(e)
      })
    },
    async submitUpload () {
      const formData = new FormData()
      if (this.$store.state.currentPhotoChapter.id == null) {
        this.$message.error('请选择章节后再上传!')
        return
      }
      this.uploadLoading = true
      this.$refs.upload_img.submit();
      for (let i = 0; i < this.waitUpload.length; i++) {
        // 一定要等待异步
        const res = await this.getBase64(this.waitUpload[i])
        formData.append('file', res)
      }
      // formData 对象要用get方法获取
      formData.append('id', this.$store.state.currentPhotoChapter.id)
      formData.append('chapter', this.$store.state.currentPhotoChapter.chapter)
      this.$axios.post('/original/uploadImg', formData).then(res => {
        if (res === '') {
          this.upFunVis = false;
          this.clearFiles()
          this.refreshList()
        }
        this.uploadLoading = false
      }).catch( res => {
        console.log(res)
        this.uploadLoading = false
      })     
    },
    handlePictureCardPreview (file) {
      this.dialogVisible = true
      // console.log('clickItem', file.url);
      this.showPhoto = file.url;
    },
    // remove时触发，file为移除文件，fileList为剩余文件列表
    handleRemove (file, fileList) {
      // console.log('onremove', file, fileList);
      this.filesUpload = []
      fileList.forEach(e => {
        this.filesUpload.push(e.url)
      })
      // 初始置为空
      // submit的时候会重新往waitUpLoad数组中添加元素
      this.waitUpload.length = 0
    },
    // 组件提供的on-success失效，需要自行调用
    handleSuccess (response, file, fileList) {
      console.log('onsuccess', response, file, fileList);
    },
    // 转换base64格式-1 return string
    getBase64(file) {
      return new Promise(function (resolve, reject) {
        let reader = new FileReader();
        let imgResult = "";
        reader.readAsDataURL(file);
        reader.onload = function () {
          imgResult = reader.result;
        };
        reader.onerror = function (error) {
          reject(error);
        };
        reader.onloadend = function () {
          resolve(imgResult);
        };
      });
    },
    // 方法-2 return Array<string>
    addImage(file) {
      this.files.push(file);

      const img = new Image(),
          reader = new FileReader();

      reader.onload = (e) => this.images.push(e.target.result);

      reader.readAsDataURL(file);
    },
    async handleDelete(index, row) {
      // console.log('waiting for remove', row)
      this.tableLoading = true
      const formData = new FormData()
      formData.append('imgUrl', row.imgurl)
      formData.append('imgId', row.id)
      this.$axios.post('/original/deleteImg', formData).then(async resp => {
        // console.log('deleteResp', resp);
        if (resp === 'success') {
          await this.refreshList()
          this.tableLoading = false
          this.$message({
            message: '删除成功!',
            duration: 700,
            type: 'success'
          })
        } else {
          this.tableLoading = false
        }
      }).catch(resp => {
        this.tableLoading = false
      })
    },
    /**
     * 
     * @param index click-index
     * @param row click-item
     */
    handlePreview (index, row) {
      this.showListPhoto = this.imageUrlPrefix + row.imgurl
      this.photoDialogVisible = true
      // console.log('show', 'http://10.6.50.239:9191' + row.imgurl);
    },
    nodeClick (data) {
      // console.log('node-click', data);
      this.tableLoading = true
      // vuex中存放章节id和name
      let item = {}
      item.chapter = data.chapter
      item.id = data.id
      this.$store.commit('setCurrentPhotoChapter', item)
      if (data.id) {
        this.$axios.post('/original/getImgUrl/' + data.id).then(resp => {
          // 清空展示列表
          this.urlData = []
          const respData = resp.data ? resp.data : resp
          respData.forEach(e => {
            // let data = {}
            // data.imgname = e.imgname
            // data.imgurl = e.imgurl
            this.urlData.push(e)
          })
          this.tableLoading = false
        })
      } else {
        this.tableLoading = false
      }
    },
    btnToUpload () {
      this.upFunVis = true
    }
  },
  created() {
    this.chapterGet()
    this.imageUrlPrefix = httpAPI()
  }
}
</script>

<style lang="scss" scoped>
el-header, .el-footer {
  background-color: #B3C0D1;
  color: #333;
  text-align: center;
  line-height: 60px;
}

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

.el-main {
  background-color: #E9EEF3;
  color: #333;
  text-align: center;
  line-height: 60px;

  ::v-deep .el-dialog__body {
    padding: 0 20px 0 20px;
  }
  ::v-deep .el-image__inner {
    height: 50vh;
  }
}
.submit-btn {
  display: flex;
  justify-content: space-between;
  margin: 15px 200px 20px 200px;
}

.upload-demo {
  ::v-deep .el-upload__tip {
    margin-top: 0px;
    margin-bottom: 12px;
    line-height: 30px;
  }
  ::v-deep .el-upload-list__item-status-label {
    // display: inherit;
  }
  ::v-deep .el-icon-close {
    top: 3px;
  }
}

::v-deep .el-icon-plus {
  line-height: 10;
}

::v-deep .el-upload-list__item {
  margin: 0 auto;
  width: 70%;
  text-align: center;
  .el-upload-list__item-name {
    margin: 20px 5px 22px 0px;
  }
  font-size: 17px;
  cursor: pointer;
}

.box-card{
    height: 130px;
}

.box-card.is-never-shadow
{
  box-shadow: never
}
</style>