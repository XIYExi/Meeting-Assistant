import upload from '@/utils/upload'
import request from '@/utils/request'

// 查询会议列表（按照时间最近的时间排序）
export function testRequestLive() {
  return request({
    url: '/im/test/info',
    method: 'get'
  })
}
