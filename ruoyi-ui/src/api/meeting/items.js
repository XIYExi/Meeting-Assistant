import request from '@/utils/request'

// 查询积分物品列表
export function listItems(query) {
  return request({
    url: '/meeting/items/list',
    method: 'get',
    params: query
  })
}

// 查询积分物品详细
export function getItems(id) {
  return request({
    url: '/meeting/items/' + id,
    method: 'get'
  })
}

// 新增积分物品
export function addItems(data) {
  return request({
    url: '/meeting/items',
    method: 'post',
    data: data
  })
}

// 修改积分物品
export function updateItems(data) {
  return request({
    url: '/meeting/items',
    method: 'put',
    data: data
  })
}

// 删除积分物品
export function delItems(id) {
  return request({
    url: '/meeting/items/' + id,
    method: 'delete'
  })
}
