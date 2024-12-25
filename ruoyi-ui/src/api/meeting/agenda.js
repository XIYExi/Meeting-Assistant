import request from '@/utils/request'

// 查询会议议程列表
export function listAgenda(query) {
  return request({
    url: '/meeting/agenda/list',
    method: 'get',
    params: query
  })
}

// 查询会议议程详细
export function getAgenda(id) {
  return request({
    url: '/meeting/agenda/' + id,
    method: 'get'
  })
}

// 新增会议议程
export function addAgenda(data) {
  return request({
    url: '/meeting/agenda',
    method: 'post',
    data: data
  })
}

// 修改会议议程
export function updateAgenda(data) {
  return request({
    url: '/meeting/agenda',
    method: 'put',
    data: data
  })
}

// 删除会议议程
export function delAgenda(id) {
  return request({
    url: '/meeting/agenda/' + id,
    method: 'delete'
  })
}
