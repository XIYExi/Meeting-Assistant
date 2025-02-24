<template>
    <view class="map_container">
        <view class="wrapperBox">
            <map 
                class="map"
                :latitude="map.latitude"
                :longitude="map.longitude" 
                :scale="map.scale" 
                :markers="map.markers"
            ></map>
        </view>

        <view class="fixed-footer">
            <view>
                <view>{{ geo.formattedAddress }}</view>
                <view>{{ geo.country }} · {{ geo.province }} · {{ geo.city }}</view>
            </view>

            <view>
                <view>{{ distance }}公里</view>
                <view>{{ duration.hours }} 小时 {{ duration.minutes }} 分钟</view>
            </view>
        </view>

    </view>
</template>

<script>
import {getGeoMapInfo, calDistance } from '@/api/meeting/meeting.js';



export default({
    onLoad(params) {
        const locationId = params.id;
        const that = this;
        uni.getLocation({
            type: 'gcj02',
            isHighAccuracy: true,
            success: function (res) {
                console.log('当前位置的经度：' + res.longitude);
                console.log('当前位置的纬度：' + res.latitude);
        
                // get geo map message
                getGeoMapInfo(locationId).then(resp => {
                    that.geo = resp.data;
                    console.log('geo', resp.data)
                    const arr = resp.data.location.split(",");
                    that.map = {
                        latitude: parseFloat(arr[1]),
                        longitude: parseFloat(arr[0]),
                        scale: 14, // 设置地图的缩放级别
                        markers: [ // 设置标记点
                            {
                                latitude: parseFloat(arr[1]),
                                longitude: parseFloat(arr[0]),
                                title: resp.data.formattedAddress,
                                iconPath: '/static/images/ttts.png',
                                callout: {
                                    content: resp.data.formattedAddress,
                                    color: '#000',
                                    fontSize: 14,
                                    borderRadius: 8,
                                    bgColor: '#fff',
                                    display: 'ALWAYS',
                                    padding: 16,
                                    borderWidth: 0,
                                    borderColor: '#000'
                                }
                            },
                        ],
                    }
                    return arr;
                })
                .then(arr => {
                    //
                    calDistance(`${res.longitude},${res.latitude}`, `${parseFloat(arr[0])},${parseFloat(arr[1])}`).then(resp => {
                        console.log(resp);
                        const result = resp.data.data;
                        if(parseInt(result.count) < 1) {
                            console.log('计算位置错误！');
                            return;
                        }
                        const distanceMessage = result.results[0];
                        let distance = parseInt(distanceMessage.distance) / 1000; // 换算成km
                        const convertDurationValue = that.convertDuration(parseInt(distanceMessage.duration)); // 原本是s换算成多少小时多少分钟
                        that.duration = convertDurationValue;
                        that.distance = distance;
                    })
                })               
            }
        });        
    },
    mounted(params) {
       

    },
    data() {
        return {
            geo: {formattedAddress: null},
            key: 'TK5BZ-DBUC6-OAOS4-ELRVY-M3PIZ-DJFWX', // 换你自己的key
			map: {
				latitude: 39.909,
				longitude: 116.39742,
				scale: 14, // 设置地图的缩放级别
				markers: [ // 设置标记点
					{
						latitude: 39.909,
						longitude: 116.39742,
						title: 'New York City',
						iconPath: '/static/marker.png',
					},
				],
			},
            distance: 0,
            duration: {hours: 0, seconds: 0, minutes: 0},
        }
    },
    methods: {
        convertDuration(duration) {
            // 计算小时数
            const hours = Math.floor(duration / 3600);
            // 计算剩余的秒数
            const remainingSeconds = duration % 3600;
            // 计算分钟数
            const minutes = Math.floor(remainingSeconds / 60);
            // 计算剩余的秒数（如果需要）
            const seconds = remainingSeconds % 60;
            // 返回结果
            return {
                hours: hours,
                minutes: minutes,
                seconds: seconds
            };
        },
    }
})
</script>


<style scoped>
.map_container {
    position: fixed;
    top: 0;
    left: 0;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}
.wrapperBox {
    position: relative;
    width: 100vw;
    height: 100vh;
    z-index: 1;
}

.map {
	width: 100vw;
	height: 100vh;
}

.fixed-footer {
    height: 250px;
    width: 100%;
    position: absolute;
    bottom: 0;
    left: 0;
    background-color: white;
    border-top-left-radius: 16px;
    border-top-right-radius: 16px;
    z-index: 999;
    box-shadow: rgba(17, 17, 26, 0.1) 0px 0px 16px;
}
</style>