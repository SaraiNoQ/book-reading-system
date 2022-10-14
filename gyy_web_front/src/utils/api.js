import axios from 'axios'
import { Message } from 'element-ui'
import router from '../router'
import store from '../store'

const instance = axios.create({
  baseURL: process.env.VUE_APP_URL
})

// 设置请求拦截器
instance.interceptors.request.use(config => { // 向服务器发送请求需要发送config
  // 如果是首页接口，不加token
  if (config.url === '/book/getBookInfo/1/6') {
    return config
  }
  // 如果存在token，请求将会携带这个token（在登陆时会把token存入sessionStorage中）
  if (window.localStorage.getItem('token')) {
    config.headers.token = window.localStorage.getItem('token')
  } else if (window.sessionStorage.getItem('token')) {
    config.headers.token = window.sessionStorage.getItem('token')
  }
  return config
}, error => {
  console.dir(error)
})

// 设置响应拦截器(方便统一地处理错误信息)
instance.interceptors.response.use(success => {
  // 业务逻辑错误(success表示能连到接口)
  if (success.status && success.status === 200) {
    if (success.data.code === 500 || success.data.code === 403) {
      console.log('Response Interceptor Error', success.data)
    }
    if (success.data.message) {
      console.log('Response Interceptor Message', success.data)
    }
    if (success.data.code === 401) {
      // 清除token（sessionStorage中保存的token关闭页面后会自动消失，但要防止用户在使用过程中token过期）
      // 不论是不是在登录页，都要清除token
      try {
        store.commit('setUserName', '')
        store.commit('setUserInfo', {})
        sessionStorage.setItem('store', JSON.stringify(store.state))
        localStorage.removeItem('token')
        sessionStorage.removeItem('token')
      } catch (err) {
        console.log('remove token error', err)
      }
      if (router.currentRoute.path !== '/book') {
        router.replace('/book')
        Message.error('登录失效，请重新登录！')
      }
    }
    // if (success.data.status === 'Unlogin') {
    //   Message.error({ message: '请先登录！' })
    //   store.commit('setLoginState', false)
    //   store.commit('setUserName', '')
    //   sessionStorage.setItem('store', JSON.stringify(store.state))
    //   store.commit('setUserInfo', {})
    //   setTimeout(() => {
    //     router.push('/')
    //   }, 1300)
    // }
  }
  // 返回一个json对象，以便可以进行后续处理
  return success
}, error => {
  // 连不上接口，根据不同状态码返回连不上接口的原因
  if (error.response.code === 504 || error.response.code === 404) {
    console.log('interceptor', '服务器错误！')
  } else if (error.response.code === 403) {
    console.log('interceptor', '权限不足，请联系管理员！')
  } else if (error.response.code === 401) {
    console.log('interceptor', '请先登录！')
    router.replace('/')
  } else {
    // 不满足以上所有情况，判断响应体中是否有信息，有信息就给打印出来
    if (error.response.data.message) {
      console.log('Response Interceptor Warning', error.response.data.message)
    }
  }
  return 'error'
})

const baseURL = '' // 前缀地址
// 封装post请求
export const postRequest = (url, params) => {
  return axios({
    method: 'post',
    url: `${baseURL}${url}`,
    data: params
  })
}

// 封装put请求
export const putRequest = (url, params) => {
  return axios({
    method: 'put',
    url: `${baseURL}${url}`,
    data: params
  })
}

// 封装get请求
export const getRequest = (url, params) => {
  return axios({
    method: 'get',
    url: `${baseURL}${url}`,
    data: params
  })
}

// 封装delete请求
export const deleteRequest = (url, params) => {
  return axios({
    method: 'delete',
    url: `${baseURL}${url}`,
    data: params
  })
}

export default instance
