import { registerApi } from '../utils/axiosapi.js'

const { ref, reactive } = Vue
const { message } = antd

export default {
  setup() {
    const router = VueRouter.useRouter()

    const formState = reactive({
      username: '',
      nickname: '',
      password: '',
      password2: ''
    })

    const onFinish = values => {
      registerApi(values)
      .then(res => router.push('/success'))
      .catch(err => message.error(err.response.data))
    }

    const validatePass2 = async (_rule, value) => {
      if (value === '') {
        return Promise.reject('请再次确认您的密码!');
      } else if (value !== formState.password) {
        return Promise.reject("两次输入的密码不一致!");
      } else {
        return Promise.resolve();
      }
    }

    const validateName = async (_rule, value) => {
      if (value === '') {
        return Promise.reject('请输入您的用户名!');
      } else if (/\W/.test(value)) {
        return Promise.reject("只允许输入：大写或小写字母、数字、下划线!");
      } else {
        return Promise.resolve();
      }
    }

    return { onFinish, formState, validatePass2, validateName }
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
      <a-form-item
        has-feedback
        label="用户名"
        name="username"
        :rules="[{ required: true, validator: validateName, trigger: 'change' }]"
      >
        <a-input v-model:value="formState.username" />
      </a-form-item>

      <a-form-item
        has-feedback
        label="昵称"
        name="nickname"
        :rules="[{ required: true, message: '请输入您的昵称!' }]"
      >
        <a-input v-model:value="formState.nickname" />
      </a-form-item>

      <a-form-item
        has-feedback
        label="密码"
        name="password"
        :rules="[{ required: true, message: '请输入您的密码!' }]"
      >
        <a-input-password v-model:value="formState.password" />
      </a-form-item>

      <a-form-item
        has-feedback
        label="确认密码"
        name="password2"
        :rules="[{ required: true, validator: validatePass2, trigger: 'change' }]"
      >
        <a-input-password v-model:value="formState.password2" />
      </a-form-item>

      <a-form-item :wrapper-col="{ offset: 10, span: 4 }">
        <a-button type="primary" html-type="submit">注册</a-button>
      </a-form-item>
    </a-form>
  `
}
