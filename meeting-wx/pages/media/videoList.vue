<template>
	<view class="main">
		<view class="topbar">
			Video
		</view>
		<view class="carousel-container">
		    <swiper 
			  :autoplay="true"
		      :interval="3000"
		      :duration="500"
		      circular
			  class="carousel"
			  previous-margin='10vw'
			  next-margin='10vw'
			  >
		      <swiper-item v-for="video in videos" :key="video.id">
		        <view class="item-container" @click="handleToPlay">
		        	<img :src="video.image" class="videoImg"/>
					<text class="videoTitle">{{video.title}}</text>
					<text class="videoContent">{{video.content}}</text>
		        </view>
		      </swiper-item>
		    </swiper>
		  </view>
		  <personRec/>
	</view>
</template>

<script>
	import personRec from '@/components/personRec.vue';
	export default {
	  name:'videoList',
	  data() {
	    return {
	      videos:[
			  { id: 1 , image: "/static/news.png", title: "2024西湖论剑大会精彩瞬间", content: "2024西湖论剑大会精彩瞬间"},
			  { id: 2 , image: "/static/news.png", title: "2024西湖论剑大会精彩瞬间", content: "2024西湖论剑大会精彩瞬间"},
			  { id: 3 , image: "/static/news.png", title: "2024西湖论剑大会精彩瞬间", content: "2024西湖论剑大会精彩瞬间"},
			  { id: 4 , image: "/static/news.png", title: "2024西湖论剑大会精彩瞬间", content: "2024西湖论剑大会精彩瞬间"},
			  { id: 5 , image: "/static/news.png", title: "2024西湖论剑大会精彩瞬间", content: "2024西湖论剑大会精彩瞬间"}
		  ],
	    }
	  },
	  components:{
	  	personRec,
	  },
	  methods:{
		  handleToPlay() {
		    this.$tab.navigateTo('/pages/media/videoPlay')
		  },
	  }
	};
</script>

<style>
	.main{
		padding: 10px;
	}
	.topbar {
		font-family: Georgia, 'Times New Roman', Times, serif;
		font-style: italic;
		font-size: 7vw;
		text-decoration-line: underline;
		text-underline-offset: 1vh;
		margin-bottom: 2.5vh;
		margin-top: 1vh;
	}
	.carousel-container {
	  height: 30vh; 
	}
	
	.carousel {
	  height: 100% !important;
	}
	
	.item-container {
	  background-color: white;
	  display: flex;
	  flex-direction: column;
	  align-items: center;
	  width: 70vw;
	  height: 95%;
	  border-radius: 10px;
	  box-shadow: 0 10px 10px rgba(0,0,0,0.1);
	  overflow: hidden;
	  transition: transform 0.3s ease;
	}
	
	.videoImg {
	  width: 100%;
	  object-fit: cover;
	  border-radius: 10px 10px 10px 10px;
	}
	
	.item-container text {
	  padding: 10px;
	  width: 100%;
	  box-sizing: border-box;
	}
	
	.videoTitle { 
	  font-size: 4.5vw;
	  font-weight: bold;
	  color: #333;
	  white-space: nowrap;
	  overflow: hidden;
	  text-overflow: ellipsis;
	}
	
	.videoContent {
	  color: #666;
	  display: -webkit-box;
	  -webkit-box-orient: vertical;
	  -webkit-line-clamp: 2;
	  overflow: hidden;
	  line-height: 1.4;
	}
	
</style>