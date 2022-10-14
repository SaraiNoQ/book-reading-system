import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import store from './store';
import { postRequest } from "./utis/api";
import { putRequest } from "./utis/api";
import { getRequest } from "./utis/api";
import { deleteRequest } from "./utis/api";
import axios from "./utis/api";
import qs from "qs"
Vue.prototype.$qs = qs;
Vue.config.productionTip = false
Vue.use(ElementUI);

Vue.prototype.postRequest = postRequest;
Vue.prototype.putRequest = putRequest;
Vue.prototype.getRequest = getRequest;
Vue.prototype.deleteRequest = deleteRequest;
Vue.prototype.$axios = axios;


new Vue({
  router,
  axios,
  store,
  render: h => h(App)
}).$mount('#app')
