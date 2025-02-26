<template>
	<view class="main">
		<view>
			<image :src="article.image" mode="aspectFit" class="news-img"></image>
		</view>
		<view class="news-title">
			<text class="title">{{article.title}}</text>
			<text class="excerpt">{{article.excerpt}}</text>
			<view class="publish">
				<text>来源：{{article.author}}</text>
				<text>{{article.time}}</text>
			</view>
		</view>
		<hr />
		<view class="content">
			<text>{{article.content}}</text>
		</view>
	</view>
</template>

<script>
	export default{
		name:'newsArticle',
		data(){
			return {
				article: {
				id: 1, 
				image:"/static/news.png", 
				title:"西湖论剑演讲实录|吴世忠院士：迎接人工智能的安全挑战", 
				author:"中国信息安全",
				time:'2024-05-22',
				content:'正文内容',
				excerpt:'摘要文字',
				},
				
			}
		}
	}
</script>

<style scoped>
	.main{
		padding: 10px;
	}
	.news-img{
		width: 100%;
	}
	.news-title{
		display: flex;
		flex-direction: column;
	}
	.title{
		font-size: 6vw;
		line-height: 5vh;
		margin-bottom: 2vh;
		font-weight: bold;
	}
	.excerpt{
		font-size: 4vw;
		margin-bottom: 2vh;
	}
	.publish{
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		margin-bottom: 2vh;
	}
	.content{
		margin-top: 2vh;
	}
</style>