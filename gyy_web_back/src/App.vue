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

<style lang="scss">
  /**修改全局的滚动条*/
  /**滚动条的宽度*/
  // *::-webkit-scrollbar {
  //     width: 5px;/*竖向*/
  //     height: 5px;/*横向*/
  // }

  // /*滚动条的滑块*/
  // *::-webkit-scrollbar-thumb {
  //     background-color: rgb(244, 207, 152);
  //     border-radius: 3px;
  //     cursor: pointer;
  // }

  *::-webkit-scrollbar {
    width: 10px;
    height: 9px;
  }
  //滚动条的轨道的两端按钮 此处隐藏
  *::-webkit-scrollbar-button {
    width: 0px;
    height: 0px;
    display: none;
  }
  //边角，即垂直滚动条和水平滚动条相交的地方
  *::-webkit-scrollbar-corner {
    background-color: transparent;
  }
  //
  *::-webkit-scrollbar-thumb {
    border: 3px solid rgba(244, 207, 152, 0);
    border-style: dashed;
    border-radius: 4px;
    background-clip: padding-box;
    background-color: rgba(244, 207, 152, 0.40);
    cursor: pointer;
  }
  //滚动条里面的小方块，能向上向下移动(或向左向右移动) 指上去的时候显示border和改变颜色造成视觉上的加粗变色
  *::-webkit-scrollbar-thumb:hover{
    border: 2px solid rgba(244, 207, 152, 0.7);
    background-color: rgba(244, 207, 152, 0.70);
  }
</style>
