<template>
    <div v-loading="pageLoading">
        <!-- 导入悬浮框 -->
        <el-dialog title="导入异文" :visible.sync="importDialogVisible">
          <el-form :model="importForm">
            <el-form-item label="来源书籍" :label-width="formLabelWidth">
                <el-select v-model="chooseFromBook" placeholder="请选择导入书籍">
                    <el-option v-for="(item, index) in importForm.bookArr"
                        :key="index"
                        :label="item"
                        :value="item"
                    ></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="目标书籍" :label-width="formLabelWidth">
                <el-select v-model="chooseToBook" placeholder="请选择导入书籍">
                    <el-option v-for="(item, index) in importForm.bookArr"
                        :key="index"
                        :label="item"
                        :value="item"
                    ></el-option>
                </el-select>
            </el-form-item>

            <!-- diffConnect -->
            <el-form-item label="目录文件" :label-width="formLabelWidth">
              <el-upload
                action=""
                ref="upload_excel_connect"
                :auto-upload="false"
                :on-change="handleConnectChange"
                :on-remove="handleConnectRemove"
                :http-request="handleTestSuccess"
                :on-exceed="handleConnectExceed"
                multiple
                :limit="1">
                <el-button size="small" type="primary">点击上传</el-button>
                <span slot="tip" class="el-upload__tip tip">只能上传.xlsx格式文件</span>
              </el-upload>
            </el-form-item>
            <!-- diffFrom -->
            <el-form-item label="来源文件" :label-width="formLabelWidth">
              <el-upload
                action=""
                ref="upload_excel_from"
                :auto-upload="false"
                :on-change="handleFromChange"
                :on-remove="handleFromRemove"
                :http-request="handleTestSuccess"
                :on-exceed="handleFromExceed"
                multiple
                :limit="1">
                <el-button size="small" type="primary">点击上传</el-button>
                <span slot="tip" class="el-upload__tip tip">只能上传.xlsx格式文件</span>
              </el-upload>
            </el-form-item>
            <!-- diffTo -->
            <el-form-item label="目标文件" :label-width="formLabelWidth">
              <el-upload
                action=""
                ref="upload_excel_to"
                :auto-upload="false"
                :on-change="handleToChange"
                :on-remove="handleToRemove"
                :http-request="handleTestSuccess"
                :on-exceed="handleToExceed"
                multiple
                :limit="1">
                <el-button size="small" type="primary">点击上传</el-button>
                <span slot="tip" class="el-upload__tip tip">只能上传.xlsx格式文件</span>
              </el-upload>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="importDialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="uploadExcel" :loading="uploading">上 传</el-button>
          </div>
        </el-dialog>

        <el-dialog title="导入异文" :visible.sync="import2DialogVisible">
          <el-form :model="importForm">
            <el-form-item label="来源书籍" :label-width="formLabelWidth">
                <el-select v-model="chooseFromBook" placeholder="请选择导入书籍">
                    <el-option v-for="(item, index) in importForm.bookArr"
                        :key="index"
                        :label="item"
                        :value="item"
                    ></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="目标书籍" :label-width="formLabelWidth">
                <el-select v-model="chooseToBook" placeholder="请选择导入书籍">
                    <el-option v-for="(item, index) in importForm.bookArr"
                        :key="index"
                        :label="item"
                        :value="item"
                    ></el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="文件" :label-width="formLabelWidth">
              <el-upload
                action=""
                ref="upload_excel_file"
                :auto-upload="false"
                :on-change="handleFileChange"
                :on-remove="handleFileRemove"
                :http-request="handleTestSuccess"
                :on-exceed="handleFileExceed"
                multiple
                :limit="1">
                <el-button size="small" type="primary">点击上传</el-button>
                <span slot="tip" class="el-upload__tip tip">只能上传.xlsx格式文件</span>
              </el-upload>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="import2DialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="uploadExcel2" :loading="uploading2">上 传</el-button>
          </div>
        </el-dialog>

        <el-container>
            <Label
                :labelDialogVisible="labelDialogVisible"
                @on-before-close="changeLabelVisible"
                :labelFrom="chooseLabelFrom"
                :labelTo="chooseLabelTo"
                :chooseId="diffId"
                :labelFromData="chooseFromJson"
                :labelToData="chooseToJson"
            />
            <el-header style="background-color: #f4cf98; padding-top: 10px">
                <div class="import-parent">
                    <el-button icon="el-icon-refresh" circle title="刷新目录" @click="chapterGet"></el-button>
                    <el-button type="danger" icon="el-icon-delete" title="删除异文：需要预先选中需要删除的异文记录" @click="removeYiwen" circle></el-button>
                    <!-- <el-button type="primary" icon="el-icon-upload2" title="上传异文：需要预先选中左右两侧目录树对应章节" circle @click="link"></el-button> -->
                    <el-button type="primary" icon="el-icon-edit" title="修改异文：需要预先选中需要修改的异文记录" @click="editYiwen" circle></el-button>
                    <el-button type="success" icon="el-icon-edit-outline" title="标记异文：需要预先选中需要标记的异文记录" @click="labelYiwen" circle></el-button>
                    <div class="import">
                        <el-button icon="el-icon-upload" circle @click="importDiff2" title="批量导入异文"></el-button>
                        <el-button icon="el-icon-upload2" circle @click="importDiff" title="批量导入异文"></el-button>
                    </div>
                </div>
            </el-header>
            <el-container class="yiwen1">
                <el-aside width="300px" style="overflow: auto">
                    <!-- 目录树 -->
                    <el-tree
                        id="tree"
                        v-loading="loading"
                        :data="chapterData"
                        :props="defaultProps"
                        class="tree"
                        @node-click="nodeClick1"
                        highlight-current
                    ></el-tree>
                </el-aside>
                <el-main style="background-color: white; height: calc(100vh - 340px)" class="main-dis">
                    <p class="dianji">典籍一</p>
                    <el-input
                        type="textarea"
                        :autosize="{ minRows: 6, maxRows: 7}"
                        placeholder="请输入原文"
                        v-model="textarea1">
                    </el-input>
                    <p class="dianji1">典籍二</p>
                    <el-input
                        type="textarea"
                        :autosize="{ minRows: 6, maxRows: 7}"
                        placeholder="请输入异文"
                        v-model="textarea2">
                    </el-input>
                </el-main>

                <el-aside width="300px" style="overflow: auto">
                <!-- 目录树 -->
                <!-- <el-tree
                    v-loading="loading"
                    :data="chapterData"
                    :props="defaultProps"
                    class="tree"
                    @node-click="nodeClick2"
                    highlight-current>
                    ></el-tree> -->
                    <el-tree
                        id="tree2"
                        v-loading="loading"
                        :data="chapterData"
                        :props="defaultProps"
                        class="tree2"
                        @node-click="nodeClick2"
                        highlight-current
                    ></el-tree>
                </el-aside>
            </el-container>
            <div class="upload-header">
                <el-button
                    type="primary" 
                    class="upload-main"
                    icon="el-icon-upload2" 
                    title="上传异文：需要预先选中左右两侧目录树对应章节" 
                    circle 
                    @click="link"
                ></el-button>
            </div>
            <el-table
                v-loading="tableLoading"
                :data="tableData.slice((currentPage - 1) * size, currentPage * size)"
                max-height="600"
                border
                style="width: 100%"
                highlight-current-row
                @current-change="handleCurrentChange"
                @row-click="rowClick"
                @selection-change="handleSelectionChange"
            >
                <el-table-column
                    type="selection"
                    align="center"
                    width="40">
                </el-table-column>
                <el-table-column
                    label="序号"
                    align="center"
                    width="50"
                    height="60"
                    header-align="center">
                    <template slot-scope="scope">
                        {{ (scope.$index+1)+(currentPage-1)*size }}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="bookFrom"
                    label="典籍一名称"
                    align="center"
                    header-align="center"
                    width="120">
                </el-table-column>
                <el-table-column
                    prop="chapterFrom"
                    label="典籍一章节目"
                    align="center"
                    header-align="center"
                    width="150">
                </el-table-column>
                <el-table-column
                    prop="diffFrom"
                    label="典籍一章内容"
                    align="center"
                    header-align="center"
                    width="315">
                </el-table-column>
                <el-table-column
                    prop="bookTo"
                    label="典籍二名称"
                    align="center"
                    header-align="center"
                    width="100">
                </el-table-column>
                <el-table-column
                    prop="chapterTo"
                    label="典籍二章节目"
                    align="center"
                    header-align="center"
                    width="150">
                </el-table-column>
                <el-table-column
                    prop="diffTo"
                    label="典籍二章内容"
                    align="center"
                    header-align="center">
                </el-table-column>
                <!-- <el-table-column
                    prop="address"
                    label="备注">
                </el-table-column> -->
            </el-table>
            <el-pagination
                 @size-change="handleSizeChange"
                 @current-change="handleCurrentPageChange"
                 :current-page="currentPage"
                 :page-sizes="[20, 50, 100, 200]"
                 :page-size="size"
                 layout="total, sizes, prev, pager, next, jumper"
                 :total="total"
                 style="margin: 3px auto">
            </el-pagination>
        </el-container>
    </div>
