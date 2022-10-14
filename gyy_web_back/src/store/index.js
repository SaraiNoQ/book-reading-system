import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        routes: [],
        userName: '',
        loginState: false,
        currentPhotoChapter: {},
        catalog: [],
        currentChapterId: ''
    },
    mutations: {
    initRoutes(state, data) {
        state.routes = data;
    },
    setUserName(state, data) {
        state.userName = data
    },
    setLoginState(state, data) {
        state.loginState = data
    },
    setCurrentPhotoChapter (state, data) {
        state.currentPhotoChapter = data
    },
    setCatalog (state, data) {
        state.catalog = data
    },
    setChapterId (state, data) {
        state.currentChapterId = data
    }
   },
    actions:{

    },
    modules:{

    }
})