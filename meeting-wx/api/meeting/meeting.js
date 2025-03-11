import upload from '@/utils/upload'
import request from '@/utils/request'

export function submitRating(userId, meetingId, score) {
	return request({
		url: `/collection/collect/send?userId=${userId}&meetingId=${meetingId}&score=${score}`,
		method: 'get'
	});
}


// 基于内容推荐
export function content_rec_list(meetingId) {
  return request({
    url: `/rec/demo/content_rec_list?meetingId=${meetingId}`,
    method: 'get'
  });
}


// 静态统计推荐（用于用户没有评分或者没有登录的时候）
export function llmViewStaticRecList() {
  return request({
    url: `/rec/demo/llm_view_static_rec_list`,
    method: 'get'
  });
}


// 用于登录用户并且有评分的时候
export function stream_real_time_rec_list(userId) {
  return request({
    url: `/rec/demo/stream_real_time_rec_list?userId=${userId}`,
    method: 'get'
  });
}



// 不走推荐，只按照时间顺序查找会议列表，会议列表页面用
export function getMeetingListPage() {
	return request({
	  url: '/meeting/meeting/meeting_page',
	  method: 'get'
	});
}


// 查询嘉宾
export function getGuestListOrderByDate() {
	return request({
		url: '/meeting/guest/list',
		method: 'get'
	});
}

export function getBeginTimeList() {
	return request({
		url: '/meeting/meeting/beginTimeList',
		method: 'get'
	});
}

export function getMeetingDetail(id) {
	return request({
		url: `/meeting/meeting/${id}`,
		method: 'get'
	});
}


export function getSimpleMeetingPartUsers(id) {
	return request({
		url:`/meeting/meeting/getPartNumber?id=${id}`,
		method: 'get'
	});
}


export function getMeetingAgendaList(id) {
	return request({
		url:`/meeting/agenda/details?id=${id}`,
		method: 'get'
	});
}

export function recordMeetingView(id) {
	return request({
		url: `/meeting/meeting/view?id=${id}`,
		method: 'get'
	});
}

export function getRank() {
	return request({
		url: '/meeting/meeting/rank',
		method: 'get'
	});
}


export function getGeoMapInfo(id) {
	return request({
		url: `/meeting/geo/getInfo?id=${id}`,
		method: 'get'
	});
}


export function calDistance(origins, distributions) {
	return request({
		url: `/meeting/geo/calDistance?origins=${origins}&distributions=${distributions}`,
		method: 'get'
	});
}

export function caloPathPlanning(origins, distributions) {
	return request({
		url: `/meeting/geo/pathPlanning?origins=${origins}&distributions=${distributions}`,
		method: 'get'
	});
}


export function imageDownloadList() {
	return request({
		url: `/cos/cos/imageDownloadList`,
		method: 'get'
	});
}

export function fileDownloadList() {
	return request({
		url: `/meeting/meeting/clipList`,
		method: 'get'
	});
}


export function getNewsList() {
	return request({
		url: '/meeting/news/getNewsList',
		method: 'get'
	});
}


export function getNewsDetails(newsId) {
	return request({
		url: `/meeting/news/getNewsDetails?newsId=${newsId}`,
		method: 'get'
	});
}


export function getNewsContent(newsId) {
	return request({
		url: `/meeting/news/selectNewEditor?newsId=${newsId}`,
		method: 'get'
	});
}


export function getSectorList() {
	return request({
		url: `/meeting/sector/getSectorList`,
		method: 'get'
	});
}

export function getActivityList(sectorId) {
	return request({
		url: `/meeting/activity/getActivityList?sectorId=${sectorId}`,
		method: 'get'
	});
}


export function getLastOne() {
	return request({
		url: '/meeting/meeting/lastOne',
		method: 'get'
	})
}