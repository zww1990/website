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
    { path: '/cate/add', component: () => import('./category/add.js') },
    { path: '/cate/success', component: () => import('./category/success.js') },
    { path: '/video/show/:id', component: () => import('./video/show.js') },
    { path: '/video/edit/:id', component: () => import('./video/edit.js') },
    { path: '/video/editsuc', component: () => import('./video/editsuc.js') },
    { path: '/video/audit/:id', component: () => import('./video/audit.js') },
    { path: '/video/auditsuc', component: () => import('./video/auditsuc.js') },
    { path: '/video/list', component: () => import('./video/list.js') },
    { path: '/video/search', component: () => import('./video/search.js') },
    { path: '/video/add', component: () => import('./video/add.js') },
    { path: '/video/addsuc', component: () => import('./video/addsuc.js') },
    { path: '/:pathMatch(.*)*', redirect: '/' }
  ]
})

Vue.createApp(Layout)
   .use(antd)
   .use(router)
   .mount('#app')
