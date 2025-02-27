<template>
	<view class="main">
		<view>
			<image :src="article.url" mode="aspectFit" class="news-img"></image>
		</view>
		<view class="news-title">
			<text class="title">{{ article.title }}</text>
			<view class="publish">
				<text>来源：{{ article.author }}</text>
				<text>{{ article.createTime }}</text>
			</view>
		</view>
		<hr />
		<view class="content">
			<rich-text :nodes="content"></rich-text>
		</view>
	</view>
</template>

<script>
import {getNewsContent, getNewsDetails} from '@/api/meeting/meeting';

export default {
	name: 'newsArticle',
	onLoad(args) {
		getNewsDetails(args.newsId).then(resp => {
			//console.log('news detail page content: ', resp);
			this.article = resp.data;
		});
		getNewsContent(args.newsId).then(resp => {
			console.log('content', resp.data.content)
			this.content = decodeURIComponent(resp.data.content);
		})
	},
	mounted(params) {
		
	},
	data() {
		return {
			content: '',
			article: {
				id: 1,
				url: "/static/news.png",
				title: "西湖论剑演讲实录|吴世忠院士：迎接人工智能的安全挑战",
				author: "中国信息安全",
				createTime: '2024-05-22',
			},

		}
	}
}
</script>

<style scoped>
.main {
	padding: 10px;
}

.news-img {
	width: 100%;
}

.news-title {
	display: flex;
	flex-direction: column;
}

.title {
	font-size: 6vw;
	line-height: 5vh;
	margin-bottom: 2vh;
	font-weight: bold;
}

.excerpt {
	font-size: 4vw;
	margin-bottom: 2vh;
}

.publish {
	display: flex;
	flex-direction: row;
	justify-content: space-between;
	margin-bottom: 2vh;
}

.content {
	margin-top: 2vh;
}
</style>