</template>
<script>
import XLSX from 'xlsx'
import Label from './label.vue'

export default {
    name: 'yw',
    components: { Label },
    data () {
        return {
            chapterData: [],
            defaultProps: {
                children: 'cataLog',
                label: 'chapter'
            },
            contentForm:{
                content: ''
            },
            demoData:[],
            loading: true,
            tableData: [],
            textarea1: '',
            textarea2: '',
            leftNode: '',
            rightNode: '',
            tableLoading: false,
            // 修改异文时使用
            diffId: '',
            // 标注弹窗
            labelDialogVisible: false,
            // 标记异文时使用
            chooseLabelTo: '',
            chooseLabelFrom: '',
            chooseFromJson: {},
            chooseToJson: {},
            total: 0,
            size: 10,
            currentPage: 1,
            importDialogVisible: false, // 导入异文弹窗
            formLabelWidth: '140px',
            connectFileList: [], // connect文件列表
            fromFileList: [], // from文件列表
            toFileList: [], // to文件列表
            fileFileList: [], // file文件列表
            importForm: {
                bookArr: []
            },
            chooseFromBook: '',
            chooseToBook: '',
            uploading: false, // 是否正在上传
            multipleSelection: [], // 多选框选中的数据
            import2DialogVisible: false, // 导入异文弹窗2
            uploading2: false, // 是否正在上传2
            pageLoading: false, // 是否正在加载
        }
    },
    async mounted () {
        this.chapterGet()
        const res = await this.$axios.post('/code/Books')
        this.importForm.bookArr = res
    },
    methods: {
        // chapterGet(){
        //     // 初始化设置
        //     this.loading = true
        //     this.chapterData = []
        //     if (this.$store.state.catalog.length !== 0) {
        //         this.chapterData = this.$store.state.catalog
        //         this.loading = false
        //     } else {
        //         getRequest('/chapter/getChapter').then(resp=>{
        //             console.log('post a request');
        //             if (resp) {
        //                 const bookData = resp.data ? resp.data : resp
        //                 bookData.forEach(e => {
        //                     e.forEach(e1 => {
        //                     this.demoData.push(e1)
        //                     })
        //                 })
        //                 this.chapterData = this.demoData
        //                 console.log(this.chapterData);
        //                 // for(var key in bookData)
        //                 // {
        //                 // let op={
        //                 //     chapter:'',
        //                 //     cataLog:[]
        //                 // }
        //                 // op.chapter=key;
        //                 // op.cataLog=resp[key];
        //                 // //console.log(op);
        //                 // this.demoData.push(op);
        //                 // }
        //                 // this.chapterData=this.demoData;
        //                 this.loading = false
        //             }
        //         })
        //     }
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
                    this.chapterData1 = this.demoData
                    console.log(this.chapterData);
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
                //     // e 为某一本书的全部内容
                //     const chapterData = e.chapter
                //     const idReg = /id\=.*?\,/
                //     const booknameReg = /bookname\=.*?\,/
                //     // 获得到bookId
                //     const obj = {}
                //     obj.bookId = idReg.exec(chapterData)[0].replace(',', '').slice(3)
                //     obj.chapter = booknameReg.exec(chapterData)[0].replace(',', '').slice(9)
                //     obj.cataLog = e.cataLog
                //     this.chapterData.push(obj)
                //     })
                }
                this.loading = false
            })
        },
        async refreshTable (id) {
            const formData = new FormData()
            formData.append('chapterId', id)
            this.tableLoading = true
            const res = await this.$axios.post('/diff/getDiff', formData)
            this.tableData = res
            this.total = this.tableData.length
            this.tableLoading = false
            return res
        },
        async nodeClick1 (data) {
            if (data.id) {
                this.leftNode = data
                
                // 传入当前点击章节的id，获取列表
                await this.refreshTable(data.id)
                // 初始化diffId、textarea1、textarea2
                this.diffId = ''
                this.textarea1 = ''
                this.textarea2 = ''
            }
        },
        nodeClick2 (data) {
            console.log('right', data);
            if (data) {
                this.rightNode = data
            }
        },
        link () {
            if (this.leftNode && this.rightNode) {
                const formData = new FormData()
                formData.append('chapterIdTo', this.rightNode.id)
                formData.append('chapterIdFrom', this.leftNode.id)
                if (this.textarea1 === '' || this.textarea2 === '') {
                    this.$message.error('请输入原文和异文!')
                    return
                }
                formData.append('contentFrom', this.textarea1)
                formData.append('contentTo', this.textarea2)
                this.$axios.post('/diff/addDiff', formData).then(res => {
                    if (res === 'success') {
                        // 刷新列表
                        const formData1 = new FormData()
                        formData1.append('chapterId', this.leftNode.id)
                        this.tableLoading = true
                        this.$axios.post('/diff/getDiff', formData1).then(res => {
                            this.tableData = res.data ? res.data : res
                            this.total = this.tableData.length
                            this.tableLoading = false
                        })
                    }
                })
            } else {
                this.$message.error('请确定对应章节关系!')
            }
        },
        handleCurrentChange (val) {
        },
        rowClick (val) {
            console.log('rowClick', val);
            this.textarea1 = val.diffFrom
            this.textarea2 = val.diffTo
            this.diffId = val.id
            this.chooseLabelFrom = val.diffFrom
            this.chooseLabelTo = val.diffTo
        },
        editYiwen () {
            if (!this.diffId) {
                this.$message({
                    type: 'error',
                    message: '请先选择要修改的异文!',
                    duration: 700
                })
                return
            }
            this.$confirm('此操作将修改选中的异文, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                const formData = new FormData()
                formData.append('diffId', this.diffId)
                formData.append('contentFrom', this.textarea1)
                formData.append('contentTo', this.textarea2)
                this.$axios.post('/diff/updateDiff', formData).then(async (resp) => {
                // console.log(resp);
                if (resp === 'success'){
                    this.$message({
                    type: 'success',
                    message: '修改成功!'
                    })
                    await this.refreshTable(this.leftNode.id)
                } else {
                    this.$message({
                        type:'error',
                        message: '修改失败!'
                        })
                    }
                })
            }).catch((err) => {
                console.log(err)
                this.$message({
                type: 'info',
                message: '已取消修改'
                });
            });
        },
        removeYiwen () {
            if (this.multipleSelection.length === 0) {
                this.$message({
                    type: 'error',
                    message: '请先选择要删除的异文!',
                    duration: 700
                })
                return
            }
            this.$confirm('此操作将删除选中的异文, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                this.pageLoading = true
                try {
                    const parr = []
                    for (let i = 0; i < this.multipleSelection.length; i++) {
                        const formData = new FormData()
                        formData.append('diffId', this.multipleSelection[i].id)
                        const res = this.$axios.post('/diff/deleteDiff', formData).then(res => res)
                        parr.push(res)
                    }
                    await Promise.all(parr)
                } catch (error) {
                    console.log(error)
                } finally {
                    this.$message({
                        type: 'success',
                        message: '删除成功!'
                    })
                    this.pageLoading = false
                    await this.refreshTable(this.leftNode.id)
                }
            }).catch((err) => {
                console.log(err)
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },
        async labelYiwen () {
            const formData = new FormData()
            formData.append('diffId', this.diffId)
            const res = await this.$axios.post('/diff/GetHighlight', formData)
            if (res.id) {
                this.chooseFromJson = res.jsonFrom ? JSON.parse(res.jsonFrom) : {}
                this.chooseToJson = res.jsonTo ? JSON.parse(res.jsonTo) : {}
                console.log('json', res.jsonFrom ? JSON.parse(res.jsonFrom) : {}, res.jsonTo ? JSON.parse(res.jsonTo) : {});
            }
            
            console.log('label!', res);
            this.labelDialogVisible = true
        },
        changeLabelVisible (val) {
            this.labelDialogVisible = val
        },
        handleSizeChange (size) {
            this.size = size
        },
        handleCurrentPageChange (currentPage) {
            this.currentPage = currentPage
        },
        async importDiff2() {
            const res = await this.$axios.post('/code/Books')
            this.importForm.bookArr = res
            this.import2DialogVisible = true
        },
        async uploadExcel2() {
            if (this.chooseFromBook === '' || this.chooseToBook === '') {
                this.$message.error('请选择对应的书籍!')
                return
            }
            if (this.fileFileList == '') {
                this.$message.error('请选择对应的文件!')
                return
            }
            this.uploading2 = true
            const config = {
                headers: { "Content-Type": "multipart/form-data" }, //跟后端约定发送的数据类型为form-data对象类型
            }
            const formData = new FormData()
            formData.append('bookFrom', this.chooseFromBook)
            formData.append('bookTo', this.chooseToBook)
            formData.append('file', await this.obj2xlsx(this.fileFileList))

            try {
                const res = await this.$axios.post('/diff/addDiffByExcel', formData, config)
                if (res === 'success') {
                    this.$message({
                        type: 'success',
                        message: '导入成功!'
                    })
                } else {
                    alert(res)
                }
            } catch (err) {
                console.log(err)
            } finally {
                this.uploading2 = false
                this.import2DialogVisible = false
            }
        },
        // 显示导入异文的弹窗
        async importDiff() {
            const res = await this.$axios.post('/code/Books')
            this.importForm.bookArr = res
            this.importDialogVisible = true
        },
        /**
         * convert object to .xlsx binary file
         * @param { Object } obj uploaded file object
         */
        obj2xlsx(obj) {
            return new Promise((resolve, reject) => {
                const reader = new FileReader()
                // obj转base64
                reader.readAsDataURL(obj.raw)
                reader.onload = (ev) => {
                    const f = ev.target.result
                    // base64编码转成二进制
                    var arr = f.split(','), mime = arr[0].match(/:(.*?);/)[1],
                        bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
                    while (n--) {
                        u8arr[n] = bstr.charCodeAt(n);
                    }
                    // application/vnd.ms-excel
                    const file = new File([u8arr], 'aaa.xlsx', { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
                    resolve(file)
                }
            })
        },
        async uploadExcel() {
            if (this.chooseFromBook === '' || this.chooseToBook === '') {
                this.$message.error('请选择对应的书籍!')
                return
            }
            if (this.connectFileList == '' || this.toFileList == '' || this.fromFileList == '') {
                this.$message.error('请选择对应的文件!')
                return
            }
            this.uploading = true
            const config = {
                headers: { "Content-Type": "multipart/form-data" }, //跟后端约定发送的数据类型为form-data对象类型
            }
            const formData = new FormData()
            formData.append('bookFrom', this.chooseFromBook)
            formData.append('bookTo', this.chooseToBook)
            // console.log('test', await this.obj2xlsx(this.connectFileList), await this.obj2xlsx(this.toFileList), await this.obj2xlsx(this.fromFileList))
            formData.append('diffConnect', await this.obj2xlsx(this.connectFileList))
            formData.append('diffTo', await this.obj2xlsx(this.toFileList))
            formData.append('diffFrom', await this.obj2xlsx(this.fromFileList))

            const res = await this.$axios({
                method: 'post',
                url: '/diff/addDiffByExcel',
                data: formData,
                headers: config.headers
            })
            this.uploading = false
            if (res === 'success') {
                this.$message({
                    type: 'success',
                    message: '导入成功!'
                })
                this.importDialogVisible = false
            } else {
                // this.$message.error(res)
                alert(res)
            }
        },
        handleConnectChange(file, fileList) {
            if (file.raw.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet') {
                this.connectFileList = file
            } else {
                this.$message({
                    type: 'error',
                    message: '文件类型错误，请上传xlsx格式的文件！'
                })
                this.$refs.upload_excel_connect.clearFiles()
            }
        },
        // 移除文件
        handleConnectRemove(file, fileList) {
            this.connectFileList = ''
        },
        // 只允许同时存在一个文件
        handleConnectExceed (files) {
            this.$refs.upload_excel_connect.clearFiles()
            const file = files[0]
            this.$refs.upload_excel_connect.handleStart(file)
        },
        handleToChange(file, fileList) {
            if (file.raw.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet') {
                this.toFileList = file
            } else {
                this.$message({
                    type: 'error',
                    message: '文件类型错误，请上传xlsx格式的文件！'
                })
                this.$refs.upload_excel_to.clearFiles()
            }
        },
        handleToRemove(file, fileList) {
            this.toFileList = ''
        },
        handleToExceed (files) {
            this.$refs.upload_excel_to.clearFiles()
            const file = files[0]
            this.$refs.upload_excel_to.handleStart(file)
        },
        handleFromChange(file, fileList) {
            if (file.raw.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet') {
                this.fromFileList = file
            } else {
                this.$message({
                    type: 'error',
                    message: '文件类型错误，请上传xlsx格式的文件！'
                })
                this.$refs.upload_excel_from.clearFiles()
            }
        },
        handleFromRemove(file, fileList) {
            this.fromFileList = ''
        },
        handleFromExceed (files) {
            this.$refs.upload_excel_from.clearFiles()
            const file = files[0]
            this.$refs.upload_excel_from.handleStart(file)
        },
        handleFileChange(file, fileList) {
            if (file.raw.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet') {
                this.fileFileList = file
            } else {
                this.$message({
                    type: 'error',
                    message: '文件类型错误，请上传xlsx格式的文件！'
                })
                this.$refs.upload_excel_file.clearFiles()
            }
        },
        handleFileRemove() {
            this.fileFileList = ''
        },
        handleFileExceed(files) {
            this.$refs.upload_excel_file.clearFiles()
            const file = files[0]
            this.$refs.upload_excel_file.handleStart(file)
        },
        handleTestSuccess () {
            console.log('test success')
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
    }
}
</script>

<style lang="scss" scoped>
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
  .tree2 {
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
    // margin-left: calc((100vw - 580px)/2 - 200px);
}

.yiwen1 {
    height: calc(100vh - 340px);
    margin-bottom: 10px;
}

.main-dis {
    .dianji {
        margin-top: 0px;
        margin-bottom: 8px;
    }
    .dianji1 {
        margin-bottom: 8px;
    }
}

.upload-header {
    .upload-main {
        margin-left: 70%;
        margin-bottom: 10px;
    }
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
