<template>
	<view class="main">
		<!-- 遮罩层 -->
		<view 
			v-if="showPreview" 
			class="preview-mask"
		>
			<image 
				:src="currentImage" 
				class="preview-image"
				mode="widthFix"
			/>
			<button @click="closePreview">关闭</button>
		</view>
		
		<view class="topbar">
			Image
		</view>
		<view class="imagelist">
			<view 
				class="content" 
				v-for="image in imageList" 
				:key="image.id" 
				@click="openPreview(image)"
			>
				<img :src="image.image" alt="" class="img"/>
				<text class="title">{{image.title}}</text>
			</view>
		</view>
	</view>
</template>

<script>
	export default{
		name:'imageList',
		data(){
			return {
				showPreview: false,
				currentImage: '',
				imageList:[
					{id:1, title:'标题', image:"/static/news.png"},
					{id:2, title:'标题', image:"/static/news.png"},
					{id:3, title:'标题', image:"/static/news.png"},
					{id:4, title:'标题', image:"/static/news.png"},
					{id:5, title:'标题', image:"/static/news.png"}
				]
			}
		},
		methods:{
			openPreview(image){
				this.currentImage = image.image
				this.showPreview = true
			},
			closePreview(){
				this.showPreview = false
				this.currentImage = ''
			},
		}
	}
</script>

<style scoped>
	.main {
		padding: 10px; 
	}
	.preview-mask {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		z-index: 999;
		backdrop-filter: blur(8px);
	}
	.preview-image {
		max-width: 90%;
		max-height: 80vh;
		margin-bottom: 10vh;
		transition: all 0.3s ease;
	}
	.topbar {
		font-family: Georgia, 'Times New Roman', Times, serif;
		font-style: italic;
		font-size: 7vw;
		text-decoration-line: underline;
		text-underline-offset: 1vh;
		margin-bottom: 2.5vh;
	}
	.imagelist {
		display: flex;
		flex-wrap: wrap;
		justify-content: space-between;
	}
	.content {
		width: 45%;
		margin-bottom: 2vh;
		background-color: #f7f7f7;
		display: flex;
		flex-direction: column;
		align-items: center;
		border-radius: 10px;
	}
	.img {
		width: 90%;
		margin-top: 1vh;
		margin-bottom: 1vh;
	}
	.title { 
		margin-bottom: 1vh; 
	}
</style>