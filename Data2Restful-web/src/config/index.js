export default {
    token: 'Basic c3lzdGVtX3dlYjpzeXN0ZW0=',
    title: '地球大数据挖掘分析系统EarthDataMiner',
    // 远程部署
    // url: {
    //     ws: 'ws://' + location.hostname + ':8080/',
    //     auth: 'http://' + location.hostname + '/auth/',
    //     api: 'http://' + location.hostname + '/server/',
    //     dockerBuild: 'http://' + location.hostname + '/dockerBuild/',
    //     geojson: 'http://' + location.hostname + '/geojson/'
    // },
    // 本地测试，连远程服务器
    url: {
        // api: 'http://localhost:8081/',
        api: 'http://60.245.208.50/server/',
        k8scli: 'http://60.245.208.50/',
        userauth: 'http://60.245.208.50/k8scli/',
    },
    // 本地测试，连本地服务器
    // url: {
    //     ws: 'ws://localhost:8800/',
    //     auth: 'http://localhost:8091',
    //     api: 'http://localhost:8800/',
    //     dockerBuild: 'http://10.18.61.1:8089/',
    //     base: 'http://localhost:8084/',
    //     transfer: 'http://localhost:8085/',
    // },
    api: {
      base_path: `http://60.245.208.50:9001/svc/`,
    },
    routes: [],//可以动态配置路由
    components: [

    ],
    languages: [
        {
            name: 'zh-CN',
            localeTitle: '语言',
            langName: '中文简体'
        },
        {
            name: 'en-US',
            localeTitle: 'lang',
            langName: 'English'
        }
    ],
    config: {

    },
    theme: 'dark'
}
