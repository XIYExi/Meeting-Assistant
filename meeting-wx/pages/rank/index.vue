<template>
    <view class="ranK_container">

        <img src="@/static/images/rank2.png" class="rank_image"/>

        <view class="rank-wrapper">
            <view class="meta-wrapper">
                <view class="meta-1">排行</view>
                <view class="meta-2">会议</view>
                <view class="meta-3">关注度</view>
            </view>
            <!--rank container-->
            <view v-for="(elem, index) in ranks" :key="index" class="rank-element" @click="navigationToMeeting(elem.id)">
                <view class="image-wrapper">
                    <view v-if="index <= 2">
                        <view v-if="index === 0"><img src="@/static/images/one.png" class="badge-image"/></view>
                        <view v-if="index === 1"><img src="@/static/images/two.png" class="badge-image" /></view>
                        <view v-if="index === 2"><img src="@/static/images/three.png" class="badge-image" /></view>
                    </view>
                    <view v-else>{{ index + 1 }}</view>
                </view>

                <view class="element-wrapper" >
                    <view class="title">{{ elem.title }}</view>
                    <view class="badge-icons-wrapper">
                        <text class="begin-time"><img src="@/static/images/clock.png" class="clock-png" />{{ elem.beginTime }}</text>
                        <text class="meeting-type">{{ eventMeetingTypes[elem.type-1] }}</text>
                        <text class="meeting-open-type">{{ elem.meetingType === 1 ? '线上' : '线下' }}</text>
                    </view>
                </view>

                <view class="view-wrapper" >
                    <view class="views">{{ elem.views }}</view>
                </view>
            </view>
        </view>

    </view>
</template>

<script>
import {getRank} from '@/api/meeting/meeting';
export default({
    onLoad(args) {
        getRank().then(resp => {
            console.log('rank', resp)
            this.ranks = resp.data;
        })
    },
    mounted() {},
    data() {
        return {
            // 排行榜数据，就是meeting，把remark删掉了
            ranks: [],
            eventMeetingTypes: ['技术会议', '学术会议', '行业峰会' ,'新品发布']
        }
    },
    methods: {
        navigationToMeeting(id){
            uni.navigateTo({
                url: `/pages/schedule/detail/index?id=${id}`
            });
        }
    }
})
</script>


<style scoped>
.ranK_container {
    background-image: linear-gradient(to top, #fff1eb 0%, #ace0f9 100%);
    position: relative;
    min-height: 800px;
}

.rank_image {
    width: calc(100% - 40px);
    left: 20px;
    height: 380px;
    top: 4ch;
    position: absolute;
    z-index: 200;
}
.rank-wrapper {
    position: absolute;
    width: 100%;
    min-height: calc(100vh - 360px);
    opacity: 1;
    z-index: 999;
    background-color: white;
    margin-top: -50px;
    padding-top: 20px;
    border-top-left-radius: 20px;
    border-top-right-radius: 20px;
    top: 360px;
}
.meta-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-bottom: 8px;
}
.meta-1 {
    width: 20%;
    text-align: center;
    font-size: 13px;
    color: #999;
}
.meta-2 {
    width: 60%;
    font-size: 13px;
    color: #999;
}
.meta-3 {
    width: 20%;
    text-align: center;
    font-size: 13px;
    color: #999;
}

.rank-element {
    margin-left: 10px;
    margin-right: 10px;
    position: relative;
    display: flex;
    justify-content: flex-start;
    align-items: center;
    height: 65px;
    border-radius: 12px;
    margin-bottom: 10px;
}


.rank-element:nth-child(1),
.rank-element:nth-child(2),
.rank-element:nth-child(3) {
    background-color: #e8f3ffaf;
}

.badge-icons-wrapper {
    display: flex;
    margin-top: 8px;
}

.image-wrapper {
    width: 20%;
    justify-content: center;
    align-items: center;
    display: flex;
}
.element-wrapper {
    width: 80%;
}
.title {
    font-weight: 500;
    font-size: 15px;
    color: #18181B;
}

.view-wrapper {
    width: 20%;
    display: flex;
    justify-content: center;
    align-items: center;
}
.views {
    color: #71717A;
    font-weight: 300;
    font-size: 13px;
}

.badge-image {
    width: 40px;
}

.begin-time {
  font-weight: 400;
  background-color: #E6F1FE;
  padding: 3px 8px;
  border-radius: 12px;;
  color: #006FEE;
  font-weight: 600;
  font-size: 12px;
}
.clock-png {
  width: 12px;
  height: 12px;
  margin-right: 4px;
}

.meeting-type {
    font-weight: 600;
    background-color: #d9506bc8;
    padding: 3px 8px;
    border-radius: 12px;;
    font-size: 12px;
    color: white;
    margin-left: 3px;
}
.meeting-open-type {
    background-color: #32E6B7;
    font-weight: 600;
    padding: 3px 8px;
    border-radius: 12px;;
    font-size: 12px;
    color: white;
    margin-left: 3px;
}
</style>