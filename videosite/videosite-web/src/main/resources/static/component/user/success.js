export default {
  setup() {
    const router = VueRouter.useRouter()
    const goLogin = () => {
      router.push('/login')
    }
    const goHome = () => {
      router.push('/')
    }
    return { goLogin, goHome }
  },
  template: `
    <a-result status="success" title="恭喜您，注册成功！">
      <template #extra>
        <a-button @click="goLogin" type="primary">去登录</a-button>
        <a-button @click="goHome">返回主页</a-button>
      </template>
    </a-result>
  `
}
