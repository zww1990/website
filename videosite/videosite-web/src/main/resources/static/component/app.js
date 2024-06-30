import Layout from './layout.js'

dayjs.locale('zh-cn')
dayjs.extend(window.dayjs_plugin_relativeTime)

const { createRouter, createWebHashHistory, createWebHistory } = VueRouter
const router = createRouter({
  history: createWebHistory('/'),
  routes: [
    { path: '/', component: () => import('./home.js') },
    { path: '/login', component: () => import('./user/login.js') },
    { path: '/register', component: () => import('./user/register.js') },
    { path: '/success', component: () => import('./user/success.js') },
    { path: '/users', component: () => import('./user/list.js') },
    {
      path: '/cate',
      children: [
        { path: 'add', component: () => import('./category/add.js') },
        { path: 'success', component: () => import('./category/success.js') }
      ]
    },
    {
      path: '/video',
      children: [
        { path: 'show/:id', component: () => import('./video/show.js') },
        { path: 'edit/:id', component: () => import('./video/edit.js') },
        { path: 'editsuc', component: () => import('./video/editsuc.js') },
        { path: 'audit/:id', component: () => import('./video/audit.js') },
        { path: 'auditsuc', component: () => import('./video/auditsuc.js') },
        { path: 'list', component: () => import('./video/list.js') },
        { path: 'search', component: () => import('./video/search.js') },
        { path: 'add', component: () => import('./video/add.js') },
        { path: 'addsuc', component: () => import('./video/addsuc.js') }
      ]
    },
    { path: '/:pathMatch(.*)*', redirect: '/' }
  ]
})

Vue.createApp(Layout)
   .use(antd)
   .use(router)
   .mount('#app')
