<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
export default {
  beforeCreate () {
    console.log('process', process.env.NODE_ENV)
  }
}
</script>

<style>
  /**修改全局的滚动条*/
  /**滚动条的宽度*/
  ::-webkit-scrollbar {
      width: 5px;/*竖向*/
      height: 5px;/*横向*/
  }

  /*滚动条的滑块*/
  ::-webkit-scrollbar-thumb {
      background-color: rgb(244, 207, 152);
      border-radius: 3px;
      cursor: pointer;
  }
</style>
