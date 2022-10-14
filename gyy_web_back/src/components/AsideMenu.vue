<template>
<div>
  <el-menu router>
    <el-submenu
      :index="item.name"
      v-for="(item, index) in this.$router.options.routes"
      :key="index"
      v-show="!item.hidden">
      <template slot="title">
        <span>{{ item.name }}</span>
      </template>
        <el-menu-item
          :index="children.path"
          v-for="(children, indexj) in item.children"
          :key="indexj">
          <span>{{ children.name }}</span>
        </el-menu-item>
    </el-submenu>
  </el-menu>


</div>
</template>

<script>
export default {
  name: "AsideMenu"
}
</script>

<style scoped>

</style>