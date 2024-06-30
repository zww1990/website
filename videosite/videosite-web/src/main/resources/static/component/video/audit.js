import { videoAuditApi, videoHandleAuditApi } from '../utils/fetchapi.js'
import { store } from '../utils/store.js'

const { message, notification } = antd
const { ref, reactive } = Vue

export default {
  async setup() {
    const router = VueRouter.useRouter()
    const route = VueRouter.useRoute()
    const audit = ref({
      id: +route.params.id,
      auditStatus: '',
      auditReason: '',
      radioOptions: [
      { label: '审核通过', value: 'PASSED' },
      { label: '审核不通过', value: 'UNPASSED' },
      ]
    })
    const res = await videoAuditApi(audit.value.id)
    const video = ref({})
    if(res.ok){
      video.value = await res.json()
    }else{
      message.error(await res.text())
    }
    const handleSubmit = async () => {
      if (!audit.value.auditStatus) {
        notification.error({ message: '操作错误', description: '请进行审核，方可提交！' })
        return
      }
      if (audit.value.auditStatus === 'UNPASSED' && !audit.value.auditReason) {
        notification.error({ message: '操作错误', description: '审核不通过时，请填写原因！' })
        return
      }
      const res2 = await videoHandleAuditApi(audit.value)
      if(res2.ok){
        router.push('/video/auditsuc')
      }else{
        notification.error({ message: '操作错误', description: await res2.text() })
      }
    }
    return { video, audit, handleSubmit }
  },
  template: `
    <a-row :gutter="[16,8]">
      <a-col :span="video.auditStatus === 'PENDING' ? 16 : 24">
        <a-card hoverable>
          <template #cover>
            <video controls style="height: 394px" :src="video.videoLink" :poster="video.videoLogo"></video>
          </template>
          <a-card-meta>
            <template #description>
              <a-descriptions :title="video.videoName" :column="4">
                <a-descriptions-item label="播放量">{{video.videoHits}}</a-descriptions-item>
                <a-descriptions-item label="类别">{{video.categoryName}}</a-descriptions-item>
                <a-descriptions-item label="状态">{{video.auditStatusDesc}}</a-descriptions-item>
                <a-descriptions-item label="作者">{{video.creatorNick}}</a-descriptions-item>
                <a-descriptions-item label="创建时间">{{video.createdDate}}</a-descriptions-item>
                <a-descriptions-item label="审核人">{{video.auditorNick}}</a-descriptions-item>
                <a-descriptions-item label="审核时间">{{video.auditedDate}}</a-descriptions-item>
              </a-descriptions>
            </template>
          </a-card-meta>
        </a-card>
      </a-col>
      <a-col :span="8" v-if="video.auditStatus === 'PENDING'">
        <a-form-item>
          <a-radio-group v-model:value="audit.auditStatus" :options="audit.radioOptions" />
        </a-form-item>
        <a-form-item>
          <a-textarea v-model:value="audit.auditReason" :rows="4" placeholder="审核不通过时，请填写原因"/>
        </a-form-item>
        <a-form-item>
          <a-button html-type="submit" type="primary" @click="handleSubmit">审核</a-button>
        </a-form-item>
      </a-col>
    </a-row>
  `
}
