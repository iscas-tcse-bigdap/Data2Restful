/**
 * ajax.js
 * 封装axios， 提供get, post 等请求
 */
import axios from 'axios'
import qs from "qs"
import router from "../router/index"

import {localRead, localSave, localRemove} from './util.js'

const TIME_OUT_MS = 10 * 60 * 1000 // 默认请求超时时间
const MAX_TIME_OUT_MS = 10 * 60 * 1000 // 默认请求超时时间
//axios.defaults.withCredentials = true; //跨域请求保证每次请求的sessionId 不变
var access_token = 'access_token';
var refresh_token = 'refresh_token';

let apiRouters = null;
let vue = null;

function getApiRouter(url) {
    var router = {
        url: ''
    };
    for (var i = 0; i < apiRouters.length; i++) {
        var r = apiRouters[i];
        if (url.startsWith(r.url) && r.url.length > router.url.length) {
            if (!r.hasOwnProperty('authorized')) {
                r.authorized = true
            }
            router = r
        }
    }
    return router
}

function processURL(config, router) {
    if (config.url.startsWith('http://') || config.url.startsWith('https://') || !router.base) {
        return
    }
    config.url = router.base + config.url
}


//http request 拦截器
axios.interceptors.request.use(
    config => {
        debugger
        if (config.method === 'get') {
            config.params = {
                t: Date.parse(new Date()) / 1000,
                ...config.params
            }
        }

        let router = config._router || getApiRouter(config.url)
        config._router = router
        processURL(config, router)

        config.withCredentials = config.withCredentials || !!router.withCredentials
        if (router.authorized && !config.headers.Authorization) {//如果路由需要权限验证，并且header没有添加Authorization，需要自动添加上
            config.headers.Authorization = router.token || ('Bearer ' + getAccessToken())
        }

        return config
    },
    err => {
        return Promise.reject(err)
    });

// http response 拦截器，不需要，不满足refresh需求
axios.interceptors.response.use(
    response => {
        return response;
    },
    error => {
        if (error.response) {
            console.log(error.response);
        }
        return Promise.reject(error); // 返回接口返回的错误信息
    });


/*
 * @param data 参数列表
 * @return
 */
function handleParams(data) {
    return qs.stringify(data)
}


function wrapper(method, url, data, config) {
    debugger;
    let headers = {
        'Content-Type': 'application/json; charset=UTF-8', //默认json， 如果config.headers里配置了会被自动覆盖掉
    }
    if(config && config.headers) {
        headers = {
            ...config.headers
        }
    }
    let _config = {
        method: method,
        url: url,
        timeout: TIME_OUT_MS,
        headers: headers,
        _stop: config && config.stop
    }
    _config[method == 'get' ? 'params' : 'data'] = data
    return new Promise((resolve, reject) => {
        axios(_config).then((response) => {
            resolve(response)
        }).catch((error) => {
            console.log("response error :" + error)
            return reject(error)
        })
    })
}

function getTokenName(name) {
    let domain = vue.prototype.$DOMAIN || '';
    return domain + name;
}



function getAccessToken() {
    let token_name = getTokenName(access_token);
    if (token_name != access_token) {
        return localRead(token_name)
    }
    return $cookies.get(getTokenName(access_token));
}


function setAccessToken(token) {
    let token_name = getTokenName(access_token);
    if (token_name != access_token) {
        return localSave(token_name, token)
    }
    return $cookies.set(getTokenName(access_token), token)
}

function removeAccessToken() {
    let token_name = getTokenName(access_token);
    if (token_name != access_token) {
        return localRemove(token_name)
    }
    return $cookies.remove(getTokenName(access_token))
}

function getRefreshToken() {
    let token_name = getTokenName(refresh_token);
    if (token_name != refresh_token) {
        return localRead(token_name)
    }
    return $cookies.get(getTokenName(refresh_token))
}

function setRefreshToken(token) {
    let token_name = getTokenName(refresh_token);
    if (token_name != refresh_token) {
        return localSave(token_name, token)
    }
    return $cookies.set(getTokenName(refresh_token), token)
}

function removeRefreshToken() {
    let token_name = getTokenName(refresh_token);
    if (token_name != refresh_token) {
        return localRemove(token_name)
    }
    return $cookies.remove(getTokenName(refresh_token))
}

const installer = {
    install: function (Vue) {
        Vue.prototype.$get = (url, data, config) => {
            return wrapper('get', url, data, config)
        };
        Vue.prototype.$post = (url, data, config) => {
            return wrapper('post', url, data, config)
        };
        Vue.prototype.$delete = (url, data, config) => {
            return wrapper('delete', url, data,  config)
        };
        Vue.prototype.$put = (url, data, config) => {
            return wrapper('put', url, data, config)
        };
        Vue.prototype.$fetch = (url) => {
            return axios.create({
                baseURL: ''
            }).get(url)
        };
        Vue.prototype.$ajax = (method, url, data, config) => {
            return wrapper(method, url, data, config)
        };
       
        Vue.prototype.$getAccessToken = () => {
            return getAccessToken();
        };
        Vue.prototype.$setAccessToken = (token) => {
            return setAccessToken(token);
        };
        Vue.prototype.$removeAccessToken = () => {
            return removeAccessToken();
        };
        apiRouters = Vue.prototype.$apiRouters;
        vue = Vue;
    }
};

export default installer;
