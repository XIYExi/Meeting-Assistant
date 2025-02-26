<template>
  <view class="file-container">
    <!-- 背景标题 -->
    <view class="header">
      <view class="header-font">
        <text>文档下载</text>
      </view>
      <uni-icons type="download" size="30" color="white"></uni-icons>
    </view>

    <view class="file-wrapper">
      <view v-for="(file, index) in files" :key="index" class="file-element-wrapper">
        <view class="file-prev">
          <view class="file-type-image-wrapper">
            <img :src="file.fileType === 'pdf' ? 'static/images/pdf.png' : 'static/images/doc.png'"
              class="file-type-icon" />
          </view>

          <uni-link class="file-title-wrapper" showUnderLine="false" :href="file.url">{{ file.fileName }}</uni-link>
        </view>
      </view>

    </view>


  </view>
</template>

<script>
import { fileDownloadList } from '@/api/meeting/meeting.js';
import uniCol from '../../../uni_modules/uni-row/components/uni-col/uni-col.vue';
import UniLink from '../../../uni_modules/uni-link/components/uni-link/uni-link.vue';

export default {
  components: { uniCol, UniLink },
  mounted() {
    fileDownloadList().then(resp => {
      console.log(resp)
      this.files = resp.data;
    })
  },
  data() {
    return {
      files: []
    };
  },
  methods: {
  }
};
</script>

<style scope>
.file-container {
  background-color: white;
  width: 100%;
  height: 100vh;
}

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

.file-wrapper {
  display: flex;
  align-items: flex-start;
  justify-content: flex-start;
  flex-wrap: wrap;
  width: 100vw;
  margin-top: 10px;
}

.file-element-wrapper {
  width: 26%;
  margin: 12px;
}

.file-prev {
  display: flex;
  flex-direction: column;
}

.file-type-image-wrapper {
  width: 100%;
  height: 130px;
  border-radius: 8px;
  justify-content: center;
  align-items: center;
  display: flex;
  background: #E6F1FE;
  box-shadow: rgba(149, 157, 165, 0.2) 0px 8px 24px;
}

.file-type-icon {
  width: 36px;
}

.header-font {
  color: white;
  margin-top: 80px;
  margin-left: 40px;
  font-size: 25px;
  padding-right: 10px;
}

.file-title-wrapper {
  margin-top: 8px;
  font-size: 12px;
  text-align: center;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  color: #333;
}
</style>
