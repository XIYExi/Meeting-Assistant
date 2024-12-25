import request from '@/utils/request'

// 查询会议聊天列表
export function listChat(query) {
  return request({
    url: '/meeting/chat/list',
    method: 'get',
    params: query
  })
}

// 查询会议聊天详细
export function getChat(id) {
  return request({
    url: '/meeting/chat/' + id,
    method: 'get'
  })
}

// 新增会议聊天
export function addChat(data) {
  return request({
    url: '/meeting/chat',
    method: 'post',
    data: data
  })
}

// 修改会议聊天
export function updateChat(data) {
  return request({
    url: '/meeting/chat',
    method: 'put',
    data: data
  })
}

// 删除会议聊天
export function delChat(id) {
  return request({
    url: '/meeting/chat/' + id,
    method: 'delete'
  })
}
