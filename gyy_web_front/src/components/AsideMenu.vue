<template>
<div>
  <el-menu router mode="horizontal" background-color="#f5f5f5">
    <el-submenu index="1"
     v-for="(item, index) in this.$router.options.routes"
     :key="index"
     v-show="!item.hidden"
    >
      <template slot="title">
        <i class="el-icon-s-shop"></i>
        <span>{{item.name}}</span>
      </template>
      <el-menu-item :index="children.path"
      v-for="(children, indexj) in item.children"
      :key="indexj"
      class="menu"
      v-show="!children.hidden"
      >
        <i class="el-icon-goods" v-if="children.name=='书库'"></i>
        <i class="el-icon-search" v-if="children.name=='搜索'"></i>
        <span>{{children.name}}</span>
      </el-menu-item>
    </el-submenu>
  </el-menu>
</div>
</template>

<style lang="scss">
$icon-color: #ffc21a;
$top-tab-color: #f5f5f5;
.top-tab {
  padding-left: 25px;
  padding-right: 228px;
  background: $top-tab-color;
}
.el-menu--horizontal>.el-submenu {
  .el-submenu__title {
    border-bottom: 0px solid transparent;
    height: 30px;
    line-height: 30px;
  }
}
.el-menu--horizontal>.el-submenu.is-active .el-submenu__title {
  border-bottom: 2px solid transparent;
}
.el-submenu__title {
  .el-icon-s-shop {
    color: $icon-color;
  }
  font-size: 15px;
  font-family: "Arial","Microsoft YaHei","黑体","宋体",sans-serif;
}
.menu {
  font-size: 15px;
  .el-icon-goods {
    margin-right: 2px;
    color: $icon-color;
  }
  .el-icon-search {
    margin-right: 2px;
    color: $icon-color;
  }
}
</style>

<script>
export default {
  data () {
    return {
    }
  },
  methods: {

  }
}
</script>
