<template>
<div>
  <el-button @click="test">测试</el-button>
</div>
</template>

<script>
export default {
  name: "Test2",
  methods: {
    test () {
      this.$axios.get('/book/bookContent/002002').then(res => {
        console.log('test', res);
      })
    }
  }
}
</script>

<style scoped>

</style>