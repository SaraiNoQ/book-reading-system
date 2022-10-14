'use strict'
/* nodejs代理解决CORS问题 */
const proxyObj = {}
// proxyObj['/ws'] = {
//   ws: true,
//   target: 'ws://localhost'
// }
proxyObj['/'] = {
  // websocket
  ws: false,
  // 目标地址
  target: 'http://localhost:8099',
  // 发送请求头中host会设置成target
  changeOrigin: true,
  // 不重写请求地址
  pathRewrite: {
    '^/': '/'
  }
}
module.exports = {
  lintOnSave: false,
  publicPath: './',
  outputDir: 'dist',
  assetsDir: 'static',
  productionSourceMap: false,
  devServer: {
    host: 'localhost',
    // port: 8080, // 前端端口号
    proxy: proxyObj
  },
  // proxy: {
  //   '/': {
  //     // 目标地址
  //     target: process.env.VUE_APP_URL,
  //     // 发送请求头中host会设置成target
  //     changeOrigin: true,
  //     // 重写请求地址
  //     rewrite: (path) => path.replace(/^\//, '/')
  //   }
  // },
  chainWebpack: config => {
    config
      .plugin('html')
      .tap(args => {
        args[0].title = '古籍药典阅读器'
        return args
      })
  },
  configureWebpack: (config) => {
    // 判断为生产模式下，因为开发模式我们是想保存console的
    if (process.env.NODE_ENV === 'production') {
      config.optimization.minimizer.map((arg) => {
        const option = arg.options.terserOptions.compress
        option.drop_console = true // 打开开关
        return arg
      })
    }
  }
}
