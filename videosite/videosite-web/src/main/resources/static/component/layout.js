import { store } from './utils/store.js'

const { ref, reactive, watch } = Vue
const { message } = antd
const locale = antd.locales.zh_CN

export default {
  setup() {
//    const selectedKeys = ref( [location.pathname] )

    const state = reactive({ keyword: null })

    const router = VueRouter.useRouter()

//    watch(
//      () => router.currentRoute.value.path,
//      (newValue, oldValue) => {
//        selectedKeys.value = [newValue]
//      },
//      { immediate: true }
//    )

    const handleClick = ( key ) => {
      if(key === '/logout'){
        store.clearUser(true)
        router.push('/')
        message.success('您已安全退出系统。')
      }else{
        router.push(key)
      }
    }

    const handleSearch = ( keyword ) => {
        if(keyword && keyword.trim()){
            store.setVideos(keyword)
            router.push('/video/search')
        }
    }

    const faviconSrc = { src: 'data:image/png;base64,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' }

    return { locale, handleClick, faviconSrc, store, state, handleSearch }
  },
  template: `
    <a-config-provider :locale="locale">
      <a-layout>
       <a-layout-header :style="{ position: 'fixed', zIndex: 1, width: '100%', background: 'none' }">
         <a-page-header title="我的视频网站" :avatar="faviconSrc" :style="{ padding: '10px 20px', backgroundColor: 'white' }">
           <template #extra>
             <a-input-search v-model:value="state.keyword" placeholder="输入关键词" @search="handleSearch" allowClear/>
             <a-button v-for="(menu, index) in store.getMenus()" :key="index" :type="menu.type" @click="handleClick(menu.key)">
                <i v-if="!!menu.icon" :class="menu.icon"></i>
                <img v-else :src="menu.src" width="22px" height="22px"/>
                {{ menu.title }}
             </a-button>
           </template>
         </a-page-header>
       </a-layout-header>
       <a-layout-content :style="{ padding: '0 50px', marginTop: '64px' }">
         <nav class="ant-breadcrumb" style="margin: 10px 0px;"></nav>
         <div :style="{ background: '#fff', padding: '24px', minHeight: '815px' }">
           <Suspense>
            <router-view></router-view>
           </Suspense>
         </div>
       </a-layout-content>
       <a-layout-footer :style="{ textAlign: 'center' }">Copyright &copy; 2020 - 2024 关于网站 网站推广 联系我们 帮助中心</a-layout-footer>
      </a-layout>
    </a-config-provider>
  `
}
