<template>
  <view>
    <!-- 背景标题 -->
    <view class="header">
      <view class="header-font">
        <text>海报下载</text>
      </view>
      <uni-icons type="download" size="30" color="white"></uni-icons>
    </view>


    <!-- 会议海报区域 -->
    <view class="poster-wrapper">
      <view class="conference-poster" v-for="(poster, index) in conferencePosters" :key="index"
        @click="showPoster(poster)">
        <image :src="poster.url" class="conference-img"></image>
        <view class="post-text">海报{{ index+1 }}</view>
      </view>
    </view>

    <!-- 灯箱弹出层 -->
    <view class="modal" v-if="showModal" @click.self="closeModal">
      <image :src="currentPoster.url" class="modal-img"></image>
      <button class="download-btn" @click.stop="downloadPoster">下载海报</button>
    </view>
  </view>
</template>

<script>
import { imageDownloadList } from '@/api/meeting/meeting.js';
export default {
  mounted() {
    imageDownloadList().then(resp => {
      const arr = resp.data.filter(elem => elem.type === "COMMON_IMAGE")
      console.log(arr);
      this.conferencePosters = arr;
    })
  },
  data() {
    return {
      conferencePosters: [],
      showModal: false,
      currentPoster: '/static/images/banner/banner07.jpg'
    };
  },
  methods: {
    showPoster(poster) {
      this.currentPoster = poster;
      this.showModal = true;
    },
    closeModal() {
      this.showModal = false;
    },
    downloadPoster() {
      // uni.showToast({ title: '下载功能暂未实现', icon: 'none' });
      uni.downloadFile({
				  url: this.currentPoster.url, // 这里是你接口地址 若要传参 直接 url拼接参数即可
					header:{
						'X-Authorization': uni.getStorageSync('session.login')['token']
					},
					methods: 'GET',
				    success: (res) => {
				    	console.log(res,'res')
						var tempFilePath = res.tempFilePath; // 这里拿到后端返回的图片路径
						uni.saveImageToPhotosAlbum({  // 然后调用这个方法
							filePath: tempFilePath,
							success : (res) => {
								uni.hideLoading();
								uni.showToast({title : '图片已保存'})
							}
						})
				    },
					fail: () => {
						uni.hideLoading();
					}
				});
    }
  }
};
</script>

<style scope>

.header {
  width: 100%;
  height: 180px;
  text-align: center;
  background: #8a2be2;
  padding: 25px 0;
  border-radius: 0 0 30px 30px;
  display: flex;
  flex-direction: row;
  align-items: flex-end;
  justify-content: flex-start;
}

.header-font {
  color: white;
  margin-top: 80px;
  margin-left: 40px;
  font-size: 25px;
  padding-right: 10px;
}

.guest-scroll {
  display: flex;
  width: 100%;
  overflow-x: auto;
  white-space: nowrap;
  padding: 20px 0;
  margin-left: 25px;
}

.guest-item {
  display: inline-flex;
  flex-direction: column;
  align-items: center;
  padding: 15px;
  border-radius: 8px;
  background: #fff;
  margin-right: 15px;
  width: 140px;
  height: 160px;
}

.guest-img {
  width: 90px;
  height: 90px;
  border-radius: 8px;
}

.guest-name {
  font-size: 13px;
  color: #000;
  padding-top: 10px;
}

.guest-job {
  font-size: 12px;
  color: #777;
  padding-top: 5px;
}

.poster-wrapper {
  display: flex;
  width: 100%;
  height: 100%;
  justify-content: flex-start;
  align-items: center;
  margin: 12px;
  flex-wrap: wrap;
  margin-top: 10px;
}
.post-text {
  font-size: 12px;
  text-align: center;
  color: #999;
  margin-top: 5px;
}

.conference-poster {
  width: 40%;
  height: 100px;
  background: #dde3eb;
  margin: 10px;
  border-radius: 10px;
  position: relative;
  margin-top: 25px;
}

.conference-poster image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 10px;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}

.modal-img {
  object-fit: contain;
  margin: auto;
  width: 100%
}

.download-btn {
  margin-top: 20px;
  margin-bottom: 20px;
  padding: 10px 20px;
  background: #007AFF;
  color: white;
  border-radius: 5px;
}
</style>
