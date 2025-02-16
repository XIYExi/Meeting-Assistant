

/**
 * 获取，会议开始时间距离现在还有多久，详细页面中倒计时使用
 * @param {会议开始时间} targetTimeStr 
 * @returns 
 */
export function calculateTimeDifference(targetTimeStr) {
    // 将目标时间字符串转换为 Date 对象
    const targetTime = new Date(targetTimeStr);
    const now = new Date(); // 获取当前时间

    // 计算时间差（单位为毫秒）
    const timeDifference = targetTime - now;

    if (timeDifference < 0) {
        console.log("目标时间已经过去！");
        return [];
    }

    // 将时间差转换为天、小时、分钟和秒
    const millisecondsPerDay = 1000 * 60 * 60 * 24;
    const millisecondsPerHour = 1000 * 60 * 60;
    const millisecondsPerMinute = 1000 * 60;

    // 计算天数
    const days = Math.floor(timeDifference / millisecondsPerDay);
    // 剩余的毫秒数
    const remainingMillisecondsAfterDays = timeDifference % millisecondsPerDay;

    // 计算小时数
    const hours = Math.floor(remainingMillisecondsAfterDays / millisecondsPerHour);
    // 剩余的毫秒数
    const remainingMillisecondsAfterHours = remainingMillisecondsAfterDays % millisecondsPerHour;

    // 计算分钟数
    const minutes = Math.floor(remainingMillisecondsAfterHours / millisecondsPerMinute);
    // 剩余的毫秒数
    const remainingMillisecondsAfterMinutes = remainingMillisecondsAfterHours % millisecondsPerMinute;

    // 计算秒数
    const seconds = Math.floor(remainingMillisecondsAfterMinutes / 1000);

    // 输出结果
    //console.log(`距离目标时间还有：${days}天 ${hours}小时 ${minutes}分钟 ${seconds}秒`);
    //console.log(`单独提取的小时数：${hours}`);
    //console.log(`单独提取的分钟数：${minutes}`);
    //console.log(`单独提取的秒数：${seconds}`);
    return [days, hours, minutes, seconds];
}


/**
 * 会议议程中，根据开始和结束时间，拼接 [08:20, 09:30] 这样
 * @param {*} startTimeStr 
 * @param {*} endTimeStr 
 * @returns 
 */
export function formatTimeRange(startTimeStr, endTimeStr) {
    // 定义一个函数来提取小时和分钟
    function extractHourMinute(timeStr) {
        // 使用正则表达式匹配小时和分钟
        const match = timeStr.match(/(\d{2}):(\d{2})/);
        if (match) {
            return `${match[1]}:${match[2]}`; // 格式化为 HH:MM
        }
        return null;
    }

    // 提取开始时间和结束时间的小时和分钟
    const startHourMinute = extractHourMinute(startTimeStr);
    const endHourMinute = extractHourMinute(endTimeStr);

    // 返回格式化后的字符串
    return [startHourMinute, endHourMinute];
}