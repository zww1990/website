import { loginApi } from '../utils/axiosapi.js'
import { store } from '../utils/store.js'

const { ref, reactive } = Vue
const { message } = antd

export default {
  setup() {
    const router = VueRouter.useRouter()
    const route = VueRouter.useRoute()
    const formState = reactive({ username: '', password: '' })

    const onFinish = values => {
      loginApi(values).then(res => {
        store.setUser(res.data)
        router.push(route.query.redirect ? route.query.redirect : '/')
      }).catch(err => message.error(err.response.data))
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
        label="用户名"
        name="username"
        :rules="[{ required: true, message: '请输入您的用户名!' }]"
      >
        <a-input v-model:value="formState.username" />
      </a-form-item>

      <a-form-item
        has-feedback
        label="密码"
        name="password"
        :rules="[{ required: true, message: '请输入您的密码!' }]"
      >
        <a-input-password v-model:value="formState.password" />
      </a-form-item>

      <a-form-item :wrapper-col="{ offset: 10, span: 4 }">
        <a-button type="primary" html-type="submit">登录</a-button>
      </a-form-item>
    </a-form>
  `
}
