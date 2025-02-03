<template>
	<view class="container">
		<view>
			<text>hello</text>
		</view>
		
		<view class="diagnosis">

			<view class="start" @tap="recReq">
				<uni-icons type="mic-filled" size="30"></uni-icons>
				请求录音权限
			</view>


	
			<!-- /录制按钮 -->
			<view class="start" @tap="startRecord()">
				<uni-icons type="mic-filled" size="30"></uni-icons>
				开始录音
			</view>

			<view >
				<!-- 进度条效果 -->
				<!-- <view>进度条</view> -->
				<!-- 回放以及上传按钮 -->
				<view class="butt" @tap="playVoice()">
					<uni-icons type="sound-filled" size="30"></uni-icons>
					回放录音
				</view>
				<view class="butt" @tap="submitrecord()">
					<uni-icons type="upload-filled" size="30"></uni-icons>
					上传录音
				</view>
			</view>
			
			<!-- 弹出层 -->
			<view>
				<uni-popup ref="popup" :mask-click="false" type="center" background-color="#fff">
					<view class="time">
						
					</view>
					<view class="start" @tap="endRecord()">
						<uni-icons type="micoff-filled" size="30"></uni-icons>
						停止录音
					</view>
				</uni-popup>
			</view>
		</view>
		
	</view>
</template>

<script>
import Recorder from 'recorder-core';
// #ifdef H5 || MP-WEIXIN
	//按需引入需要的录音格式编码器，用不到的不需要引入，减少程序体积；H5、renderjs中可以把编码器放到static文件夹里面用动态创建script来引入，免得这些文件太大
	import 'recorder-core/src/engine/mp3.js'
	import 'recorder-core/src/engine/mp3-engine.js'
	import 'recorder-core/src/engine/wav.js'
	import 'recorder-core/src/engine/pcm.js'
	import 'recorder-core/src/engine/g711x'

	//可选引入可视化插件
	import 'recorder-core/src/extensions/waveview.js'
	import 'recorder-core/src/extensions/wavesurfer.view.js'

	import 'recorder-core/src/extensions/frequency.histogram.view.js'
	import 'recorder-core/src/extensions/lib.fft.js'
	
	//实时播放语音，仅支持h5
	import 'recorder-core/src/extensions/buffer_stream.player.js'
	//测试用根据简谱生成一段音乐
	import 'recorder-core/src/extensions/create-audio.nmn2pcm.js'
// #endif
/** 引入RecordApp **/
import RecordApp from 'recorder-core/src/app-support/app.js'
//【所有平台必须引入】uni-app支持文件
import '@/uni_modules/Recorder-UniCore/app-uni-support.js'

var disableOgg=false;
// #ifdef MP-WEIXIN
	//可选引入微信小程序支持文件
	import 'recorder-core/src/app-support/app-miniProgram-wx-support.js'
	disableOgg=true; //小程序不测试ogg js文件太大
// #endif

// #ifdef H5 || MP-WEIXIN
	//H5、renderjs中可以把编码器放到static文件夹里面用动态创建script来引入，免得这些文件太大
	import 'recorder-core/src/engine/beta-amr'
	import 'recorder-core/src/engine/beta-amr-engine'
// #endif
// #ifdef H5
	//app、h5测试ogg，小程序不测试ogg js文件太大
	import 'recorder-core/src/engine/beta-ogg'
	import 'recorder-core/src/engine/beta-ogg-engine'
// #endif
RecordApp.UniNativeUtsPlugin = {
	nativePlugin: true
};

