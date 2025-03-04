import request from '@/utils/request'

// 查询会议列表（按照时间最近的时间排序）
export function executeChatMessage(question) {
  return request({
    url: `/agent/agent/chat?question=${question}`,
    method: 'get'
  })
}


export function sendFlux(data) {
  return request({
    url: '/rag/rag/flux',
    method: 'post',
	data: data
  })
}