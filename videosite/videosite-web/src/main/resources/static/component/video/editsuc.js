export default {
  setup() {
    const router = VueRouter.useRouter()

    const goVideoList = () => {
      router.push('/video/list')
    }

    const goHome = () => {
      router.push('/')
    }

    return { goVideoList, goHome }
  },
  template: `
    <a-result status="success" title="视频编辑成功！">
      <template #extra>
        <a-button @click="goVideoList" type="primary">视频列表</a-button>
        <a-button @click="goHome">返回主页</a-button>
      </template>
    </a-result>
  `
}