export default {
    components: {

    },
	data() {
		return {
			recType: 'mp3',
            recSampleRate: 16000,
            recBitRate: 16,

            takeoffEncodeChunkSet: false,
            takeoffEncodeChunkMsg: '',
            useAEC:false,
            useANotifySrv: true,
            appUseH5Rec: false,
            showUpload: false,

            recwaveChoiceKey: 'WaveView',
            recpowerx: 0,
            recpowert: "",
            pageDeep: 0,
            pageNewPath: 'main_rec',
            disableOgg: disableOgg,
            evalExecCode: '',
            
            testMsgs: [],
            reclogs: [],
            recloglast: "",
		}
	},
	mounted() {
		var vueVer = [];
		var vv = typeof(Vue) != "undefined" && Vue && Vue.version;
		if (vv) vueVer.push("Vue.version:" + vv);
		var v3 = (((this.$ || {}).appContext || {}).app || {}).version;
		if (v3) vueVer.push("appContext.app.version:" + v3);
		var v2 = (((this.$root || {}).constructor || {}).super || {}).version;
		if (v2) vueVer.push("constructor.super:" + v2);
		this.reclog("页面mounted(" + getCurrentPages().length + "层)" +
			"，Vue=" + vueVer.join("/") +
			"，WebViewId=" + (this.$root.$page && this.$root.$page.id || "?") +
			"，ComponentId=_$id:" + (this._$id || "?") + "/" + "$.uid:" + (this.$ && this.$.uid || "?") +
			"，Recorder.LM=" + Recorder.LM +
			"，RecordApp.LM=" + RecordApp.LM +
			"，UniSupportLM=" + RecordApp.UniSupportLM +
			"，UniJsSource=" + RecordApp.UniJsSource.IsSource);
		
		
		this.isMounted=true;
        this.uniPage__onShow(); //onShow可能比mounted先执行，页面准备好了时再执行一次
		
		
		RecordApp.Install(()=>{
			this.reclog("Install成功，环境："+this.currentKeyTag(),2);
			this.reclog("请先请求录音权限，然后再开始录音");
		},(err)=>{
			this.reclog("RecordApp.Install出错："+err,1);
		});
	},
    destroyed() {
        RecordApp.Stop(); //清理资源，如果打开了录音没有关闭，这里将会进行关闭
    },
    onShow() {
         // onShow可能比mounted先执行，页面可能还未准备好
        if(this.isMounted) 
            this.uniPage__onShow();
    },
	methods: {
        uniPage__onShow() {
            RecordApp.UniPageOnShow(this);
        },
        currentKeyTag() {
        	if (!RecordApp.Current) return "[?]";
        	// #ifdef APP
        	var tag2 = "Renderjs+H5";
        	if (RecordApp.UniNativeUtsPlugin) {
        		tag2 = RecordApp.UniNativeUtsPlugin.nativePlugin ? "NativePlugin" : "UtsPlugin";
        	}
        	return RecordApp.Current.Key + "(" + tag2 + ")";
        	// #endif
        	return RecordApp.Current.Key;
        },
		reclog(msg, color, set) {
			var now = new Date();
			var t = ("0" + now.getHours()).substr(-2) +
				":" + ("0" + now.getMinutes()).substr(-2) +
				":" + ("0" + now.getSeconds()).substr(-2);
			var txt = "[" + t + "]" + msg;
			if (!set || !set.noLog) console.log(txt);
			this.reclogs.splice(0, 0, {
				txt: txt,
				color: color
			});
			this.reclogLast = {
				txt: txt,
				color: color
			};
		},
		// 请求录音权限
		recReq() {
			if(this.appUseH5Rec) {
				RecordApp.UniNativeUtsPlugin = null;
			}
			else {
				RecordApp.UniNativeUtsPlugin = {
					nativePlugin: true
				};
				RecordApp.UniCheckNativeUtsPluginConfig(); //可以检查一下原生插件配置是否有效
				RecordApp.UniNativeUtsPlugin_JsCall = (data) => { //可以绑定原生插件的jsCall回调
					if (data.action == "onLog") { //显示原生插件日志信息
						this.reclog("[Native.onLog][" + data.tag + "]" + data.message, data.isError ? 1 : "#bbb", {
							noLog: 1
						});
					}
				};
			}

			if (this.useAEC) { //这个是Start中的audioTrackSet配置，在h5（H5、App+renderjs）中必须提前配置，因为h5中RequestPermission会直接打开录音
				RecordApp.RequestPermission_H5OpenSet = {
					audioTrackSet: {
						noiseSuppression: true,
						echoCancellation: true,
						autoGainControl: true
					}
				};
			}

			this.reclog("正在请求录音权限...");
			RecordApp.UniWebViewActivate(this); //App环境下必须先切换成当前页面WebView
			RecordApp.RequestPermission(() => {
				this.reclog(this.currentKeyTag() + " 已获得录音权限，可以开始录音了", 2);
				if (this.reqOkCall) this.reqOkCall();
				this.reqOkCall = null; //留别的组件内调用的回调
			}, (msg, isUserNotAllow) => {
				if (isUserNotAllow) { //用户拒绝了录音权限
					//这里你应当编写代码进行引导用户给录音权限，不同平台分别进行编写
				}
				this.reclog(this.currentKeyTag() + " " +
					(isUserNotAllow ? "isUserNotAllow," : "") + "请求录音权限失败：" + msg, 1);
			});
		},
	}
}
</script>

<style>
.container {
	padding: 20px 10px;
	height: auto;
	min-height: 100vh;
	
}

.diagnosis {
	width: 86%;
	margin: auto;
}

.recordinput {
	display: inline-block;
	width: 80%;
	margin: 300rpx 20rpx 100rpx 50rpx;
}

.start {
	width: 50%;
	margin: 50rpx auto;
	font-size: 16px;
	color: #666666;
	height: 80rpx;
	border: 1px solid;
	display: flex;
	flex-direction: row;
	align-items: center;
	border-color: #b8b8b8;
	border-radius: 10rpx;
	font-family: "楷体";
	justify-content: center;

}

.start image {
	margin-left: 10rpx;
	margin-right: 10rpx;
	width: 35rpx;
	height: 35rpx;
}

.butt {
	width: 50%;
	margin: 50rpx auto;
	font-size: 16px;
	color: #666666;
	height: 80rpx;
	border: 1px solid;
	display: flex;
	flex-direction: row;
	align-items: center;
	border-color: #b8b8b8;
	border-radius: 10rpx;
	font-family: "楷体";
	justify-content: center;
}


.time {
	width: 50%;
	margin: 10rpx auto;
	font-size: 22px;
	height: 80rpx;
	display: flex;
	flex-direction: row;
	align-items: center;
	font-family: "楷体";
	justify-content: center;
}
</style>