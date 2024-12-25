import request from '@/utils/request'
import {parseTime} from "../../utils/ruoyi";

// 查询会议议程列表
export function listAgenda(query) {
  return request({
    url: '/meeting/agenda/list',
    method: 'get',
    params: query
  })
}

// 查询会议议程详细
export function getAgenda(id) {
  return request({
    url: '/meeting/agenda/' + id,
    method: 'get'
  })
}

// 新增会议议程
export function addAgenda(data, meetingId) {
  data['meetingId'] = parseInt(meetingId);
  // 这是element ui最唐的地方
  data['beginTime'] = parseTime(data['beginTime']);
  data['endTime'] = parseTime(data['endTime']);
  return request({
    url: '/meeting/agenda',
    method: 'post',
    data: data
  })
}

// 修改会议议程
export function updateAgenda(data) {
  data['beginTime'] = parseTime(data['beginTime']);
  data['endTime'] = parseTime(data['endTime']);
  return request({
    url: '/meeting/agenda',
    method: 'put',
    data: data
  })
}

// 删除会议议程
export function delAgenda(id) {
  return request({
    url: '/meeting/agenda/' + id,
    method: 'delete'
  })
}
