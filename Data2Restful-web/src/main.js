// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';


import router from './router'
import ajax from './plugins/ajax.js'
import Cookies from 'vue-cookies';
import buildApiRouters from './config/api-routers.js'
import JsonViewer from 'vue-json-viewer';
import store from './store'
import 'babel-polyfill'



Vue.config.productionTip = false


//加载配置文件
let conf = import("@/config/index.js");
conf.then(({default: properties}) => {
    Vue.prototype.$properties = properties;
    Vue.prototype.$apiRouters = buildApiRouters(properties);
    Vue.prototype.api = properties.api;
    if (properties.config) {
      Vue.prototype.$config = { ...Vue.prototype.$config, ...properties.config };
    }
    Vue.use(ajax);
    Vue.use(Cookies);
    Vue.use(ElementUI);
    Vue.use(JsonViewer);

    let components = properties.components || [];
    for (let i = 0; i < components.length; i++) {
      Vue.component(components[i].name, components[i].component);
    }
    router.addRoutes(properties.routes || []);//添加路由配置

    /**
     * 自定义全局指令，防止重复点击按钮
     */
    Vue.directive('preventReClick', {
        inserted(el, binding) {
            el.addEventListener('click', () => {
                if (!el.disabled) {
                    el.disabled = true
                    setTimeout(() => {
                        el.disabled = false
                    }, binding.value || 2000)
                }
            })
        }
    });

  /* eslint-disable no-new */
  new Vue({
    el: '#app',
    router,
    store,
    components: { App },
    template: '<App/>'
  })
});



