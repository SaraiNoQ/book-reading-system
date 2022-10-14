<template>
  <div class="cata-wrapper" :style="popupTheme">
    <div class="title">
      目录
    </div>
    <div
      class="data-wrapper"
      ref="cataData"
      :class="{ night: isNight, day: !isNight }"
    >
      <div>
        <div class="catalog-list-wrap" v-for="(note, index) in catalog" :key="index">
          <!-- 卷名 -->
          <h2 class="catalog-juanming" :class="{ 'firsth': isFirst(index), 'isRed': isRed(note) }" @click="gotoChapter(note)">{{note.chapter}}</h2>
          <!-- 卷列表 -->
          <div class="volume-list">
            <div
              class="volume-content"
              :class="{ isRed: isRed(item) }"
              v-for="(item, idx) in note.cataLog"
              :key="idx"
              @click="gotoChapter(item)"
              :ref="'cata_' + item.id"
            >{{item.chapter}}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// import jump from '../../plugins/jump'
import config from '../../plugins/config'
import '../../assets/fonts/popfont.css'
export default {
  name: 'PopCata',
  data () {
    return {
      isNight: this.$store.state.config.theme === 6
      // index: this.$store.state.readingBook.index
    }
  },
  computed: {
    catalog () {
      // console.log(this.$store.state.catalogArr[0])
      return this.$store.state.catalogArr
    },
    popCataVisible () {
      return this.$store.state.popCataVisible
    },
    theme () {
      return this.$store.state.config.theme
    },
    popupTheme () {
      return {
        background: config.themes[this.theme].popup
      }
    }
  },
  mounted () {
  },
  watch: {
    theme (theme) {
      if (theme === 6) {
        this.isNight = true
      } else {
        this.isNight = false
      }
    },
    popCataVisible () {
      this.$nextTick(function () {
        // const index = this.$store.state.readingBook.index
        // const wrapper = this.$refs.cataData
        // jump(this.$refs.cata[index], { container: wrapper, duration: 0 })
        // 如果找到isRed属性的元素，就将其放置到可视区域最上方
        this.$el.querySelector('.isRed') && this.$el.querySelector('.isRed').scrollIntoView()
      })
    }
  },
  methods: {
    isFirst (index) {
      // console.log(index)
      // 第零个head的margin-top为0
      return index === 0
    },
    isRed (item) {
      return item.chapter === this.$store.state.currentChapter
    },
    gotoChapter (item) {
      // this.index = this.catalog.indexOf(note)
      // this.$store.commit('setPopCataVisible', false)
      // this.$store.commit('setContentLoading', true)
      // this.$emit('getContent', this.index)
      this.$store.commit('setCurrentChapter', item.chapter)
      window.sessionStorage.setItem('store', JSON.stringify(this.$store.state))
      this.$emit('getChapContent', item.chapter)
    }
  }
}
</script>

<style lang="scss" scoped>
.cata-wrapper {
  margin: -16px;
  padding: 18px 0 24px 25px;

  /**滚动条的宽度*/
  ::-webkit-scrollbar {
      width: 12px !important;/*竖向*/
      // height: 100px !important;/*横向*/
  }

  /*滚动条的滑块*/
  ::-webkit-scrollbar-thumb {
      // background-color: rgb(244, 207, 152);
      border-radius: 0px;
      cursor: pointer;
  }

  // background: #ede7da url('../assets/imgs/themes/popup_1.png') repeat;
  .title {
    font-size: 18px;
    font-weight: 400;
    font-family: FZZCYSK;
    margin: 0 0 20px 0;
    color: #ed4259;
    width: fit-content;
    border-bottom: 1px solid #ed4259;
  }

  .data-wrapper {
    height: 300px;
    overflow: auto;

    .catalog-list-wrap {
        .isRed {
          color: #ed4259;
        }
      .firsth {
        margin-top: 0px;
      }
      h2 {
        position: relative;
        z-index: 2;
        margin-bottom: 0px;
        border-bottom: 1px solid #e5e5e5;
      }

      .catalog-juanming {
        cursor: pointer;
        &:hover {
            color: #ed4259;
        }
      }

      .volume-list {

        display: grid;
        grid-template-columns:repeat(2,50%);

        .volume-content {
          padding-right: 20px;
          border-bottom: 1px solid #EBEEF5;
          cursor: pointer;
          font: 16px / 40px PingFangSC-Regular, HelveticaNeue-Light, 'Helvetica Neue Light', 'Microsoft YaHei', sans-serif;
          display:block;
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
          &:hover {
            color: #ed4259;
          }
        }
      }
    }

    .cata { // 废弃

      .cata-head {
        margin: 0px 0px 0px 0px;
        // border-bottom: 1px solid #EBEEF5
      }
      .selected {
        color: #EB4259;
      }

      .log {
      //   display: flex;
      // flex-direction: row;
      // flex-wrap: wrap;
      // justify-content: space-between;
          width: 50%;
          height: 40px;
          cursor: pointer;
          float: left;
          font: 16px / 40px PingFangSC-Regular, HelveticaNeue-Light, 'Helvetica Neue Light', 'Microsoft YaHei', sans-serif;
        .log-text {
          margin-right: 26px;
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
          &:hover {
            color: red;
          }
        }
      }
    }
  }

  .night {
    ::v-deep .log {
      border-bottom: 1px solid #666;
    }
  }

  .day {
    ::v-deep .log {
      border-bottom: 1px solid #f2f2f2;
    }
  }
}
</style>
