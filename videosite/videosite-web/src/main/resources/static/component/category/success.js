export default {
  setup() {
    const router = VueRouter.useRouter()

    const goCategoryAdd = () => {
      router.push('/cate/add')
    }

    const goHome = () => {
      router.push('/')
    }

    return { goCategoryAdd, goHome }
  },
  template: `
    <a-result status="success" title="视频类别添加成功！">
      <template #extra>
        <a-button @click="goCategoryAdd" type="primary">继续添加</a-button>
        <a-button @click="goHome">返回主页</a-button>
      </template>
    </a-result>
  `
}
