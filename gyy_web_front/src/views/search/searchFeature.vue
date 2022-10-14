<template>
  <div>
    <div class="book-introduction content-wrap">
      <div class="nav-wrap fl">
        <ul>
          <li>
            <a class="lang" :class="{ act: isAct1 }" @click="clickContent">内容搜索</a>
          </li>
          <li>
            <a class="lang" :class="{ act: isAct2 }" @click="clickDiff">异文搜索</a>
          </li>
        </ul>
      </div>
    </div>
    <div class="content" v-show="isAct1">
      <super-search />
    </div>
    <div class="content" v-show="isAct2">
      <connect-diff />
    </div>
  </div>
</template>

<script>
import SuperSearch from './SuperSearch.vue'
import ConnectDiff from './ConnectDiff.vue'

export default {
  name: 'searchFeature',
  components: {
    SuperSearch,
    ConnectDiff
  },
  data () {
    return {
      isAct1: true,
      isAct2: false,
      searchLoading: false,
      showOutcomes: false,
      content: [],
      totalItems: 0,
      outcomesLoading: false
    }
  },
  methods: {
    clickContent () {
      this.isAct1 = true
      this.isAct2 = false
    },
    clickDiff () {
      this.isAct1 = false // 切换到异文搜索
      this.isAct2 = true
    }
  },
  mounted () {
  }
}
</script>

<style lang="scss" scoped>
$my-red: #ed4259;

.act {
  color: $my-red !important;
  margin-bottom: -1px;
  border-bottom: 2px solid $my-red;
  padding-bottom: 2.5px;
}

.content {
    margin-top: 10px;
}

.content-wrap {
  line-height: 16px;
  border-bottom: 1px solid #e6e6e6;
}

.book-introduction {
  display: block;
  height: 41px;
  width: 990px;
  margin: -10px auto 10px auto;
  padding-left: calc(100vw - 100%);

  .fl {
    float: left;
  }

  li, ul, ol {
    list-style: none outside none;
    font: 18px/40px FZZCYSK;
    float: left;
    overflow: hidden;
    height: 40px;
    padding: 0 21px;
    text-align: center;
    margin: 0;

    a {
      display: block;
      overflow: hidden;
      max-height: 36px;
      color: #a6a6a6;
      text-decoration: none;
      outline: 0;
    }
  }

  ul {
    padding-left: 0;
  }

  .lang {
    font-family: FZZCYSK;
    font-weight: 400;
    transition: color .3s,background-color .3s;
    &:hover {
      color: $my-red;
      cursor: pointer;
    }
  }
}
</style>
