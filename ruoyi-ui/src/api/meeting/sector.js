import request from '@/utils/request'

// 查询会议活动板块列表
export function listSector(query) {
  return request({
    url: '/meeting/sector/list',
    method: 'get',
    params: query
  })
}

// 查询会议活动板块详细
export function getSector(id) {
  return request({
    url: '/meeting/sector/' + id,
    method: 'get'
  })
}

// 新增会议活动板块
export function addSector(data) {
  return request({
    url: '/meeting/sector',
    method: 'post',
    data: data
  })
}

// 修改会议活动板块
export function updateSector(data) {
  return request({
    url: '/meeting/sector',
    method: 'put',
    data: data
  })
}

// 删除会议活动板块
export function delSector(id) {
  return request({
    url: '/meeting/sector/' + id,
    method: 'delete'
  })
}
