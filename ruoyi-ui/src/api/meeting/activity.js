import request from '@/utils/request'

// 查询会议活动列表
export function listActivity(query) {
  return request({
    url: '/meeting/activity/list',
    method: 'get',
    params: query
  })
}

// 查询会议活动详细
export function getActivity(id) {
  return request({
    url: '/meeting/activity/' + id,
    method: 'get'
  })
}

// 新增会议活动
export function addActivity(data, sectorId) {
  data['sectorId'] = sectorId;
  return request({
    url: '/meeting/activity',
    method: 'post',
    data: data
  })
}

// 修改会议活动
export function updateActivity(data) {
  return request({
    url: '/meeting/activity',
    method: 'put',
    data: data
  })
}

// 删除会议活动
export function delActivity(id) {
  return request({
    url: '/meeting/activity/' + id,
    method: 'delete'
  })
}
