import { homeApi } from './utils/axiosapi.js'

const { ref, reactive } = Vue

export default {
  async setup() {
    const router = VueRouter.useRouter()
    const homeData = await homeApi()

    const state = reactive({
      tags: homeData.categories,
      cards: homeData.videos,
      selectedTags: []
    })

    const handleChange = async (tag, checked) => {
      if(checked){
        state.selectedTags = [tag]
        state.cards = (await homeApi(tag.id)).videos
      }else{
        state.selectedTags = []
        state.cards = (await homeApi()).videos
      }
    }

    const handleClick = card => {
      router.push(`/video/show/${card.id}`)
    }

    const fallbackImg = 'data:image/png;base64,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'
    const avatarImg = 'data:image/png;base64,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'

    return { state, handleChange, fallbackImg, avatarImg, handleClick }
  },
  template: `
    <a-row>
      <a-col :span="24">
        <span :style="{ marginRight: '8px' }">推荐:</span>
      </a-col>
    </a-row>
    <a-divider />
    <a-row :gutter="[8,8]">
      <template v-if="state.cards.length > 0">
        <a-col :span="4" v-for="card in state.cards" :key="card.id">
          <a-card hoverable>
            <template #cover>
              <a-image :alt="card.videoName" :src="card.videoLogo" :fallback="fallbackImg" />
            </template>
            <template #actions>
              <i class="fa fa-eye" @click="handleClick(card)">查看({{card.videoHits}})</i>
            </template>
            <a-card-meta :title="card.creatorNick" :description="card.videoName">
              <template #avatar>
                <a-avatar :src="avatarImg" />
              </template>
            </a-card-meta>
          </a-card>
        </a-col>
      </template>
      <a-col :span="24" v-else>
        <a-empty />
      </a-col>
    </a-row>
    <a-divider />
    <a-row>
      <a-col :span="24">
        <span :style="{ marginRight: '8px' }">类别:</span>
        <template v-for="tag in state.tags" :key="tag.id">
          <a-checkable-tag
            :checked="state.selectedTags.indexOf(tag) > -1"
            @change="checked => handleChange(tag, checked)"
            style="font-size: 14px;"
          >
            {{ tag.categoryName }}
          </a-checkable-tag>
        </template>
      </a-col>
    </a-row>
    <a-divider />
    <a-row :gutter="[8,8]">
      <template v-if="state.cards.length > 0">
        <a-col :span="4" v-for="card in state.cards" :key="card.id">
          <a-card hoverable>
            <template #cover>
              <a-image :alt="card.videoName" :src="card.videoLogo" :fallback="fallbackImg" />
            </template>
            <template #actions>
              <i class="fa fa-eye" @click="handleClick(card)">查看({{card.videoHits}})</i>
            </template>
            <a-card-meta :title="card.creatorNick" :description="card.videoName">
              <template #avatar>
                <a-avatar :src="avatarImg" />
              </template>
            </a-card-meta>
          </a-card>
        </a-col>
      </template>
      <a-col :span="24" v-else>
        <a-empty />
      </a-col>
    </a-row>
  `
}
