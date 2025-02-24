import request from '@/utils/request'

// 查询积分列表
export function listPoints(query) {
  return request({
    url: '/meeting/points/list',
    method: 'get',
    params: query
  })
}

// 查询积分详细
export function getPoints(id) {
  return request({
    url: '/meeting/points/' + id,
    method: 'get'
  })
}

// 新增积分
export function addPoints(data) {
  return request({
    url: '/meeting/points',
    method: 'post',
    data: data
  })
}

// 修改积分
export function updatePoints(data) {
  return request({
    url: '/meeting/points',
    method: 'put',
    data: data
  })
}

// 删除积分
export function delPoints(id) {
  return request({
    url: '/meeting/points/' + id,
    method: 'delete'
  })
}
