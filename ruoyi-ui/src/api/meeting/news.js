import request from '@/utils/request'

// 查询新闻管理列表
export function listNews(query) {
  return request({
    url: '/meeting/news/list',
    method: 'get',
    params: query
  })
}

// 查询新闻管理详细
export function getNews(id) {
  return request({
    url: '/meeting/news/' + id,
    method: 'get'
  })
}

// 新增新闻管理
export function addNews(data) {
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
  //   url: '/meeting/news',
  //   method: 'post',
  //   data: data
  // })
  return request({
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    url: '/meeting/news/addImage',
    method: 'post',
    data: fd
  }).then(response => {
    data['url'] = response.msg ? response.msg : 'null';
    request({
      url: '/meeting/news/add',
      method: 'post',
      data: data
    })
  });
}

// 修改新闻管理
export function updateNews(data) {
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
    url: '/meeting/news/edit',
    method: 'post',
    data: data
  })
}

// 删除新闻管理
export function delNews(id) {
  return request({
    url: '/meeting/news/' + id,
    method: 'delete'
  })
}


export function submitNews(data) {
  return request({
    url: '/meeting/news/submitNewEditor',
    method: 'post',
    data: data
  })
}

export function selectNewEditor(id) {
  return request({
    url: `/meeting/news/selectNewEditor?newsId=${id}`,
    method: 'get'
  })
}
