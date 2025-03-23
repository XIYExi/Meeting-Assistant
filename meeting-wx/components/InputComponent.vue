<template>
  <view class="input-bar">
    <view class="text-btn">
      <view @click="toggleInputMode">
        <image class="talk" :src="isVoiceMode ? '/static/record/mic-icon.png' : '/static/record/keyboard-icon.png'" mode="aspectFill"></image>
      </view>
      
      <!-- 文本输入模式 -->
      <view class="input-wrapper" v-if="!isVoiceMode">
        <input
          class="input-box"
          v-model="inputValue"
          placeholder="请输入内容"
          type="text"
          @keypress.enter="sendMsg"
        />
      </view>
      
      <!-- 语音输入模式 -->
      <view class="voice-wrapper" v-else>
        <touchSpeak  
		  @record-complete="handleRecordComplete"
          @record-cancel="handleRecordCancel"
		/>
      </view>
	  <uni-icons type="plus" size='30' @click="showUtils"></uni-icons>
      <view class="send-btn" @click="sendMsg">
        <text class="btn-text">发送</text>
      </view>
    </view>
	<view v-if="showUtilsMenu" class="utils-menu">
	      <view class="menu-item" @click="handleMenuClick('camera')">
	        <uni-icons type="camera-filled" size="30" color="#666"></uni-icons>
	        <text class="menu-text">拍照</text>
	      </view>
	      <view class="menu-item" @click="handleMenuClick('image')">
	        <uni-icons type="image" size="30" color="#666"></uni-icons>
	        <text class="menu-text">上传图片</text>
	      </view>
	      <view class="menu-item" @click="handleMenuClick('file')">
	        <uni-icons type="folder-add-filled" size="30" color="#666"></uni-icons>
	        <text class="menu-text">上传文件</text>
	      </view>
	    </view>
  </view>
</template>

<script>
import touchSpeak from './touchSpeak.vue'

export default {
  name: 'InputComponent',
  components: { touchSpeak },
  data() {
    return {
      isVoiceMode: false,  // 控制当前模式
      inputValue: this.modelValue,
	  showUtilsMenu: false,
    }
  },
  watch: {
  	modelValue(val) {
  		this.inputValue = val;
  	},
  	inputValue(val) {
  		this.$emit('update:modelValue', val);
  	},
  },
  methods: {
    toggleInputMode() {
      this.isVoiceMode = !this.isVoiceMode
    },
    sendMsg() {
    	if (this.inputValue.trim() === '') return;
    	this.$emit('send-msg', this.inputValue);
    	this.inputValue = '';
    },
     handleRecordComplete(text) {
	  this.isVoiceMode = false; // 切换到输入模式
	  this.inputValue = text; // 填入识别结果
	},
	handleRecordCancel() {
	  this.isVoiceMode = true; // 保持语音模式
	},
	showUtils() {
	  this.showUtilsMenu = !this.showUtilsMenu; // 切换菜单显示
	},
	handleMenuClick(type) {
	  this.showUtilsMenu = false; // 关闭菜单
	  switch(type) {
		case 'camera':
		  this.openCamera();
		  break;
		case 'image':
		  this.pickImage();
		  break;
		case 'file':
		  this.chooseFile();
		  break;
	  }
	},
	openCamera() {
	  uni.chooseImage({
		sourceType: ['camera'],
		success: (res) => {
		  console.log('拍照结果:', res.tempFilePaths);
		}
	  });
	},
	pickImage() {
	  uni.chooseImage({
		success: (res) => {
		  console.log('选择图片:', res.tempFilePaths);
		}
	  });
	},
	chooseFile() {
	  uni.chooseFile({
		success: (res) => {
		  console.log('选择文件:', res.tempFilePaths);
		}
	  });
	}
  }
}
</script>

<style scoped>
.input-bar {
  padding: 10px;
  background: #f8f8f8;
  border-top: 1px solid #ddd;
  margin-top: 88vh;
  position: fixed;
  width: 100%;
}

.text-btn {
  display: flex;
  align-items: center;
  gap: 10px;
}

.talk {
  width: 4vh;
  height: 4vh;
  padding: 8px;
}

.input-wrapper {
  flex: 1;
}

.input-box {
  background: #fff;
  padding: 8px 12px;
  border-radius: 18px;
  height: 30px;
  line-height: 40px;
  box-sizing: border-box; 
}

.voice-wrapper {
  flex: 1;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
  border-radius: 18px;
  overflow: hidden; 
}


.send-btn {
  background: #07c160;
  padding: 8px 16px;
  border-radius: 18px;
}

.btn-text {
  color: white;
  font-size: 14px;
}
.utils-menu {
  position: absolute;
  bottom: 70px;
  left: 10px;
  right: 10px;
  background: white;
  border-radius: 12px;
  padding: 15px;
  display: flex;
  justify-content: space-around;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  z-index: 999;
}

.menu-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 10px;
}

.menu-text {
  font-size: 12px;
  color: #666;
}
</style>