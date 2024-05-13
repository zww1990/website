import { userListApi } from '../utils/api.js'

const { message } = antd

export default {
  async setup() {
    const res = await userListApi()
    let dataSource = []
    if(res.ok){
      dataSource = await res.json()
    }else{
      message.error(await res.text())
    }
    const columns = [
      { title: '主键', dataIndex: 'id', },
      { title: '用户名', dataIndex: 'username', },
      { title: '昵称', dataIndex: 'nickname', },
      { title: '类型', dataIndex: 'userType', },
      { title: '创建人', dataIndex: 'creator', },
      { title: '创建时间', dataIndex: 'createdDate', },
      { title: '修改人', dataIndex: 'modifier', },
      { title: '修改时间', dataIndex: 'modifiedDate', },
    ]
    return { dataSource, columns }
  },
  template:`
    <a-row>
      <a-col :span="24">
        <a-table
          :dataSource="dataSource"
          :columns="columns"
          bordered
          :pagination="false"
          rowKey="id"
          size="middle"
        />
      </a-col>
    </a-row>
  `
}
