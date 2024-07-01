const { message } = antd

const categoryAddApi = params => fetch('/category/add', {
  method: 'POST',
  body: JSON.stringify(params),
  headers: { 'Content-Type': 'application/json' }
})

const categoryListApi = async () => await (await fetch('/category/list')).json()

const commentAddApi = params => fetch('/comment/add', {
  method: 'POST',
  body: JSON.stringify(params),
  headers: { 'Content-Type': 'application/json' }
})

const loginApi = params => fetch('/user/login', {
  method: 'POST',
  body: JSON.stringify(params),
  headers: { 'Content-Type': 'application/json' }
})

const registerApi = params => fetch('/user/register', {
  method: 'POST',
  body: JSON.stringify(params),
  headers: { 'Content-Type': 'application/json' }
})

const logoutApi = () => axios.get('/user/logout')
.then(res => console.log(res.data, res.status, res.statusText))
.catch(err => console.log(err.message, err.response))

const userListApi = () => axios.get('/user/list')
.then(res => res.data)
.catch(err => {
  console.log(err.message, err.response)
  message.error(err.response.data)
  return []
})

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

const videoSearchApi = keyword => axios.get('/videohub/search', { params: { 'keyword': keyword } })
.then(res => res.data)
.catch(err => {
  console.log(err.message, err.response)
  message.error(err.response.data)
  return []
})

const videoDelApi = params => fetch(`/videohub/delete/${params.id}`, {
  method: 'DELETE'
})

const videoAddHitsApi = id => fetch(`/videohub/addhits/${id}`, {
  method: 'PUT'
})

const videoShowApi = id => fetch(`/videohub/show/${id}`)

const videoEditApi = id => fetch(`/videohub/edit/${id}`)

const videoAuditApi = id => fetch(`/videohub/audit/${id}`)

const videoListApi = () => fetch('/videohub/list')

const videoAddApi = params => {
  const formData = new FormData()
  Object.entries(params).forEach(([ k, v ]) => formData.append(k, v))
  return fetch('/videohub/add', {
    method: 'POST',
    body: formData,
  })
}

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
