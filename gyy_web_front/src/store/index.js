import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    userName: '',
    loginState: false,
    connectStatus: '正在连接后端服务器……',
    connectType: '',
    newConnect: true,
    shelf: [],
    catalog: '',
    catalogArr: [],
    chapterArr: [],
    chapterId: '',
    readingBook: {},
    diffbook: [],
    popCataVisible: false,
    contentLoading: true,
    showContent: true,
    config: {
      theme: 0,
      font: 0,
      fontSize: 18,
      readWidth: 800
    },
    readSettingsVisible: false,
    currentPage: 0,
    currentBook: 0,
    currentBookName: '',
    currentChapter: '',
    clickNum: 0,
    // 快速搜索
    searchContent: [],
    searchNums: 0,
    searchKeyword: '',
    // 强制跳转到高级搜索携带的关键词
    supterKeyword: '',
    userInfo: {},
    // 存放图书信息
    shopBookInfo: []
  },
  mutations: {
    setUserName (state, data) {
      state.userName = data
    },
    // setLoginState (state, data) {
    //   state.loginState = data
    // },
    setConnectStatus (state, connectStatus) {
      state.connectStatus = connectStatus
    },
    setConnectType (state, connectType) {
      state.connectType = connectType
    },
    setNewConnect (state, newConnect) {
      state.newConnect = newConnect
    },
    addBooks (state, books) {
      state.shelf = books
    },
    setCatalog (state, catalog) {
      state.catalog = catalog
    },
    setChapterArr (state, data) {
      state.chapterArr = data
    },
    setChapterId (state, data) {
      state.chapterId = data
    },
    setCatalogArr (state, data) {
      // state.catalogArr = []
      state.catalogArr = data
    },
    setdiffbook (state, data) {
      state.diffbook = data
    },
    setPopCataVisible (state, visible) {
      state.popCataVisible = visible
    },
    setContentLoading (state, loading) {
      state.contentLoading = loading
    },
    setReadingBook (state, readingBook) {
      state.readingBook = readingBook
    },
    setConfig (state, config) {
      state.config = config
    },
    setReadSettingsVisible (state, visible) {
      state.readSettingsVisible = visible
    },
    setShowContent (state, visible) {
      state.showContent = visible
    },
    setCurrentPage (state, data) {
      state.currentPage = data
    },
    setCurrentBook (state, data) {
      state.currentBook = data
    },
    setCurrentBookName (state, data) {
      state.currentBookName = data
    },
    setCurrentChapter (state, data) {
      state.currentChapter = data
    },
    setClickNum (state, data) {
      state.clickNum = data
    },
    setSearchContent (state, data) {
      state.searchContent = data
    },
    setSearchNums (state, data) {
      state.searchNums = data
    },
    setSearchKeyword (state, data) {
      state.searchKeyword = data
    },
    setSupterKeyword (state, data) {
      state.supterKeyword = data
    },
    setUserInfo (state, data) {
      state.userInfo = data
    },
    setShopBookInfo (state, data) {
      state.shopBookInfo = data
    }
  },
  actions: {
  },
  modules: {
  }
})
