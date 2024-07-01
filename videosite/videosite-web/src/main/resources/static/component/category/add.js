import { categoryAddApi } from '../utils/axiosapi.js'

const { ref, reactive } = Vue
const { message } = antd

export default {
  setup() {
    const router = VueRouter.useRouter()
    const formState = reactive({ categoryName: '' })
    const onFinish = values => {
      categoryAddApi(values)
      .then(res => router.push('/cate/success'))
      .catch(err => message.error(err.response.data))
    }
    return { onFinish, formState }
  },
  template: `
    <a-form
      :model="formState"
      name="basic"
      :label-col="{ span: 10 }"
      :wrapper-col="{ span: 4 }"
      autocomplete="off"
      @finish="onFinish"
    >
      <a-form-item></a-form-item>
      <a-form-item></a-form-item>
      <a-form-item></a-form-item>
      <a-form-item
        has-feedback
        label="视频类别名称"
        name="categoryName"
        :rules="[{ required: true, message: '请输入视频类别名称!' }]"
      >
        <a-input v-model:value="formState.categoryName" />
      </a-form-item>

      <a-form-item :wrapper-col="{ offset: 10, span: 4 }">
        <a-button type="primary" html-type="submit">添加</a-button>
      </a-form-item>
    </a-form>
  `
}
