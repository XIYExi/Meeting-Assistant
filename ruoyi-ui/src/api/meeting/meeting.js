import request from '@/utils/request'

// 查询会议列表
export function listMeeting(query) {
  return request({
    url: '/meeting/meeting/list',
    method: 'get',
    params: query
  })
}

// 查询会议详细
export function getMeeting(id) {
  return request({
    url: '/meeting/meeting/' + id,
    method: 'get'
  })
}

// 新增会议
export function addMeeting(data) {
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
    url: '/meeting/meeting/addImage',
    method: 'post',
    data: fd
  }).then(response => {
    data['url'] = response.msg ? response.msg : 'null';
    request({
      url: '/meeting/meeting/add',
      method: 'post',
      data: data
    })
  })
}

// 修改会议
export function updateMeeting(data) {
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
    url: '/meeting/meeting/edit',
    method: 'post',
    data: fd
  })
}

// 删除会议
export function delMeeting(id) {
  return request({
    url: '/meeting/meeting/' + id,
    method: 'delete'
  })
}
