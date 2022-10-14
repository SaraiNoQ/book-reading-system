<!-- 书籍目录组件 -->
<template>
<div class="catalog">
  <div class="catalogborder">
    <h2>
      目录
      <i>·</i>
      <span class="head-span">共X卷XX章</span>
    </h2>
  </div>
</div>
</template>

<script>
export default {
  name: 'Catalog',
  data () {
    return {
      cataLog: [{
        grade: 1,
        val: '卷一 绪论',
        cataLog: [{ grade: 2, val: '论大医习业第一' }, { grade: 2, val: '论大医习业第二' }, { grade: 2, val: '求子第五' }, { grade: 2, val: '妊娠恶阻第六' }, { grade: 2, val: '求子第一' }, { grade: 2, val: '妊娠恶阻第二' }]
      }, {
        grade: 1,
        val: '卷二 妇人方上',
        cataLog: [{ grade: 2, val: '求子第一' }, { grade: 2, val: '妊娠恶阻第二' }, { grade: 2, val: '养胎第三' }, { grade: 2, val: '第四第五' }]
      }, {
        grade: 1,
        val: '卷三 妇人方下',
        cataLog: [{ grade: 2, val: '求子第一' }, { grade: 2, val: '妊娠恶阻第二' }, { grade: 2, val: '养胎第三' }, { grade: 2, val: '第四第五' }]
      }]
    }
  },
  // 生命周期 - 创建完成（访问当前this实例）
  created () {
  },
  // 生命周期 - 挂载完成（访问DOM元素）
  mounted () {

  }
}
</script>
<style lang="scss">
/* @import url(); 引入css类 */
$head-baseline: #666;
.catalog {
  display: flex;
  justify-content: center;

  .catalogborder {
    .volumn {
      li, ol, ul {
        list-style: none outside none;
      }

      li {
        font: 14px/40px PingFangSC-Regular,'-apple-system',Simsun;
        float: left;
        overflow: hidden;
        width: 290px;
        height: 40px;
        padding-right: 60px;
        border-bottom: 1px solid #ebebeb;

        h2 h3 h4 {
          font-size: 14px;
          float: left;
          overflow: hidden;
          max-width: 240px;
          white-space: nowrap;
          text-overflow: ellipsis;
        }
      }

      .cf {

        .book-name {
          color:red;
          font-weight: 400;
          display: inline-block;
          vertical-align: middle;
          color: #262626;
        }

        &:after {
          display: table;
          content: '';
        }
        &:before {
          display: table;
          content: '';
        }
      }
    }
    // display: flex;
    // align-items: baseline;
    width: 990px;
    h2 {
      // margin-right: 8px;
      border-bottom: 1px solid $head-baseline;
      padding-bottom: 8px;

      .head-span {
        margin-left: 5px;
        color: rgb(170, 170, 170);
        font-size: 22px;
      }
    }
    h3 {
      color: rgb(170, 170, 170);
      border-bottom: 1px solid $head-baseline;
    }
  }
}
</style>
