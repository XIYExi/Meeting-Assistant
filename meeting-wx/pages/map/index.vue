<template>
    <view class="map_container">
        <view class="wrapperBox">
			<view id="wrapper"></view>
		</view>
    </view>
</template>

<script>
import {getGeoMapInfo } from '@/api/meeting/meeting.js';

// todo demo 需要修改，太丑陋了nnd

let mapObj = null; // 生成组件实例

window.mapInit = function() { // 挂载地图实例
    mapObj = new TMap.Map("wrapper", {
        center: new TMap.LatLng(22.254515, 113.469689), // 地图初始坐标
        zoom: 14, // 缩放等级
        mapStyleId: 'style1', // 地图样式
        zoomControl: false, // 设置是否启用缩放控件
    });
    
    mapObj.on("click", function(evt) { // 地图全局事件
        // 这里是通过获取地图点击的位置，将该位置移动至中心点，后面会用上
        mapObj.setCenter(new TMap.LatLng(evt.latLng.getLat().toFixed(6), evt.latLng.getLng().toFixed(6)))
    })
}


export default({
    onLoad(params) {
        const locationId = params.id;
        getGeoMapInfo(locationId).then(resp => {
            this.geo = resp.data;
        })

        this.loadScrpit();
    },
    mounted(params) {
       

    },
    data() {
        return {
            geo: null,
            key: 'TK5BZ-DBUC6-OAOS4-ELRVY-M3PIZ-DJFWX', // 换你自己的key
        }
    },
    methods: {
        loadScrpit() { // 挂载js
            var script = document.createElement('script');
            script.src =`https://map.qq.com/api/gljs?v=1.exp&key=${this.key}&libraries=visualization&callback=mapInit`;
            document.body.appendChild(script);
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

#wrapper {
    z-index: 20;
    width: 120vw;
    height: 120vh;
    top: -10vh;
    left: -10vw;
    position: absolute;
}

</style>