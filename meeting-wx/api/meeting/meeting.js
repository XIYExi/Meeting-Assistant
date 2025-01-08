import upload from '@/utils/upload'
import request from '@/utils/request'

// 查询会议列表（按照时间最近的时间排序）
export function getMeetingListOrderByAsc() {
  return request({
    url: '/meeting/meeting/list',
    method: 'get'
  })
}

// 查询嘉宾
export function getGuestListOrderByDate() {
	return request({
		url: '/meeting/guest/list',
		method: 'get'
	})
}