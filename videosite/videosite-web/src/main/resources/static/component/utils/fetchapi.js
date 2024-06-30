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

const logoutApi = () => fetch('/user/logout')

const userListApi = () => fetch('/user/list')

const homeApi = async (id) => {
  let url = '/home'
  if(id){
    const params = new URLSearchParams()
    params.append('categoryId', id)
    url = `${url}?${params.toString()}`
  }
  return await (await fetch(url)).json()
}

const videoSearchApi = async (keyword) => {
  let url = '/videohub/search'
  if(keyword){
    const params = new URLSearchParams()
    params.append('keyword', keyword)
    url = `${url}?${params.toString()}`
  }
  return await (await fetch(url)).json()
}

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
