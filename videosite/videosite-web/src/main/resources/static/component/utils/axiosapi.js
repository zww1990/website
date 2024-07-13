import { store } from './store.js'

const { message } = antd

// 添加视频分类
const categoryAddApi = params => axios.post('/category/add', params)

// 视频分类列表
const categoryListApi = () => axios.get('/category/list')
.then(res => res.data)
.catch(err => {
  console.log(err.message, err.response)
  message.error(err.response.data)
  return []
})

// 添加视频评论
const commentAddApi = params => axios.post('/comment/add', params)

// 用户登录
const loginApi = params => axios.post('/user/login', params)

// 用户注册
const registerApi = params => axios.post('/user/register', params)

// 用户注销
const logoutApi = () => axios.get('/user/logout')
.then(res => console.log(res.data, res.status, res.statusText))
.catch(err => console.log(err.message, err.response))

// 用户列表
const userListApi = () => axios.get('/user/list')
.then(res => res.data)
.catch(err => {
  console.log(err.message, err.response)
  message.error(err.response.data)
  return []
})

// 首页
const homeApi = id => {
  if(id){
    return axios.get('/home', { params: { 'categoryId': id } })
    .then(res => res.data)
    .catch(err => {
      console.log(err.message, err.response)
      message.error(err.response.data)
      return {}
    })
  }
  return axios.get('/home')
  .then(res => res.data)
  .catch(err => {
    console.log(err.message, err.response)
    message.error(err.response.data)
    return {}
  })
}

//视频搜索
const videoSearchApi = keyword => axios.get('/videohub/search', { params: { 'keyword': keyword } })
.then(res => res.data)
.catch(err => {
  console.log(err.message, err.response)
  message.error(err.response.data)
  return []
})

// 删除视频
const videoDelApi = params => axios.delete('/videohub/delete', { params: { 'id': params.id } })

// 添加视频点击量
const videoAddHitsApi = id => axios.put('/videohub/addhits', { params: { 'id': id } })
.then(res => console.log(res.data, res.status, res.statusText))
.catch(err => console.log(err.message, err.response))

// 视频详情
const videoShowApi = id => axios.get('/videohub/show', { params: { 'id': id } })
.then(res => res.data)
.catch(err => {
  console.log(err.message, err.response)
  message.error(err.response.data)
  return {}
})

// 视频详情
const videoEditApi = id => axios.get('/videohub/edit', { params: { 'id': id } })

// 视频详情
const videoAuditApi = id => axios.get('/videohub/audit', { params: { 'id': id } })

// 视频列表
const videoListApi = () => axios.get('/videohub/list')
.then(res => res.data)
.catch(err => {
  console.log(err.message, err.response)
  message.error(err.response.data)
  return []
})

// 添加视频
const videoAddApi = params => {
  const formData = new FormData()
  Object.entries(params).forEach(([ k, v ]) => formData.append(k, v))
  return axios.post('/videohub/add', formData)
}

// 编辑视频
const videoHandleEditApi = params => {
  const formData = new FormData()
  Object.entries(params).forEach(([ k, v ]) => {
    if(v){
      formData.append(k, v)
    }
  })
  return axios.put('/videohub/edit', formData)
}

// 审核视频
const videoHandleAuditApi = params => axios.put('/videohub/audit', params)

// 添加响应拦截器
axios.interceptors.response.use(res => {
  return res
}, err => {
  if([401].includes(err.response.status)){
    store.clearUser()
  }
  return Promise.reject(err)
})

export {
  loginApi,
  logoutApi,
  registerApi,
  userListApi,
  categoryAddApi,
  categoryListApi,
  homeApi,
  commentAddApi,
  videoSearchApi,
  videoDelApi,
  videoAddHitsApi,
  videoShowApi,
  videoListApi,
  videoAddApi,
  videoAuditApi,
  videoEditApi,
  videoHandleAuditApi,
  videoHandleEditApi,
}
