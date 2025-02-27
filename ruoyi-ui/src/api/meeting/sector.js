import request from '@/utils/request'

// 查询会议活动板块列表
export function listSector(query) {
  return request({
    url: '/meeting/sector/list',
    method: 'get',
    params: query
  })
}

// 查询会议活动板块详细
export function getSector(id) {
  return request({
    url: '/meeting/sector/' + id,
    method: 'get'
  })
}

// 新增会议活动板块
export function addSector(data) {
  const fd = new FormData();
  Object.keys(data).forEach(key => {
    if (data[key] instanceof Array) {
      // 如果是数组就循环加入表单，key保持相同即可，这就是表达单的数组
      data[key].forEach(item => {
        //console.log(key, item)
        fd.append(key, item)
      })
    } else {
      // 如果不是数组就直接追加进去
      fd.append(key, data[key])
    }
  });
  // return request({
  //   url: '/meeting/sector',
  //   method: 'post',
  //   data: data
  // })
  return request({
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    url: '/meeting/sector/addImage',
    method: 'post',
    data: fd
  }).then(response => {
    data['url'] = response.msg ? response.msg : 'null';
    request({
      url: '/meeting/sector/add',
      method: 'post',
      data: data
    })
  });
}

// 修改会议活动板块
export function updateSector(data) {
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
    url: '/meeting/sector/edit',
    method: 'post',
    data: data
  })
}

// 删除会议活动板块
export function delSector(id) {
  return request({
    url: '/meeting/sector/' + id,
    method: 'delete'
  })
}
