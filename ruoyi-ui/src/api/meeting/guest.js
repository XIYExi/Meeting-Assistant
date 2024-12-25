import request from '@/utils/request'

// 查询会议嘉宾列表
export function listGuest(query) {
  return request({
    url: '/meeting/guest/list',
    method: 'get',
    params: query
  })
}

// 查询会议嘉宾详细
export function getGuest(id) {
  return request({
    url: '/meeting/guest/' + id,
    method: 'get'
  })
}

// 新增会议嘉宾
export function addGuest(data) {
  return request({
    url: '/meeting/guest',
    method: 'post',
    data: data
  })
}

// 修改会议嘉宾
export function updateGuest(data) {
  return request({
    url: '/meeting/guest',
    method: 'put',
    data: data
  })
}

// 删除会议嘉宾
export function delGuest(id) {
  return request({
    url: '/meeting/guest/' + id,
    method: 'delete'
  })
}
