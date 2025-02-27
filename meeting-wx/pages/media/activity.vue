<template>
    <view class="activity_wrapper">

        <!-- <view class="ac_container" v-for="(ac, index) in activities" :key="index">
            <img :src="ac.url" class="ac_image"/>
            <view class="ac_font_wrapper">
                <view class="ac_title">{{ ac.title }}</view>
                <view class="ac_time">{{ ac.time }}</view>
            </view>
        </view> -->

        <uni-list>
			<!-- title、note设置标题和小文字 -->
			<uni-list-item direction="row" v-for="item in activities" :key="item.id" :title="item.title" :note="item.time">
				<!-- 通过v-slot:header插槽定义列表左侧的图片显示，其他内容通过List组件内置属性实现-->
				<template v-slot:header>
					<!-- 当前判断长度只为简单判断类型，实际业务中，根据逻辑直接渲染即可 -->
					<image class="ac_image" :src="item.url" mode="aspectFill"></image>
				</template>
			</uni-list-item>
		</uni-list>

    </view>
</template>


<script>
import '@/pages/media/activity.css';
import {getActivityList} from '@/api/meeting/meeting';

export default ({
    data() {
        return {
            activities: [],
        }
    },
    onLoad(args) {
        console.log('args', args.id)
        getActivityList(args.id).then(resp => {
            console.log('activity resp: ', resp.data)
            this.activities = resp.data;
        })
    },
    methods: {

    }
})
</script>