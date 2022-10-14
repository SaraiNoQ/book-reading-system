<template>
<div class>


<div>
  <div class="head2" style="margin-bottom: 20px;height: 60px">
    <el-row style="">
      <el-button type="success" icon="el-icon-video-pause" circle></el-button>
      <el-button icon="el-icon-search" circle></el-button>
      <el-button type="danger" icon="el-icon-delete" circle></el-button>
      <el-button type="primary" icon="el-icon-refresh-left" circle></el-button>

    </el-row>
  </div>
  昵称
<el-input style="width:150px; margin-left: 40px"  prefix-icon="el-icon-search" placeholder="请输入内容"></el-input>
  用户名
  <el-input style="width:150px" prefix-icon="el-icon-search" placeholder="请输入内容"></el-input>
  E-Mail
  <el-input style="width:150px" prefix-icon="el-icon-search" placeholder="请输入内容"></el-input>
</div>
  <div>
    <el-table
        :data="tableData"
        border
        stripe
        style="width: 100%">
      <el-table-column
          prop="id"
          label="序号"
          align="left"
          width="200">
      </el-table-column>
      <el-table-column
          prop="nickname"
          label="昵称"
          align="left"
          width="200">
      </el-table-column>
      <el-table-column
          prop="username"
          label="用户名"
          align="left"
          width="200px">
      </el-table-column>
      <el-table-column
          prop="email"
          label="email"
          align="left"
          width="219px">
      </el-table-column>
      <el-table-column
          prop="usertype"
          label="用户类型"
          align="left"
          width="200px">
      </el-table-column>
    </el-table>
    <div style="display: flex;justify-content: center">
      <el-pagination
          background
          layout="sizes,prev, pager, next, jumper, ->, total"
          :total="100">
      </el-pagination>
    </div>
  </div>
</div>
</template>

<script>
export default {
  name: "User",
  data(){
    return{
      tableData: [],
      total:0,
      page:1,
      size:10
    }
  },
  methods:{
    initTableData(){
     /* this.getRequest('/user/getUser/1/1').then(resp=>{
        if(resp)
        {
          console.log(resp);
          this.tableData=resp.data;
          this.total=resp.total;
        }
      })*/
      this.$axios.get('/api/user/getUser/1/1').then(resp=>{
        console.log(resp);
        if(resp){
          this.tableData=resp.data;
          console.log(JSON.stringify(resp.data));
        }
      })
    }
  },
  created() {
    this.initTableData()
  }

}
</script>

<style scoped>
.head2{
  background-color: #faebd6;
}
</style>