<template>
  <div style="padding-right: 15px; text-align: justify;">
    <p
      v-for="(item, index) in carray"
      :key="item + index"
      v-show="!disDiffCon"
    >
      <span
        v-for="(e, index) in item"
        :key="e + index"
        :ref="'p_' + e"
        :style="font"
        :class="{ lines: focusStyle, colorred: isClick(e) }"
        @click="sendCon(e, $event)"
        @mouseover="sendAxis(e)"
        @mouseout="sendOutAxis(e)"
      >{{e}}</span>
    </p>
    <span v-for="(item, index) in label" :key="item + index" v-show="disDiffCon">
      <span
        :ref="'p_' + item"
        :style="font"
        :class="{ lines: focusStyle }"
        @click="sendCon(item, $event)"
        @mouseover="sendAxis(item)"
        @mouseout="sendOutAxis(item)"
        v-html="item"
    ></span></span>
    <!-- <span
      v-for="(item, index) in carray"
      :key="item + index"
      :ref="'p_'+item">
      <span
        v-if="item.indexOf('\r\n') !== -1"
        :style="font"
        :class="{ lines: focusStyle }"
        @click="sendCon(item, $event)"
        @mouseover="sendAxis(item)"
        @mouseout="sendOutAxis(item)"
      >&nbsp;&nbsp;{{item.replace('\r\n', '')}}
      </span>
      <span
        v-else
        :style="font"
        :class="{ lines: focusStyle }"
        @click="sendCon(item, $event)"
        @mouseover="sendAxis(item)"
        @mouseout="sendOutAxis(item)"
      ><br/>&nbsp;&nbsp;{{item}}</span>
    </span> -->
    <!-- <span v-for="(item, index) in carray"
      v-show="item !== '\r\n'"
      :key="item + index"
      :style="font"
      :class="{ lines: focusStyle }"
      @click="sendCon(item, $event)"
      @mouseover="sendAxis(item)"
      @mouseout="sendOutAxis(item)"
      :ref="'p_'+item"
    >&nbsp;&nbsp;{{item}}</span> -->
    <!-- <span v-for="(item) in carray"
      v-show="item === '\r\n'"
      :key="item +index"
      :style="font"
      class="f-line"
      @click="sendCon(item, $event)"
      @mouseover="sendAxis(item)"
      @mouseout="sendOutAxis(item)"
      :ref="'p_'+item"
    ><br/>
    </span> -->
  </div>
</template>

<script>
import config from '../../plugins/config'
export default {
  name: 'pcontent',
  data () {
    return {
      clickItem: '',
      isActive: false
    }
  },
  props: ['carray', 'label', 'disDiffCon'],
  // mounted () {
  //   this.$nextTick(function () {
  //     this.carray.forEach(e => {
  //       if (e.indexOf('\r\n') !== -1) {
  //         this.$refs['p_' + e].innerHTML = '<br/>' + this.$refs['p_' + e].innerHTML
  //         console.log('!')
  //       }
  //       console.log('ref', this.$refs['p_' + e])
  //     })
  //   })
  // },
  // render () {
  //   const { fontFamily, fontSize } = this
  //   const style = fontFamily
  //   style.fontSize = fontSize
  //   if (this.show) {
  //     return (
  //       <div>
  //         {this.carray.map(a => {
  //           return <p style={style} class="f-line">&nbsp;&nbsp;{a}</p>
  //         })}
  //       </div>
  //     )
  //   } else {
  //     return <div />
  //   }
  // },
  computed: {
    show () {
      return this.$store.state.showContent
    },
    fontFamily () {
      return config.fonts[this.$store.state.config.font]
    },
    fontSize () {
      return this.$store.state.config.fontSize + 'px'
    },
    font () {
      return { fontFamily: this.fontFamily.fontFamily, fontSize: this.fontSize }
    },
    focusStyle () {
      // console.log('thisconfig', this.$store.state.config.readWidth)
      const w = window.screen.width * 0.86 || 1280
      return this.$store.state.config.readWidth === w
    }
  },
  watch: {
    fontSize () {
      const that = this
      that.$store.commit('setShowContent', false)
      this.$nextTick(() => {
        that.$store.commit('setShowContent', true)
      })
      // console.log(this.$store.state.config.fontSize)
    }
    // isActive (val) {
    //   console.log('change val', val)
    // }
  },
  methods: {
    sendCon (item, e) {
      this.toggleClick()
      // console.log('item', item.replaceAll(/<\/?.+?\/?>/g, ''))
      const clickE = item.replaceAll(/<\/?.+?\/?>/g, '')
      const pos = this.$refs['p_' + item][0].offsetTop
      // 使被点击的文字变色
      this.clickItem = clickE
      this.$emit('getWenContent', clickE, pos)
      this.$emit('getOriginal', clickE)
      this.$emit('clickToImg', clickE)
    },
    sendAxis (e) {
      this.$emit('getMouseOver', e.replaceAll(/<\/?.+?\/?>/g, ''))
    },
    sendOutAxis () {
      this.$emit('getMouseOut')
    },
    isClick (item) {
      console.log('click item', item)
      if (item.replaceAll(/<\/?.+?\/?>/g, '') === this.clickItem) {
        return true
      } else {
        return false
      }
    },
    toggleClick () {
      this.isActive = !this.isActive
    }
  }
}
</script>

<style lang="scss" scoped>
p {
  display: block;
  word-wrap: break-word;
  word-break: break-all;
  margin-top: 0px;
  margin-bottom: 0px;
  text-indent:2em;
}
.lines {
  margin: 0;
  cursor: pointer;
  &:hover {
    text-decoration:underline;
    color: red;
  }
}
.colorred {
  color: red;
}
.colorblcak {
  color: #262626;
}
</style>
