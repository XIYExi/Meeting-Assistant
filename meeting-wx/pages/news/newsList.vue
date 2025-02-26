<template>
	<view class="main">
	  <!-- 搜索框部分 -->
	  <view class="search-box" v-cloak>
		<image src="/static/logo.png" class="search-span"></image> 
		<input 
		  type="text" 
		  class="search-input" 
		  placeholder="请输入搜索内容" 
		  v-model="keyword"
		  @input="handleSearch"
		/>
		<button 
		  class="search-btn"
		  @click="clearSearch"
		  v-if="keyword"
		>清除</button>
	  </view>
  
	  <!-- 搜索结果提示 -->
	  <view class="search-tip" v-if="filteredNews.length === 0 && keyword">
		未找到"{{ keyword }}"相关结果
	  </view>
  
	  <view class="discover">
		Discover
	  </view>
      <view v-for="item in filteredNews" :key="item.id" @click="handleToArticle">
		<view class="news">
			<img :src='item.src' alt="" class="news-img"/>
			<view class="info">
			  <text class="title">{{ item.title }}</text>
			  <view class="detail">
			  	<text>{{ item.author }}</text>
			  	<text>{{ item.time }}</text>
			  </view>
			</view>
		</view>
      </view>
  </view>
</template>

<script>

export default {
  name:'newsList',
  data() {
    return {
      news: [
		 { src:"/static/news.png",id: 1, title:"西湖论剑演讲实录|吴世忠院士：迎接人工智能的安全挑战", author:"中国信息安全",time:'2024-05-22'},
		 { src:"/static/news.png",id: 2, title:"论剑演讲实录|吴世忠院士：迎接人工智能的安全挑战", author:"中国信息安全",time:'2024-05-22'},
		 { src:"/static/news.png",id: 3, title:"演讲实录|吴世忠院士：迎接人工智能的安全挑战", author:"中国信息安全",time:'2024-05-22'},
		 { src:"/static/news.png",id: 4, title:"实录|吴世忠院士：迎接人工智能的安全挑战", author:"中国信息安全",time:'2024-05-22'},
		 { src:"/static/news.png",id: 5, title:"|吴世忠院士：迎接人工智能的安全挑战", author:"中国信息安全",time:'2024-05-22'},
	  ],
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
	  handleToArticle() {
	    this.$tab.navigateTo('/pages/news/newsArticle')
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
		padding: 10px;
	}
	.search-box{
		display: flex;
		width: 100%;
		height: 10vw;
		background-color:#dededf ;
		border-radius: 10px;
		margin-top: 1vh;
	}
	.search-span{
		width: 10vw ;
		height: 100%;
		margin-left: 1vw;
	}
	.search-input{
		width: 65%;
		height: 100%;
		margin-left: 3vw;
		font-size: 4vw;
	}
	.search-btn{
		height: 100%;
		margin-right: 0;
		background-color:#dededf ;
		font-size: 4vw;
	}
	.discover{
		font-family: Georgia, 'Times New Roman', Times, serif;
		font-style: italic;
		font-size: 7vw;
		margin: 2.5vh 0;
		text-decoration-line: underline;
		text-underline-offset: 1vh;
	}
	.news{
		display: flex;
		align-items: flex-start;
		margin-bottom: 3vh;
	}
	.news-img{
		width: 40vw;
		height: 11vh;
		object-fit: cover;
	}
	.info{
		margin-left: 2vw;
		display: flex;
		flex-direction: column;
		height: 11vh;
		justify-content: space-between;
		flex: 1;
	}
	.title{
		font-size: 2vh;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 3;
		overflow: hidden;
	}
	.detail{
		display: flex;
		gap: 3vw;
		font-size: 1.5vh;
	}
</style>