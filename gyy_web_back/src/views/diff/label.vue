<template>
    <div ref="label">
        <el-dialog
        title="异文标记"
        :visible.sync="labelDialogVisible"
        :before-close="beforeClose"
        destroy-on-close
        width="80%"
        >
          <h4 style="margin: 0px; color: #ed4259;">提示：鼠标可选中需要标记的内容，右键单击已有异文标识可删除。退出前需要保存</h4>
          <div class="label-container">
              <div ref="annotator" class="label-anno">
                  <h2>原文</h2>
              </div>
              <div ref="diffanno" class="label-anno">
                  <h2>异文</h2>
              </div>
          </div>
          <span slot="footer" class="dialog-footer">
              <el-button type="danger" @click="beforeClose">取 消</el-button>
              <el-button type="success" @click="beforeSave">保 存</el-button>
          </span>
        </el-dialog>
    </div>
</template>

<script>
import { Action, Annotator } from 'poplar-annotation'

export default {
    name: 'Label',
    props: {
        chooseId: {
            type: String,
            default: '',
        },
        labelFrom: {
          type: String,
          default: '',
        },
        labelTo: {
          type: String,
          default: '',
        },
        labelDialogVisible: {
          type: Boolean,
          default: false,
        },
        labelFromData: {
          type: Object,
          default: () => {
            return {
              content: '',
              labelCategories: [
                {
                  id: 0,
                  text: '异文',
                  color: '#eac0a2',
                  borderColor: '#a38671'
                }
              ],
              labels: [],
              connectionCategories: [],
              connections: []
            }
          }
        },
        labelToData: {
          type: Object,
          default: () => {
            return {
              content: '',
              labelCategories: [
                {
                  id: 0,
                  text: '异文',
                  color: '#eac0a2',
                  borderColor: '#a38671'
                }
              ],
              labels: [],
              connectionCategories: [],
              connections: []
            }
          }
        }
    },
    data () {
      return {
        labelV: this.labelDialogVisible,
        diffId: '',
        labelFromDataData: {
          content: '',
          labelCategories: [
            {
              id: 0,
              text: '异文',
              color: '#eac0a2',
              borderColor: '#a38671'
            }
          ],
          labels: [],
          connectionCategories: [],
          connections: []
        },
        labelToDataData: {
          content: '',
          labelCategories: [
            {
              id: 0,
              text: '异文',
              color: '#eac0a2',
              borderColor: '#a38671'
            }
          ],
          labels: [],
          connectionCategories: [],
          connections: []
        },
        example1: {},
        example2: {},
        saveFlag: 0,
      }
    },
    mounted () {
        
    },
    methods: {
        beforeClose () {
          if (this.saveFlag) {
            // 销毁变量
            this.saveFlag = 0
            this.$emit('on-before-close', false)
            this.labelFromDataData = {
                content: '',
                labelCategories: [
                  {
                    id: 0,
                    text: '异文',
                    color: '#eac0a2',
                    borderColor: '#a38671'
                  }
                ],
                labels: [],
                connectionCategories: [],
                connections: []
              }
              this.labelToDataData = {
                content: '',
                labelCategories: [
                  {
                    id: 0,
                    text: '异文',
                    color: '#eac0a2',
                    borderColor: '#a38671'
                  }
                ],
                labels: [],
                connectionCategories: [],
                connections: []
              }
              return
          }
          this.$confirm('确认关闭？如果关闭将不会保存当前编辑的内容。')
            .then(_ => {
              this.$emit('on-before-close', false)
              this.labelFromDataData = {
                content: '',
                labelCategories: [
                  {
                    id: 0,
                    text: '异文',
                    color: '#eac0a2',
                    borderColor: '#a38671'
                  }
                ],
                labels: [],
                connectionCategories: [],
                connections: []
              }
              this.labelToDataData = {
                content: '',
                labelCategories: [
                  {
                    id: 0,
                    text: '异文',
                    color: '#eac0a2',
                    borderColor: '#a38671'
                  }
                ],
                labels: [],
                connectionCategories: [],
                connections: []
              }
            })
            .catch(_ => {
              this.$message({
                  type: 'info',
                  message: '已取消关闭'
              });
            });
        },
        beforeSave () {
          this.$confirm('确认保存？')
            .then(async _ => {
              // TODO: 保存
              console.log('save-label', this.example1.store.json, this.example2.store.json);
              const formData = new FormData()
              formData.append('diffId', this.diffId)
              formData.append('jsonFrom', JSON.stringify(this.example1.store.json))
              formData.append('jsonTo', JSON.stringify(this.example2.store.json))
              const res = await this.$axios.post('/diff/updateHighlight', formData)
              console.log('save-res', res);
              if (res === 'success') {
                this.$message.success('保存成功!')
                this.saveFlag = 1
              } else {
                this.$message.error('保存失败!')
              }
            })
            .catch(async _ => {
              this.$message({
                type: 'info',
                message: '已取消保存'
              });
            });
        },
    },
    computed: {
        visible: {
          get () {
            // console.log('computed labelV', this.labelDialogVisible);
            return this.labelDialogVisible
          }
        },
        labelFromCom: {
          get () {
            // console.log('computed labelFromData', this.labelFromData.content);
            this.labelFromDataData.content = this.labelFrom
            return this.labelFrom
          }
        },
        labelToCom: {
          get () {
            // console.log('computed labelFromData', this.labelToData.content);
            this.labelToDataData.content = this.labelTo
            return this.labelTo
          }
        },
        labelFromDataCom: {
          get () {
            // console.log('testAAA', this.labelToData);
            if (this.labelFromData.content) {
              // console.log('testEEE');
              return this.labelFromDataData = this.labelFromData
            }
          }
        },
        labelToDataCom: {
          get () {
            // console.log('testDDD');
            if (this.labelToData.content) {
              // console.log('testCCC', this.labelData)
              return this.labelToDataData  = this.labelToData
            }
          }
        },
        diffIdCom: {
          get () {
            return this.diffId = this.chooseId
          }
        }
    },
    watch: {
      visible (val) {
        if (val) {
          // dialog的组件为懒加载，需要在组件初始化完成后才能调用
          this.$nextTick(()=> {
            console.log('refs', this.labelFromDataData, this.labelToDataData)
            const jsonFrom = JSON.stringify(this.labelFromDataData)
            const jsonTo = JSON.stringify(this.labelToDataData)
            this.example1 = new Annotator(jsonFrom, this.$refs.annotator)
            this.example2 = new Annotator(jsonTo, this.$refs.diffanno)
            this.example1.on('textSelected', (startIndex, endIndex) => {
              console.log('selected', this.labelFromDataData.content.slice(startIndex, endIndex))
              // 这里可以控制添加的标签的类型
              this.example1.applyAction(Action.Label.Create(0, startIndex, endIndex))
            })
            this.example1.on('labelRightClicked', (id) => {
              console.log('right clicked', id)
              // 这里可以删除标签
              this.example1.applyAction(Action.Label.Delete(id))
            })
            this.example1.on('labelClicked', (id) => {
              console.log('labelId', id)
            })
            this.example1.on('twoLabelsClicked', (first, second) => {
              this.example1.applyAction(Action.Connection.Create(0, first, second))
            })
            this.example2.on('textSelected', (startIndex, endIndex) => {
              console.log('selected', this.labelFromDataData.content.slice(startIndex, endIndex))
              // 这里可以控制添加的标签的类型
              this.example2.applyAction(Action.Label.Create(0, startIndex, endIndex))
            })
            this.example2.on('labelRightClicked', (id) => {
              console.log('right clicked', id)
              // 这里可以删除标签
              this.example2.applyAction(Action.Label.Delete(id))
            })
            this.example2.on('labelClicked', (id) => {
              console.log('labelId', id)
            })
            this.example2.on('twoLabelsClicked', (first, second) => {
              this.example2.applyAction(Action.Connection.Create(0, first, second))
            })

            console.log('example', this.example1, this.example2)
          })
        }
      },
      labelFromCom (val) {
        console.log('watch labelFromCom', val)
      },
      labelToCom (val) {
        console.log('watch labelToCom', val)
      },
      diffIdCom (val) {
        console.log('watch diffIdCom', val)
      },
      labelFromDataCom (val) {
        console.log('watch labelFromDataCom', val)
      },
      labelToDataCom (val) {
        console.log('watch labelToDataCom', val)
      }
    }
}
</script>

<style lang="scss" scoped>
::v-deep .el-dialog__body {
    padding-top: 0;
}
.label-container {
    display: flex;
    justify-content: space-between
}
::v-deep .label-anno {
    width: 45%;
    > svg {
      display: block;
      width: 100%;
      font-size: 20px;
    }
    h2 {
        text-align: center;
    }
}
</style>