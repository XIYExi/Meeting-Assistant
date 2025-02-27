<template>
	<view class="main">

      <view v-for="item in filteredNews" :key="item.id" @click="handleToArticle(item.id)">
		<view class="news">
			<img :src='item.url' alt="" class="news-img"/>
			<view class="info">
			  <text class="title">{{ item.title }}</text>
			  <view class="detail">
			  	<view class="author">作者：{{ item.author }}</view>
			  	<view>{{ new Date(item.createTime).getMonth() + 1 }}月{{  new Date(item.createTime).getDate() }}日</view>
			  </view>
			</view>
		</view>
      </view>
  </view>
</template>

<script>
import {getNewsList} from '@/api/meeting/meeting';

export default {
  name:'newsList',
  mounted() {
	getNewsList().then(resp => {
		console.log('news', resp.data);
		this.news = resp.data;
	})
  },
  data() {
    return {
      news: [],
	  keyword: '',
    }
  },
	computed: {
	  filteredNews() {
		if (!this.keyword) return this.news;
		const lowerKeyword = this.keyword.toLowerCase();
		return this.news.filter(item => 
		  item.title.toLowerCase().includes(lowerKeyword) ||
		  item.author.toLowerCase().includes(lowerKeyword) ||
		  item.time.includes(this.keyword)
		);
	  }
	},
  methods:{
	  handleToArticle(newsId) {
	    this.$tab.navigateTo(`/pages/news/newsArticle?newsId=${newsId}`)
	  },
	  clearSearch() {
	    this.keyword = '';
	  },
	  handleSearch() {
		// 可以在此添加防抖逻辑
		console.log('Searching:', this.keyword);
	  },
  }
}
</script>

<style scoped>
	.main{
		padding: 20px 10px;
		background-color: white;
		min-height: 100vh;
	}
	.news{
		display: flex;
		align-items: flex-start;
		margin-bottom: 3vh;
	}
	.news-img{
		width: 36vw;
		height: 10vh;
		object-fit: cover;
		border-radius: 8px;
		box-shadow: rgba(149, 157, 165, 0.2) 0px 8px 24px;
	}
	.info{
		margin-left: 5vw;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		flex: 1;
	}
	.title{
		font-size: 16PX;
		font-weight: 500;
		color: #333;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 3;
		overflow: hidden;
	}
	.detail{

		display: flex;
		font-size: 13px;
		color: #999;
		font-weight: 300;
		flex-direction: column;
	}
	.author {
		margin-top: 24px;
	}
</style>