import request from '@/utils/request'

// 查询图片cos列表
export function listImage(query) {
  return request({
    url: '/cos/image/list',
    method: 'get',
    params: query
  })
}

// 查询图片cos详细
export function getImage(id) {
  return request({
    url: '/cos/image/' + id,
    method: 'get'
  })
}

// 新增图片cos
export function addImage(data) {
  return request({
    url: '/cos/image',
    method: 'post',
    data: data
  })
}

// 修改图片cos
export function updateImage(data) {
  return request({
    url: '/cos/image',
    method: 'put',
    data: data
  })
}

// 删除图片cos
export function delImage(id) {
  return request({
    url: '/cos/image/' + id,
    method: 'delete'
  })
}
