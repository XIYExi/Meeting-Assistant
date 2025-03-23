<template>
	<view :class="longPress == '1' ? 'record-layer' : 'record-layer1'">
		<view  :class="longPress == '1' ? 'record-box' : 'record-box1'">
			<view class="record-btn-layer">
				<button class="record-btn" :class="longPress == '1' ? 'record-btn-1' : 'record-btn-2'" :style="VoiceTitle != '松开手指,取消发送' && longPress != '1'?'background-image: linear-gradient(to top, #cfd9df 0%, #e2ebf0 100%);':'background-color: rgba(0, 0, 0, .5);color:white'"
					@longtap="longpressBtn" @touchend="touchendBtn()" @touchmove="handleTouchMove" @touchstart="longpressBtn">
					<text>{{VoiceText}}</text>
				</button>
			</view>
			<!-- 语音音阶动画 -->
			<view :class="VoiceTitle != '松开手指,取消发送'?'prompt-layer prompt-layer-1':'prompt-layer1 prompt-layer-1'"  v-if="longPress == '2'">
				<view class="prompt-loader">
					<view class="em" v-for="(item,index) in 15" :key="index"></view>
				</view>
				<text class="span">{{VoiceTitle}}</text>
			</view>
		</view>
	</view>
</template>


<script>
import Recorder from 'recorder-core';
import 'recorder-core/src/engine/mp3';
import 'recorder-core/src/engine/mp3-engine';

export default {
  data() {
    return {
      longPress: '1',
      delShow: false,
      time: 0,
      duration: 60000,
      tempFilePath: '',
      startPoint: {},
      sendLock: true,
      VoiceTitle: '松手结束录音',
      VoiceText: '按住 说话',
      recorderManager: null,
      recorderConfig: null,
      isRecording: false,
      isOpening: false // 新增：防止重复请求权限
    };
  },
  mounted() {
    this.recorderManager = Recorder();
    this.recorderConfig = {
      type: "mp3",
      sampleRate: 16000,
      bitRate: 16
    };
  },
  methods: {
    async requestPermission() {
      if (this.isOpening) return false;
      this.isOpening = true;
    
      try {
        // 强制重新初始化，确保实例有效
        this.recorderManager = Recorder();
        await this.recorderManager.open();
        return true;
      } catch (err) {
        uni.showToast({ title: '请授权录音权限', icon: 'none' });
        return false;
      } finally {
        this.isOpening = false;
      }
    },

    async longpressBtn(e) {
      if (!await this.requestPermission()) return;
    
      this.longPress = '2';
      this.VoiceText = '录音中...';
      this.sendLock = false;
    
      try {
        // 添加 100ms 延迟，确保录音设备初始化完成
        await new Promise(resolve => setTimeout(resolve, 100));
        await this.recorderManager.start(this.recorderConfig);
        this.isRecording = true;
        console.log("录音开始");
      } catch (err) {
        console.error("录音启动失败:", err);
        this.isRecording = false;
        this.recorderManager = Recorder(); 
      }
    },


    // touchSpeak.vue 修改后的代码片段
    async handleAudioRecognize(blob) {
      try {
        const reader = new FileReader();
        reader.readAsArrayBuffer(blob);
        reader.onloadend = async () => {
          const arrayBuffer = reader.result;
			const base64 = btoa(
			  new Uint8Array(arrayBuffer).reduce(
				(data, byte) => data + String.fromCharCode(byte), ''
			  )
          );
          try {
            // 调用 uni.request 并解析数组响应
            const [err, res] = await uni.request({
              url: 'http://localhost:3000/asr',
              method: 'POST',
              data: { 
                audio: base64,
                format: 'mp3',
                duration: Math.ceil(blob.size / (16000 * 2))
              },
              header: { 'Content-Type': 'application/json' }
            });
			
			console.log('!!!!!',err);
			console.log('@@@@@',res);
    
            // 错误处理
            if (err) {
              console.error("请求失败:", err);
              this.$emit('record-cancel');
              return;
            }
    
            // 成功时判断 res.data.code
            if (res.data && res.data.code === 0) {
              this.$emit('record-complete', res.data.text);
            } else {
              this.$emit('record-cancel');
            }
          } catch (err) {
            console.error("请求异常:", err);
            this.$emit('record-cancel');
          }
        };
      } catch (err) {
        console.error("识别失败:", err);
        this.$emit('record-cancel');
      }
    },

    async touchendBtn() {
      this.longPress = '1';
      this.VoiceText = '按住 说话';
      this.VoiceTitle = '松手结束录音';
    
      if (!this.isRecording || !this.recorderManager) {
        console.warn("未处于录音状态，忽略 stop 调用");
        return;
      }
    
      try {
        this.isRecording = false; // 先更新状态，防止重复调用
    
        // 手动封装 `stop()` 以适应回调模式
        const { blob, duration } = await new Promise((resolve, reject) => {
          this.recorderManager.stop((blob, duration) => {
            if (!blob) reject(new Error("录音数据为空"));
            else resolve({ blob, duration });
          }, reject);
        });
    
        if (duration < 1000) {
          uni.showToast({ title: '录音时间太短', icon: 'none' });
          return;
        }
        this.handleAudioRecognize(blob);
      } catch (err) {
        console.error("录音停止失败:", err);
      } finally {
        this.recorderManager = Recorder(); // 确保 recorder 重新初始化
      }
    },

    
  async handleTouchMove(e) {
    // 获取起始触点坐标（补充缺失的起点记录）
    if (!this.startPoint.clientY) {
      this.startPoint = { clientY: e.touches[0].clientY };
    }
  
    const moveY = e.touches[0].clientY - this.startPoint.clientY;
    if (Math.abs(moveY) > 70) {
      this.VoiceTitle = '松开手指,取消发送';
      this.VoiceText = '松开手指,取消发送';
      this.sendLock = true;
  
      // 异步停止录音并处理错误
      if (this.isRecording && this.recorderManager) {
        try {
          await this.recorderManager.stop(); // 等待录音完全停止
          this.isRecording = false;
          this.recorderManager = Recorder(); // 强制重置实例
          this.$emit('record-cancel'); // 触发取消事件
        } catch (err) {
          console.error("取消录音失败:", err);
          uni.showToast({ title: '取消失败，请重试', icon: 'none' });
        }
      }
    } else {
      this.VoiceTitle = '松手结束录音';
      this.VoiceText = '松手结束录音';
      this.sendLock = false;
    }
  },

    resetState() {
      this.isRecording = false;
      this.isOpening = false;
      this.longPress = '1';
      this.VoiceText = '按住 说话';
      this.VoiceTitle = '松手结束录音';
    }
  }
};
</script>

