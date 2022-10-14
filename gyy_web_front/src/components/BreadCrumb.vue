<template>
    <div class="breadcrumb">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item
         v-for="(breadCrumbItem, index) in breadCrumbList[0].meta"
         :key="index"
        >{{ breadCrumbItem }}
        </el-breadcrumb-item>
        <el-breadcrumb-item>{{this.$store.state.currentBookName}}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
</template>

<script>
export default {
  mounted () {
    // console.log(this.$router.history.current.matched[0])
  },
  data () {
    return {
    }
  },
  computed: {
    breadCrumbList () {
      return this.$router.history.current.matched
    }
  },
  methods: {
    toNavigation (item) {
      console.log(item)
    }
  }
}
</script>

<style lang="scss">
.breadcrumb {
  margin: 20px 225px 30px 225px;
  padding-right: 20px;
  color:#606266;
}
</style>
