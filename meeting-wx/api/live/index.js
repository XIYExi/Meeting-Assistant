import upload from '@/utils/upload'
import request from '@/utils/request'

// 查询会议列表（按照时间最近的时间排序）
export function testRequestLive() {
  return request({
    url: '/im/test/info',
    method: 'get'
  })
}


export function startLiving() {
  return request({
    url: `/live/living/startLiving?type=${0}`,
    method: 'post'
  })
}


export function getImConfig() {
	return request({
		url: '/im/im/imConfig',
		method: 'get'
	})
}