import Layout from './layout.js'
import { store } from './utils/store.js'

dayjs.locale('zh-cn')
dayjs.extend(window.dayjs_plugin_relativeTime)

const { createRouter, createWebHashHistory, createWebHistory } = VueRouter
const router = createRouter({
  history: createWebHistory('/'),
  routes: [
    { path: '/', component: () => import('./home.js'), meta: { requiresAuth: false } },
    { path: '/login', component: () => import('./user/login.js'), meta: { requiresAuth: false } },
    { path: '/register', component: () => import('./user/register.js'), meta: { requiresAuth: false } },
    { path: '/success', component: () => import('./user/success.js'), meta: { requiresAuth: false } },
    { path: '/users', component: () => import('./user/list.js'), meta: { requiresAuth: true } },
    {
      path: '/cate',
      children: [
        { path: 'add', component: () => import('./category/add.js'), meta: { requiresAuth: true } },
        { path: 'success', component: () => import('./category/success.js'), meta: { requiresAuth: true } }
      ]
    },
    {
      path: '/video',
      children: [
        { path: 'show/:id', component: () => import('./video/show.js'), meta: { requiresAuth: false } },
        { path: 'edit/:id', component: () => import('./video/edit.js'), meta: { requiresAuth: true } },
        { path: 'editsuc', component: () => import('./video/editsuc.js'), meta: { requiresAuth: true } },
        { path: 'audit/:id', component: () => import('./video/audit.js'), meta: { requiresAuth: true } },
        { path: 'auditsuc', component: () => import('./video/auditsuc.js'), meta: { requiresAuth: true } },
        { path: 'list', component: () => import('./video/list.js'), meta: { requiresAuth: true } },
        { path: 'search', component: () => import('./video/search.js'), meta: { requiresAuth: false } },
        { path: 'add', component: () => import('./video/add.js'), meta: { requiresAuth: true } },
        { path: 'addsuc', component: () => import('./video/addsuc.js'), meta: { requiresAuth: true } }
      ]
    },
    { path: '/:pathMatch(.*)*', redirect: '/', meta: { requiresAuth: false } }
  ]
})

router.beforeEach((to, from) => {
  if (to.meta.requiresAuth && !store.user) {
    // 此路由需要授权，请检查是否已登录
    // 如果没有，则重定向到登录页面
    return {
      path: '/login',
      // 保存我们所在的位置，以便以后再来
      query: { redirect: to.fullPath },
    }
  }
})

Vue.createApp(Layout)
   .use(antd)
   .use(router)
   .mount('#app')
