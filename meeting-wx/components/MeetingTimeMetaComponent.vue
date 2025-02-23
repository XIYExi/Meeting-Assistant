<template>
    <view>

        <view class="event-name-wrapper">
            <text class="event-name">{{ title }}</text>
        </view>


        <view class="time-wrapper">
            <view class="left-wrapper">
                <uni-icons type="calendar" size="15"></uni-icons>
                <text class="event-time event-text">开始时间</text>
                <text class="event-time">{{ beginTime }}</text>
            </view>
        </view>

        <view class="location-wrapper">
            <view class="left-wrapper">
                <uni-icons type="location" size="15"></uni-icons>
                <text class="event-time event-text">会议地点</text>
                <text class="event-location">{{ location.formattedAddress }}</text>
            </view>
        </view>
    </view>
</template>


<script>

export default {
    props: {
        beginTime: {
			type: String,
		},
        title: {
            type: String,
        },
        location: {
            type: Object,
        }
    }
}
</script>

<style scoped>
.time-wrapper {
    display: flex;
    padding: 2px 5px;
    margin: 0;
    justify-content: space-between;
    align-items: center;
}
.location-wrapper {
    display: flex;
    padding: 0px 5px;
    margin: 0;
    justify-content: space-between;
    align-items: center;
}
.left-wrapper {
    display: flex;
}
.right-wrapper {
    display: flex;
}


.event-time {
  font-size: 12px;
  color: #666;
  margin-bottom: 5px;
  margin-left: 3px;
}
.event-text {
    margin-right: 5px;
}
.event-name-wrapper {
    margin-bottom: 10px;
}
.event-name {
  font-size: 16px;
  color: #333;
  padding: 2px 5px;
}
.event-location {
  font-size: 12px;
  color: #666;
  margin-bottom: 5px;
  margin-left: 3px;
}
</style>
