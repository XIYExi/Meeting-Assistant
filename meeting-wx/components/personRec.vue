<template>
	<view class="main">
		<!-- 推荐人物 -->
		<view class="topbar">
			Recommend
		</view>
		<view class="personList">
			<view 
				class="content" 
				v-for="person in personList" 
				:key="person.id" 
			>
				<img :src="person.image" alt="" class="img"/>
				<text class="title">{{person.name}}</text>
			</view>
		</view>
	</view>
</template>

<script>
	export default{
		name:'personRec',
		data(){
			return {
				personList:[
					{id:1, name:'名字', image:"/static/news.png"},
					{id:2, name:'名字', image:"/static/news.png"},
					{id:3, name:'名字', image:"/static/news.png"},
					{id:4, name:'名字', image:"/static/news.png"},
					{id:5, name:'名字', image:"/static/news.png"},
					{id:6, name:'名字', image:"/static/news.png"}
				]
			}
		},

	}
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
	.personList {
		display: flex;
		flex-wrap: wrap;
		justify-content: space-between;
	}
	.content {
		width: 45%;
		margin-bottom: 1vh;
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