<style>
	.record-container {
	  width: 100%;
	  position: relative;
	  box-sizing: border-box;
	}

	.record-layer {
	  width: 100%;
	  height: auto;
	  box-sizing: border-box;
	  position: relative;
	  margin: 0;
	  background: transparent;
	}
	.record-layer1 {
	  width: 100%;
	  height: 100%;
	  position: absolute;
	  background-color: rgba(0, 0, 0, 0.6);
	  z-index: 10;
	  top: 0;
	  left: 0;
	}

	/* 按钮容器 */
	.record-box {
	  width: 100%;
	  position: relative;
	  display: flex;
	  justify-content: center;
	  align-items: center;
	}

	/* 按钮通用样式 */
	.record-btn-layer {
	  width: 100%;
	  position: relative;
	}

	.record-btn-layer button {
	  font-size: 14px;
	  line-height: 1.5;
	  width: 100%;
	  height: 50px;
	  border-radius: 8px;
	  display: flex;
	  align-items: center;
	  justify-content: center;
	  transition: all 0.3s;
	  border: none;
	  outline: none;
	}

	/* 正常状态按钮 */
	.record-btn-1 {
	  background: linear-gradient(to right, #43e97b 0%, #38f9d7 100%);
	  color: #000 !important;
	}

	/* 按下状态按钮 */
	.record-btn-2 {
	  background: rgba(0, 0, 0, 0.5) !important;
	  color: white !important;
	  border-radius: 25px !important;
	  height: 50px !important;
	}

	/* 提示层通用样式 */
	.prompt-layer,
	.prompt-layer1 {
	  position: absolute;
	  left: 50%;
	  transform: translateX(-50%);
	  top: -80px;
	  z-index: 100;
	  width: 150px;
	  border-radius: 15px;
	  padding: 8px 16px;
	  box-sizing: border-box;
	  text-align: center;
	  display: flex;
	  flex-direction: column;
	  align-items: center;
	}

	.prompt-layer::after,
	.prompt-layer1::after {
	  content: '';
	  display: block;
	  border: 12px solid transparent;
	  position: absolute;
	  bottom: -24px;
	  left: 50%;
	  transform: translateX(-50%);
	}

	/* 正常录音提示 */
	.prompt-layer-1 {
	  background: #95eb6c;
	}

	.prompt-layer-1::after {
	  border-top-color: #95eb6c;
	}

	/* 取消录音提示 */
	.prompt-layer1 {
	  background: #fb5353;
	}

	.prompt-layer1::after {
	  border-top-color: #fb5353;
	}

	/* 语音波动动画 */
	.prompt-loader {
	  width: 96px;
	  height: 20px;
	  display: flex;
	  align-items: center;
	  justify-content: space-between;
	  margin-bottom: 6px;
	}

	.prompt-loader .em {
	  display: block;
	  background: #333;
	  width: 1px;
	  height: 10%;
	  margin-right: 2.5px;
	  animation: load 2.5s infinite linear;
	}

	/* 手动展开Less循环生成的动画延迟 */
	.prompt-loader .em:nth-child(1) { animation-delay: 2.8s; }
	.prompt-loader .em:nth-child(2) { animation-delay: 2.6s; }
	.prompt-loader .em:nth-child(3) { animation-delay: 2.4s; }
	.prompt-loader .em:nth-child(4) { animation-delay: 2.2s; }
	.prompt-loader .em:nth-child(5) { animation-delay: 2.0s; }
	.prompt-loader .em:nth-child(6) { animation-delay: 1.8s; }
	.prompt-loader .em:nth-child(7) { animation-delay: 1.6s; }
	.prompt-loader .em:nth-child(8) { animation-delay: 1.4s; }
	.prompt-loader .em:nth-child(9) { animation-delay: 1.2s; }
	.prompt-loader .em:nth-child(10) { animation-delay: 1.0s; }
	.prompt-loader .em:nth-child(11) { animation-delay: 0.8s; }
	.prompt-loader .em:nth-child(12) { animation-delay: 0.6s; }
	.prompt-loader .em:nth-child(13) { animation-delay: 0.4s; }
	.prompt-loader .em:nth-child(14) { animation-delay: 0.2s; }
	.prompt-loader .em:nth-child(15) { animation-delay: 0s; }

	@keyframes load {
	  0%, 100% { height: 10%; }
	  50% { height: 100%; }
	}

	/* 文本样式 */
	.span {
	  color: rgba(0, 0, 0, 0.6);
	  font-size: 12px;
	}
</style>