import request from '@/utils/request'

// 查询会议聊天列表
export function cosUploadFile(query) {
  return request({
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    url: '/cos/file/upload',
    method: 'post',
    data: query
  })
}
