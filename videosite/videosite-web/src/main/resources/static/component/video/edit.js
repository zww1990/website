import { videoEditApi, categoryListApi, videoHandleEditApi } from '../utils/api.js'

const { ref, reactive } = Vue
const { message, Upload } = antd

export default {
  async setup() {
    const router = VueRouter.useRouter()
    const route = VueRouter.useRoute()
    const formState = reactive({
      id: +route.params.id,
      videoName: '',
      categoryId: undefined,
      videoLogo: null,
      videoLink: null,
      videoFileList: [],
      imageFileList: [],
    })
    const res2 = await videoEditApi(formState.id)
    if(res2.ok){
      const data = await res2.json()
      formState.videoName = data.videoName
      formState.categoryId = data.categoryId
      formState.imageFileList = [{
        uid: new Date().getTime(),
        name: data.videoLogo.substring(data.videoLogo.lastIndexOf('/') + 1),
        status: 'done',
        url: data.videoLogo
      }]
      formState.videoFileList = [{
        uid: new Date().getTime(),
        name: data.videoLink.substring(data.videoLink.lastIndexOf('/') + 1),
        status: 'done',
        url: data.videoLink
      }]
    }else{
      message.error(await res2.text())
    }
    const onFinish = async values => {
      const res = await videoHandleEditApi(formState)
      if(res.ok){
        router.push('/video/editsuc')
      }else{
        message.error(await res.text())
      }
    }
    const categories = (await categoryListApi()).map(({ id, categoryName }) => {
      return { label: categoryName, value: id }
    })
    const getFileSize = number => {
      if (number < 1024) {
        return `${number} bytes`
      } else if (number >= 1024 && number < 1048576) {
        return `${(number / 1024).toFixed(1)} KB`
      } else if (number >= 1048576) {
        return `${(number / 1048576).toFixed(1)} MB`
      }
    }
    const imageBeforeUpload = file => {
      if (!file.type.startsWith('image/')) {
        message.error(`此 ${file.name} 不是有效的图片文件！`)
        return Upload.LIST_IGNORE
      }
      const limit = 1024 * 1024 * 10
      if (file.size > limit) {
        message.error(`此 ${file.name} (${getFileSize(file.size)}) 超过最大限制 (${getFileSize(limit)}) ！`)
        return Upload.LIST_IGNORE
      }
      formState.videoLogo = file
      return false
    }
    const videoBeforeUpload = file => {
      if (!file.type.startsWith('video/')) {
        message.error(`此 ${file.name} 不是有效的视频文件！`)
        return Upload.LIST_IGNORE
      }
      const limit = 1024 * 1024 * 100
      if (file.size > limit) {
        message.error(`此 ${file.name} (${getFileSize(file.size)}) 超过最大限制 (${getFileSize(limit)}) ！`)
        return Upload.LIST_IGNORE
      }
      formState.videoLink = file
      return false
    }
    const setCursor = () => {
      document.querySelectorAll('span.ant-upload-list-item-name').forEach(el => el.style.cursor = 'pointer')
    }
    const modalState = reactive({
      previewVisible: false,
      previewTitle: '',
      previewUrl: '',
      isVideo: false,
    })
    const handleCancel = () => {
      modalState.previewVisible = false
      modalState.previewTitle = ''
      modalState.previewUrl = ''
    }
    const handlePreview = (file, isVideo) => {
      setCursor()
      modalState.previewVisible = true
      modalState.previewTitle = file.name
      modalState.previewUrl = file.originFileObj ? URL.createObjectURL(file.originFileObj) : file.url
      modalState.isVideo = isVideo
    }
    return {
      onFinish,
      formState,
      categories,
      imageBeforeUpload,
      videoBeforeUpload,
      handlePreview,
      handleCancel,
      modalState
    }
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
        label="视频名称"
        name="videoName"
        :rules="[{ required: true, message: '请输入视频名称!' }]"
      >
        <a-input v-model:value="formState.videoName" />
      </a-form-item>

      <a-form-item
        has-feedback
        label="视频类别"
        name="categoryId"
        :rules="[{ required: true, message: '请选择视频类别!' }]"
      >
        <a-select v-model:value="formState.categoryId" :options="categories"></a-select>
      </a-form-item>

      <a-form-item label="视频封面" name="videoLogo">
        <a-upload
          name="videoLogo"
          v-model:file-list="formState.imageFileList"
          :before-upload="imageBeforeUpload"
          :maxCount="1"
          accept="image/*"
          @preview="file => handlePreview(file, false)"
          :showUploadList="{ showRemoveIcon: false }">
          <a-button>选择文件</a-button>
        </a-upload>
      </a-form-item>

      <a-form-item label="视频文件" name="videoLink">
        <a-upload
          name="videoLink"
          v-model:file-list="formState.videoFileList"
          :before-upload="videoBeforeUpload"
          :maxCount="1"
          accept="video/*"
          @preview="file => handlePreview(file, true)"
          :showUploadList="{ showRemoveIcon: false }">
          <a-button>选择文件</a-button>
        </a-upload>
      </a-form-item>

      <a-form-item :wrapper-col="{ offset: 10, span: 4 }">
        <a-button type="primary" html-type="submit">保存</a-button>
      </a-form-item>
    </a-form>
    <a-modal
      :open="modalState.previewVisible"
      :title="modalState.previewTitle"
      :footer="null"
      @cancel="handleCancel"
      :bodyStyle="{ textAlign: 'center' }"
      width="800px"
      centered
      destroyOnClose>
      <video controls style="width: 100%" :src="modalState.previewUrl" v-if="modalState.isVideo"></video>
      <img style="width: 100%" :src="modalState.previewUrl" v-else/>
    </a-modal>
  `
}
