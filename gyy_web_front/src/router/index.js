import Vue from 'vue'
import VueRouter from 'vue-router'
import vuex from '../store/index'

Vue.use(VueRouter)

const routes = [
  // {
  //   path: '/',
  //   redirect: '/login'
  //   // component: () => import(/* webpackChunkName: "about" */ '../views/Main.vue')
  // },
  {
    path: '/',
    redirect: '/book',
    name: '首页',
    component: () => import('../views/navigation/Home.vue'),
    children: [
      {
        path: '/regist',
        name: '注册',
        hidden: true,
        component: () => import('../components/Register.vue')
      },
      {
        path: '/book',
        name: '书库',
        component: () => import('../views/navigation/Shop.vue')
      },
      {
        path: '/supersearch',
        name: '高级搜索',
        component: () => import('../views/search/SuperSearch.vue'),
        meta: {
          keepAlive: true
        }
      },
      {
        path: '/search',
        name: '搜索',
        component: () => import('../views/search/SearchQuick.vue'),
        meta: {
          keepAlive: true
        }
      },
      {
        path: '/search/diff',
        name: '关联异文',
        component: () => import('../views/search/ConnectDiff.vue'),
        meta: {
          keepAlive: true
        }
      },
      {
        path: '/search/similar',
        name: '综合搜索',
        component: () => import('../views/search/searchFeature.vue'),
        mata: {
          keepAlive: true
        }
      },
      {
        path: '/test2',
        name: 'Test2',
        component: () => import('../views/navigation/Test2.vue')
      },
      {
        path: 'setting',
        name: 'Setting',
        component: () => import('../views/settings/SettingIndex.vue')
      }
    ]
  },
  {
    path: '/chapter',
    name: '阅读',
    component: () => import('../views/chapter.vue'),
    hidden: true
    // beforeRouteLeave (to, from, next) {
    //   console.log('to', to, from, next)
    //   to.meta.keepAlive = true
    //   next(0)
    // }
  },
  {
    path: '/bookInfo',
    name: '书籍详情',
    component: () => import('../views/bookInfo.vue'),
    hidden: true,
    meta: ['书库', '详情']
  },
  {
    path: '/chapter/orignal',
    name: '原文',
    component: () => import('../views/orignal/Label.vue')
  }
]

const router = new VueRouter({
  mode: 'hash',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  // console.log('change', to, from)
  if (from.path === '/supersearch') {
    // console.log('from')
    vuex.commit('setSupterKeyword', '')
    // console.log(vuex.state.searchKeyword)
  }
  next()
})

export default router
