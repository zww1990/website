// 添加视频分类
const categoryAddApi = params => fetch('/category/add', {
  method: 'POST',
  body: JSON.stringify(params),
  headers: { 'Content-Type': 'application/json' }
})

// 视频分类列表
const categoryListApi = async () => await (await fetch('/category/list')).json()

// 添加视频评论
const commentAddApi = params => fetch('/comment/add', {
  method: 'POST',
  body: JSON.stringify(params),
  headers: { 'Content-Type': 'application/json' }
})

// 用户登录
const loginApi = params => fetch('/user/login', {
  method: 'POST',
  body: JSON.stringify(params),
  headers: { 'Content-Type': 'application/json' }
})

// 用户注册
const registerApi = params => fetch('/user/register', {
  method: 'POST',
  body: JSON.stringify(params),
  headers: { 'Content-Type': 'application/json' }
})

// 用户注销
const logoutApi = () => fetch('/user/logout')

// 用户列表
const userListApi = () => fetch('/user/list')

// 首页
const homeApi = async (id) => {
  let url = '/home'
  if(id){
    const params = new URLSearchParams()
    params.append('categoryId', id)
    url = `${url}?${params.toString()}`
  }
  return await (await fetch(url)).json()
}

// 视频搜索
const videoSearchApi = async (keyword) => {
  let url = '/videohub/search'
  if(keyword){
    const params = new URLSearchParams()
    params.append('keyword', keyword)
    url = `${url}?${params.toString()}`
  }
  return await (await fetch(url)).json()
}

// 删除视频
const videoDelApi = params => fetch(`/videohub/delete/${params.id}`, {
  method: 'DELETE'
})

// 添加视频点击量
const videoAddHitsApi = id => fetch(`/videohub/addhits/${id}`, {
  method: 'PUT'
})

// 视频详情
const videoShowApi = id => fetch(`/videohub/show/${id}`)

// 视频详情
const videoEditApi = id => fetch(`/videohub/edit/${id}`)

// 视频详情
const videoAuditApi = id => fetch(`/videohub/audit/${id}`)

// 视频列表
const videoListApi = () => fetch('/videohub/list')

// 添加视频
const videoAddApi = params => {
  const formData = new FormData()
  Object.entries(params).forEach(([ k, v ]) => formData.append(k, v))
  return fetch('/videohub/add', {
    method: 'POST',
    body: formData,
  })
}

// 编辑视频
const videoHandleEditApi = params => {
  const formData = new FormData()
  Object.entries(params).forEach(([ k, v ]) => {
    if(v){
      formData.append(k, v)
    }
  })
  return fetch('/videohub/edit', {
    method: 'PUT',
    body: formData,
  })
}

// 审核视频
const videoHandleAuditApi = params => fetch('/videohub/audit', {
  method: 'PUT',
  body: JSON.stringify(params),
  headers: { 'Content-Type': 'application/json' }
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
