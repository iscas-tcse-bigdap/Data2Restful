import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'index',
      component: () => import('@/views/index'),
      children: [
        {
          path: 'home',
          component: () => import('@/views/home/index'),
        },
        {
          path: 'algopublic',
          component: () => import('@/views/apiServices/index'),
        },
        {
          path: 'algos',
          component: () => import('@/views/algosApp/index'),
        },
        {
          path: 'api',
          component: () => import('@/views/apiServices/apiCenter'),
        },
        {
          path: 'api/add',
          component: () => import('@/views/apiServices/addApi')
        },
        {
          path: 'api/detail/:apiId',
          component: () => import('@/views/apiServices/apiDetail')
        },
        {
          path: 'api/test/:apiId',
          component: () => import('@/views/apiServices/apiTest')
        },
        {
          path: 'api/group_manage',
          component: () => import('@/views/apiServices/groupManage'),
          props: true
        },
        {
          path: '/ds',
          name: 'dsCenter',
          component: () => import('@/views/dataSource/dsCenter'),
        },
        {
          path: '/ds/add',
          name: 'index',
          component: () => import('@/views/dataSource/addDataSource'),
        },
        {
          path: '/ds/detail/:srcId',
          name: 'dsDetail',
          component: () => import('@/views/dataSource/dsDetail'),
        },
        {
          path: 'management',
          name: 'svcManagemantModule',
          component: () => import('@/views/svcManagement/index'),
        },
        {
          path: 'management/service/:svcId',
          name: 'servicePage',
          component: () => import('@/views/svcManagement/service/index'),
        },
      ]
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/login'),
    },
  ]
})
