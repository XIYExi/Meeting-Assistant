<template>
    <view class="total_map_container">
        <view class="map_container" v-if="loading===false">
            <view class="wrapperBox">
                <map 
                    class="map"
                    :latitude="map.latitude"
                    :longitude="map.longitude"
                    :scale="map.scale"
                    :markers="map.markers"
                    :polyline="map.polyline"
                ></map>
            </view>

            <view class="fixed-footer">
                <view class="footer-wrapper">
                    <view class="position-wrapper">
                        <view class="position-title">{{ geo.formattedAddress }}</view> 
                        <view class="position-meta">{{ geo.country }} · {{ geo.province }} · {{ geo.city }}</view>
                    </view>
        
                    <view class="distance-line-wrapper">
                        <img class="distance-icon" src="static/images/car.png" />
                        <view class="distance-line">距离目的地约<view class="distance-point">{{ distance }}</view>公里</view>
                    </view>

                    <view class="distance-hour-wrapper">
                        <img class="car-icon" src="static/images/car-raw.png" />
                        <view class="distance-hour">驾车约 {{ duration.hours }} 小时 {{ duration.minutes }} 分钟</view>
                    </view>
                </view>
            </view>
        </view>
        <view v-else class="empty-container">
            地图载入中...
        </view>
    </view>
</template>

<script>
import { getGeoMapInfo, calDistance,caloPathPlanning } from '@/api/meeting/meeting.js';

export default({
    onLoad(params) {
        const locationId = params.id;
        const that = this;
        this.loading = true;
        
        uni.getLocation({
            type: 'gcj02',
            isHighAccuracy: true,
            success: function (res) {
                //console.log('当前位置的经度 longitude：' + res.longitude);
                //console.log('当前位置的纬度 latitude：' + res.latitude);
                // get geo map message
                getGeoMapInfo(locationId).then(resp => {
                    that.geo = resp.data;
                    return resp.data;
                })
                .then(geo => {
                    const arr = geo.location.split(",");
                    // 计算直线距离
                    calDistance(`${res.longitude},${res.latitude}`, `${parseFloat(arr[0])},${parseFloat(arr[1])}`).then(resp => {
                        console.log(resp);
                        const result = resp.data.data;
                        if(parseInt(result.count) < 1) {
                            console.log('计算位置错误！');
                            return;
                        }
                        const distanceMessage = result.results[0];
                        let distance = (parseInt(distanceMessage.distance) / 1000).toFixed(1); // 换算成km, 固定一位小数
                        const convertDurationValue = that.convertDuration(parseInt(distanceMessage.duration)); // 原本是s换算成多少小时多少分钟
                        that.duration = convertDurationValue;
                        that.distance = distance;
                        return {geo, distance}
                    })
                    .then(params => {
                        const {geo, distance} = params;
                        // console.log(distance)
                        const arr = geo.location.split(",");
                        caloPathPlanning(`${res.longitude},${res.latitude}`, `${parseFloat(arr[0])},${parseFloat(arr[1])}`)
                        .then(resp => {
                            // console.log('pathPlanning', resp.data[0].points)
                            // 初始化地图缩放
                            const scale = that.calMapScale(distance);
                            that.map = {
                                latitude: parseFloat(arr[1]),
                                longitude: parseFloat(arr[0]),
                                scale: scale, // 设置地图的缩放级别
                                markers: [ // 设置标记点
                                    {
                                        latitude: parseFloat(arr[1]),
                                        longitude: parseFloat(arr[0]),
                                        title: geo.formattedAddress,
                                        iconPath: '/static/images/pin.png',
                                        callout: {
                                            content: geo.formattedAddress,
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
                                polyline: resp.data
                            };
                            // 关闭loading
                            that.loading = false;
                        });
                    });               
                });
                
            }
        });        
    },
    mounted(params) {
       

    },
    data() {
        return {
            geo: {formattedAddress: '测试地图终点', country: '中国', province: '江苏', city: '无锡'},
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
            distance: 128.8,
            duration: {hours: 12, seconds: 12, minutes: 12},
            loading: false,
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
        calMapScale(distance) {
            let scale = 14; // 默认缩放
            if (distance > 1000) {
                scale = 3;
            }
            else if(distance > 900 && distance <= 1000) {
                scale = 4;
            }
            else if (distance > 800 && distance <= 900) {
                scale = 5;
            }
            else if (distance > 700 && distance <= 800) {
                scale = 6;
            }
            else if(distance > 600 && distance <= 700) {
                scale = 7;
            }
            else if(distance > 500 && distance <= 600) {
                scale = 8;
            }
            else if(distance > 400 && distance <= 500) {
                scale = 9;
            }
            else if(distance > 300 && distance <= 400) {
                scale = 10;
            }
            else if (distance > 200 && distance <=300){
                scale = 11;
            }
            else if(distance > 100 && distance <= 200) {
                scale = 12;
            }
            else if(distance > 50 && distance <= 100) {
                scale = 13;
            }
            return scale;
        }
    }
})
</script>


<style scoped>
.total_map_container {
    background-color: white;
}
.map_container {
    position: fixed;
    top: 0;
    left: 0;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}
.empty-container{
    position: fixed;
    top: 0;
    left: 0;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    width: 100vw;
    height: 100vh;
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
.footer-wrapper {
    padding: 20px;
}

.position-wrapper {
    margin-bottom: 10px;
}
.position-title {
    font-size: 20px;
    font-weight: 600;
    margin-top: 5px;
}
.position-meta {
    margin-top: 5px;
    font-size: 14px;
    color: #666;
    font-weight: 300;
}
.distance-wrapper {
    display: flex;
}
/* 距离xxx */
.distance-line-wrapper {
    margin-right: 10px;
    display: flex;
    margin-top: 20px;
    justify-content: flex-start;
    align-items: center;
}
.distance-line {
    font-size:16px;
    color: #333;
    margin-left: 10px;
    font-weight: 500;
    display: flex;
    justify-content: center;
    align-items: center;
}
.distance-point {
    font-weight: 700;
    font-size: 18px;
    margin-left: 5px;
    margin-right: 5px;
}
.distance-icon {
    width: 30px;
    height: 30px;
}

/* 驾车约xxx */
.distance-hour-wrapper {
    display: flex;
    margin-top: 20px;
    background-color: #006FEE;
    justify-content: center;
    align-items: center;
    padding: 13px 10px ;
    border-radius: 16px;
    opacity: 0.95;
    position: absolute;
    width: calc(100% - 40px);
    bottom: 30px;
    left: 20px;
}
.distance-hour {
    color: white;
    font-weight: 600;
    font-size: 15px;
}
.car-icon {
    width: 24px;
    height: 20px;
    margin-right: 10px;
}

.nav-wrapper {

}
</style>