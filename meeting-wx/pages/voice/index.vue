<template>
	<view>
		<view style="padding:5px 10px 0">
			<view style="font-size:24px;color:#f60">Recorder App uni-app测试</view>
			<view style="font-size:13px">TTS test page</view>
		</view>
		
		<MainRec ref="mainRec"></MainRec>
		
		<view style="margin-top:80px;height:10px;background:#eee"></view>
		<!-- <testPerfRenderjs></testPerfRenderjs> -->
		<view style="padding-top:80px"></view>
	</view>
</template>

<script>
import mainRec from './main_rec.vue';

export default {
	components:{
		MainRec: mainRec
	},
	mounted(){
		//onShow可能比mounted先执行，页面准备好了时再执行一次
		this.$refs.mainRec.uniPage__onShow();
	},
	onShow(){
		//使用到录音的页面onShow时必须进行调用；如果是在组件中录音，就由页面转发进去
		if(this.$refs.mainRec){//onShow可能比mounted先执行，页面可能还未准备好
			this.$refs.mainRec.uniPage__onShow();
		}
	}
}
</script>
