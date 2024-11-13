export default {
  setup() {
    const router = VueRouter.useRouter()

    const goVideoAdd = () => {
      router.push('/video/add')
    }

    const goHome = () => {
      router.push('/')
    }

    return { goVideoAdd, goHome }
  },
  template: `
    <a-result status="success" title="视频添加成功！">
      <template #extra>
        <a-button @click="goVideoAdd" type="primary">继续添加</a-button>
        <a-button @click="goHome">返回主页</a-button>
      </template>
    </a-result>
  `
}
