import request from '@/utils/request'

/**
 * 查询当前用户的积分余额
 * @param {} userId 
 * @returns 
 */
export function getUserWalletPoint(userId) {
    return request({
        url: `/meeting/points/wallet?userId=${userId}`,
        method: 'get'
    })
}


/**
 * 查询积分可以兑换的items
 * @returns 
 */
export function getPointItems() {
    return request({
        url: '/meeting/items/list',
        method: 'get'
    })
}


/**
 * 查询当前用户今日可以完成的任务
 * @param {*} userId 
 */
export function getUserPointList(userId) {
    return request({
        url: `/meeting/points/getUserPointList?userId=${userId}`,
        method: 'get'
    })
}


/**
 * 用户提交任务 后端检查是否可以提交
 * @param {*} pointId 
 * @param {*} userId 
 * @returns 
 */
export function submitTaskForPoint(pointId, userId) {
    return request({
        url: `/meeting/points/submitTaskForPoint?pointId=${pointId}&userId=${userId}`,
        method: 'get'
    })
}


/**
 * 查询当前用户的积分获得记录
 * @param {*} userId 
 * @returns 
 */
export function getHistoryPointRecords(userId) {
    return request({
         url: `/meeting/points/getHistoryPointRecords?userId=${userId}`,
        method: 'get'
    })
}


/**
 * 每日签到，个人主页，快速签到
 * @param {*} userId 
 * @returns 
 */
export function dailySignIn(userId) {
    return request({
        url: `/meeting/points/dailySignIn?userId=${userId}`,
       method: 'get'
   })
}


/**
 * 兑换物品出库
 * @param {*} userId 
 * @param {*} itemId 
 * @returns 
 */
export function itemExchange(userId, itemId) {
    return request({
        url: `/meeting/items/itemExchange?userId=${userId}&itemId=${itemId}`,
       method: 'get'
    })
}