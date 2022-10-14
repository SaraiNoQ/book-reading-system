<template>
  <div>
    <el-container>
      <el-header style="background-color: #f4cf98;">
        <el-button icon="el-icon-circle-plus-outline" circle @click="chapterGet;"></el-button>
      </el-header>
      <el-container>
        <el-aside width="300px" style="overflow: auto">
          <el-tree :data="chapterData" :props="defaultProps"></el-tree>
        </el-aside>
        <el-main style="background-color: white">
          <!-- 上传图片悬浮框 -->
          <el-dialog
            title="提示"
            :visible.sync="dialogVisible"
            width="30%">
            <span>这是一段信息</span>
            <span slot="footer" class="dialog-footer">
              <el-button @click="dialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
            </span>
          </el-dialog>
          <!-- 图片查询 -->
          <el-card class="box-card">
            <el-form  :model="imageForm" class="chapform" style="">
              <el-form-item label="图片名称">
                <el-input v-model="imageForm.imgName" style="width: 250px;" placeholder="请输入内容"></el-input>
              </el-form-item>
              <el-form-item label="图片URL">
                <el-input v-model="imageForm.imgUrl"  style="width: 250px;" placeholder="请输入内容"></el-input>
              </el-form-item>
            </el-form>
          </el-card>
          <!-- 图片列表 -->
          <el-table
              :data="urlData"
              style="width: 100%;"
              :header-cell-style="tableHeaderColor">
            <el-table-column
                label="序号"
                align="left"
                width="50"
                height="60"
                header-align="center">
              <template slot-scope="scope">
                {{ (scope.$index+1)+(currentPage-1)*size }}
              </template>
            </el-table-column>
            <el-table-column
                prop="date"
                label="图片名称"
                width="180"
                height="60"
                header-align="center">
            </el-table-column>
            <el-table-column
                prop="name"
                label="图片URL"
                width="350"
                height="60"
                header-align="center">
            </el-table-column>
            <el-table-column
                prop="address"
                label="备注"
                height="60"
                header-align="center">
            </el-table-column>
          </el-table>
          <!-- 上传图片功能 -->
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
              <img v-if="imageUrl" :src="imageUrl">
            </el-upload>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import {getRequest} from "../../utis/api";

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
      demoData:[],
      urlData:[],
      formDate: '',
      fileList: [{name: 'food.jpeg', url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'}, {name: 'food2.jpeg', url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'}],
      dialogVisible: false,
      imgUrl: '',
      files: [],
      images: []
    }
  },
  methods:{
    chapterGet(){
      getRequest('/chapter/getChapter').then(resp=>{
        if(resp)
        {
          const cha=localStorage.getItem('contentdata');

          let bookData=JSON.parse(JSON.stringify(resp))
          for(var key in bookData)
          {
            let op={
              chapter:'',
              cataLog:[]
            }
            op.chapter=key;
            op.cataLog=resp[key];
            //console.log(op);
            this.demoData.push(op);
          }
          this.chapterData=this.demoData;
        }
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
      const isLt2M = file.size <  1024 * 1024 * 2;
      if (!isImage) {
        // 验证图片格式
        this.$message.error('上传只能是png,jpg,jpeg,bmp,gif,webp格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过 2MB!');
      }
      if (isImage && isLt2M){
        // this.$refs.upload_img.uploadFiles 已上传的照片，status判断
        // fileList 是当前待上传的图片列表
        console.log('upload', fileList)
        console.log('base64');
        // this.imageList = fileList;
        // this.images[''] = fileList;
      } else{
        //删除上传失败的图片，不然会占位
        fileList.splice(-1,1);
      }
    },
    handleTestSuccess(file) {
      this.getBase64(file.file).then(resp => {
        console.log('base64-1', resp);
      })
      //构建一个formData对象，因为这里要求表单类型的数据
      this.formDate.append('file', file.file);
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
      console.log('uploading', this.$refs.upload_img.uploadFiles);
      this.$refs.upload_img.uploadFiles = []
    },
    submitUpload () {
      this.formDate = new FormData()
      this.$refs.upload_img.submit();
      this.formDate.append('WS_CODE', "12133");
      // formData 对象要用get方法获取
      console.log('uploadAll', this.formDate.getAll('file'));
      let config = {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }
      this.$axios.post('/book', this.formDate, config).then( res => {
          console.log(res)
      }).catch( res => {
          console.log(res)
      })     
    },
    handlePictureCardPreview (file) {
      this.dialogVisible = true
      console.log('clickItem', file);
    },
    handleRemove (file, fileList) {
      console.log('onremove', file, fileList);
    },
    // 组件提供的on-success失效，需要自行调用
    handleSuccess (response, file, fileList) {
      console.log('onsuccess', response, file, fileList);
    },
    // 转换base64格式
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
    addImage(file) {
      this.files.push(file);

      const img = new Image(),
          reader = new FileReader();

      reader.onload = (e) => this.images.push(e.target.result);

      reader.readAsDataURL(file);
      console.log('base64-2', this.images)
    },
  },
  created() {
    this.chapterGet();
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
  background-color: white;
  color: #333;
  text-align: center;
}

.el-main {
  background-color: #E9EEF3;
  color: #333;
  text-align: center;
  line-height: 60px;

}
.submit-btn {
  display: flex;
  justify-content: space-between;
  margin: 15px 260px 20px 260px;
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