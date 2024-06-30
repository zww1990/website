import { categoryAddApi } from '../utils/fetchapi.js'

const { ref, reactive } = Vue
const { message } = antd

export default {
  setup() {
    const router = VueRouter.useRouter()
    const formState = reactive({ categoryName: '' })
    const onFinish = async values => {
      const res = await categoryAddApi(values)
      if(res.ok){
        router.push('/cate/success')
      }else{
        message.error(await res.text())
      }
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
