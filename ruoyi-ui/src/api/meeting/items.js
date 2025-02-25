import request from '@/utils/request'

// 查询积分物品列表
export function listItems(query) {
  return request({
    url: '/meeting/items/list',
    method: 'get',
    params: query
  })
}

// 查询积分物品详细
export function getItems(id) {
  return request({
    url: '/meeting/items/' + id,
    method: 'get'
  })
}

// 新增积分物品
export function addItems(data) {
  const fd = new FormData();
  Object.keys(data).forEach(key => {
    if (data[key] instanceof Array) {
      // 如果是数组就循环加入表单，key保持相同即可，这就是表达单的数组
      data[key].forEach(item => {
        console.log(key, item)
        fd.append(key, item)
      })
    } else {
      // 如果不是数组就直接追加进去
      fd.append(key, data[key])
    }
  });

  return request({
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    url: '/meeting/items/addImage',
    method: 'post',
    data: fd
  }).then(response => {
    console.log('items image add', response)
    data['url'] = response.msg ? response.msg : 'null';
    request({
      url: '/meeting/items/add',
      method: 'post',
      data: data
    })
  })
}

// 修改积分物品
export function updateItems(data) {
  console.log(data)
  const fd = new FormData();
  Object.keys(data).forEach(key => {
    if (data[key] instanceof Array) {
      // 如果是数组就循环加入表单，key保持相同即可，这就是表达单的数组
      data[key].forEach(item => {
        console.log(key, item)
        fd.append(key, item)
      })
    } else {
      // 如果不是数组就直接追加进去
      fd.append(key, data[key])
    }
  });

  return request({
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    url: '/meeting/items/edit',
    method: 'post',
    data: fd
  })
}

// 删除积分物品
export function delItems(id) {
  return request({
    url: '/meeting/items/' + id,
    method: 'delete'
  })
}
