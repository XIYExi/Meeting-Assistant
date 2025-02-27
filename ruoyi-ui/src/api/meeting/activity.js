import request from '@/utils/request'

// 查询会议活动列表
export function listActivity(query) {
  return request({
    url: '/meeting/activity/list',
    method: 'get',
    params: query
  })
}

// 查询会议活动详细
export function getActivity(id) {
  return request({
    url: '/meeting/activity/' + id,
    method: 'get'
  })
}

// 新增会议活动
export function addActivity(data, sectorId) {
  data['sectorId'] = sectorId;
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
  return request({
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    url: '/meeting/activity/addImage',
    method: 'post',
    data: fd
  }).then(response => {
    data['url'] = response.msg ? response.msg : 'null';
    request({
      url: '/meeting/activity/add',
      method: 'post',
      data: data
    })
  });
}

// 修改会议活动
export function updateActivity(data) {
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
    url: '/meeting/activity/edit',
    method: 'post',
    data: data
  })
}

// 删除会议活动
export function delActivity(id) {
  return request({
    url: '/meeting/activity/' + id,
    method: 'delete'
  })
}
