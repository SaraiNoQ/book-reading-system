<template>
  <div class="searchBox">
     <div class="searchBoxs">
        <el-input
          placeholder="请输入关键字"
          @keyup.enter.native="searchKeyup"
          v-model.trim="searchValue"
          class="input-with-select"
        >
        <el-button slot="append" icon="el-icon-close" @click="searchClose()"></el-button>
        </el-input>
        <el-button icon="el-icon-search" @click="search()" class="refreshBtn"></el-button>
      </div>
  </div>
</template>

<script>
export default {
  props: {
    nameOption: [Array, Object]
  },
  data () {
    return {
      nameValue: '',
      searchValue: ''
    }
  },
  methods: {
    search () {
      const filters = {}
      filters.name = this.nameValue // 'name' || 'id'
      filters.value = this.searchValue // 具体值
      if (filters.value === '') {
        this.$message.warning('查询条件不能为空！')
        return null
      } else {
        this.$emit('searchEvent', filters)
      }
    },
    searchKeyup () {
      const filters = {}
      this.statusValue = ''
      filters.name = this.nameValue // 'name' || 'id'
      filters.value = this.searchValue // 具体值
      if (filters.value === '') {
        this.$message.warning('查询条件不能为空！')
        return null
      } else {
        this.$emit('searchEvent', filters)
      }
    },
    searchClose () {
      this.searchValue = ''
    }
  },
  computed: {
  },
  created () {
  },
  mounted () {
  },
  watch: {
  }
}
</script>

<style lang="scss" scoped="scoped">
.searchBox {
  .refreshBtn {
    vertical-align: middle;
    padding: 0;
    width: 70px;
    height: 43px;
    margin-left: 10px;
    background: #3c72ca;
    color: #fff;
  }
  ::v-deep .el-select .el-input {
    width: 190px;
  }
  ::v-deep .el-select-dropdown {
    width: 270px !important;
  }
}
.btntext {
  font-size: 12px;
  line-height: 3;
}
.btnBox {
  text-align: left;
  button {
    font-size: 12px;
    padding: 8px 20px;
    color: #666666;
    background: #f0f2f7;
    border: 0px solid #f0f2f7;
    white-space: nowrap;
    cursor: pointer;
    text-align: center;
    box-sizing: border-box;
    outline: 0;
    margin: 0;
    font-weight: 500;
    border-radius: 0px;
    margin-right: 10px;
  }
  .active {
    border: 1px solid #56d6c4;
    background: #56d6c4;
    color: #ffffff;
  }
}
</style>
<style lang="scss">
.searchBox {
  .el-input__inner {
    height: 30px;
  }
  .searchBoxs {
    width: 400px;
    min-width: 380px;
    height: 48px;
    padding-right: 10px;
    text-align: right;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    .el-input-group > .el-input__inner {
      vertical-align: baseline;
    }
    .input-with-select {
      .el-select {
        width: 110px;
        .el-input {
          width: 110px;
          border: none;
        }
        .el-select__caret {
          color: #000;
        }
      }
      .el-input-group__append {
        background: #fff;
        border: 0px;

        .el-button {
          vertical-align: middle;
          padding: 0;
          width: 30px;
          height: 30px;
        }
      }
    }
    .el-input-group__prepend {
      background: #fff;
      border: none;
      position: relative;
      &::before {
        position: absolute;
        top: 8px;
        right: 0px;
        width: 1px;
        height: 16px;
        content: '';
        background: #bebebe;
      }
    }
    .input-with-select > .el-input__inner {
      width: 260px;
      border: none;
      background: #fff;
    }
    .el-select .el-input.is-focus .el-input__inner {
      border-color: transparent;
    }
  }
  .el-select {
    width: 150px;
    .el-input {
      width: 150px;
    }
  }
  .el-input {
    width: 300px;
    height: 42px;
    border: 0px solid #e5e5e5;
    box-sizing: border-box;
    &__inner {
      height: 42px;
      width: 200px;
    }
  }
}
#input-select-place {
  text-align: center;
  color: #000;
}
</style>
