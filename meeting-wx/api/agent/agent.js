import request from '@/utils/request'

// 查询会议列表（按照时间最近的时间排序）
export function executeChatMessage(question) {
  return request({
    url: `/agent/agent/chat?question=${question}`,
    method: 'get'
  })
}
