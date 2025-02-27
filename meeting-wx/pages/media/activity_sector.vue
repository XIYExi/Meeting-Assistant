<template>
	<view class="main">

		<!-- <view class="sector-wrapper">
			<view v-for="(sector, index) in sectors" :key="index" class="sector"
				@click="handleNavigateActicity(sector.id)">
				<img :src="sector.url" class="sector-image" />
				<view class="sector-font-wrapper">
					<view class="sector-title">{{ sector.title }}</view>

					<view class="sector-desc">{{ sector.description }}</view>
				</view>
			</view>
		</view> -->

		<uni-list class="sector-wrapper">
			<!-- title、note设置标题和小文字 -->
			<uni-list-item direction="row" v-for="item in sectors" :key="item.id">
				<!-- 通过v-slot:header插槽定义列表左侧的图片显示，其他内容通过List组件内置属性实现-->
				<template v-slot:header>
					<!-- 当前判断长度只为简单判断类型，实际业务中，根据逻辑直接渲染即可 -->
					<img class="sector-image" :src="item.url" />
				</template>
				<template v-slot:body>
					<view class="sector-font-wrapper"  @click="handleNavigateActicity(item.id)">
						<view class="sector-title">{{ item.title }}</view>

						<view class="sector-desc">{{ item.description }}</view>
					</view>
				</template>
			</uni-list-item>
		</uni-list>


	</view>
</template>


<script>
import { getSectorList } from '@/api/meeting/meeting';
import '@/pages/media/activity_sector.css';
export default {
	name: 'activitySector',
	mounted() {
		getSectorList().then(resp => {
			console.log('sector list: ', resp.data);
			this.sectors = resp.data;
		})
	},
	data() {
		return {
			sectors: [],
		}
	},
	methods: {
		handleNavigateActicity(id) {
			uni.navigateTo({
				url: `/pages/media/activity?id=${id}`
			})
		}
	}
}
</script>