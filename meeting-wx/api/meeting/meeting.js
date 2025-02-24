import upload from '@/utils/upload'
import request from '@/utils/request'

// 查询会议列表（按照时间最近的时间排序）
export function getMeetingListOrderByAsc() {
  return request({
    url: '/meeting/meeting/list',
    method: 'get'
  })
}

// 查询嘉宾
export function getGuestListOrderByDate() {
	return request({
		url: '/meeting/guest/list',
		method: 'get'
	})
}

export function getBeginTimeList() {
	return request({
		url: '/meeting/meeting/beginTimeList',
		method: 'get'
	})
}

export function getMeetingDetail(id) {
	return request({
		url: `/meeting/meeting/${id}`,
		method: 'get'
	})
}


export function getSimpleMeetingPartUsers(id) {
	return request({
		url:`/meeting/meeting/getPartNumber?id=${id}`,
		method: 'get'
	})
}


export function getMeetingAgendaList(id) {
	return request({
		url:`/meeting/agenda/details?id=${id}`,
		method: 'get'
	})
}

export function recordMeetingView(id) {
	return request({
		url: `/meeting/meeting/view?id=${id}`,
		method: 'get'
	})
}

export function getRank() {
	return request({
		url: '/meeting/meeting/rank',
		method: 'get'
	})
}


export function getGeoMapInfo(id) {
	return request({
		url: `/meeting/geo/getInfo?id=${id}`,
		method: 'get'
	})
}


export function calDistance(origins, distributions) {
	return request({
		url: `/meeting/geo/calDistance?origins=${origins}&distributions=${distributions}`,
		method: 'get'
	})
}