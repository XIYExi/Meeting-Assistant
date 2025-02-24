<template>
  <view class="container">
    <view class="product-grid">
      <view v-for="(item, index) in products" :key="index" class="product-item">
        <image :src="item.image" class="product-image"></image>
        <view class="product-info">
          <view class="product-name">{{ item.name }}</view>
          <view class="product-desc">{{ item.desc }}</view>
        </view>
        <view class="product-bottom">
          <view class="product-points">兑换积分：<span>{{ item.points }}</span></view>
          <view class="status" :class="{ soldout: item.soldout }">
            {{ item.soldout ? '已抢光' : '可兑换' }}
          </view>
        </view>
      </view>
    </view>
  </view>
</template>


<script>
export default {
  data() {
    return {
      products: [
        {
          name: "小米充电宝",
          desc: "全店已拼500万+件",
          points: 6580,
          image: "",
          soldout: false
        },
        {
          name: "安恒手提帆布包",
          desc: "退货包运费",
          points: 6887,
          image: "",
          soldout: false
        },
        {
          name: "《数据安全与隐私计算》",
          desc: "全店已拼500万+件",
          points: 499,
          image: "",
          soldout: false
        },
        {
          name: "遛遛水豚卡通毛绒玩具",
          desc: "全店已拼1.1万+件",
          points: 2750,
          image: "",
          soldout: true
        },
		{
		  name: "遛遛水豚卡通毛绒玩具",
		  desc: "全店已拼1.1万+件",
		  points: 2750,
		  image: "",
		  soldout: true
		}
      ]
    };
  }
};
</script>


<style scoped>
.container {
  width: 100%;
  padding: 10px;
  background-color: #f5f5f5;
}

.product-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: space-between;
}

.product-item {
  width: 48%;
  background: #fff;
  border-radius: 10px;
  overflow: hidden;
  padding-bottom: 20px;  /* 留出空间放置底部内容 */
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  position: relative;
}

.product-image {
  width: 100%;
  height: 180px; /* 固定高度 */
  object-fit: cover;
}

.product-info {
  padding: 10px;
}

.product-name {
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

.product-desc {
  font-size: 12px;
  color: #777;
  margin: 5px 0;
}

.product-bottom {
  position: absolute;
  bottom: 10px;  /* 固定底部距离 */
  left: 10px;
  right: 10px;
}

.product-points {
  font-size: 12px;
  color: #ff5722;
  font-weight: bold;
}

.status {
  margin-top: 5px;
  padding: 3px 5px;
  font-size: 12px;
  color: #fff;
  background: #8a2be2;
  border-radius: 10px;
  display: inline-block;
  position: absolute;
  bottom: 0;
  right: 0; /* 将状态按钮放置右下角 */
}

.soldout {
  background: #ccc;
  color: #ff3d00;
}
</style>
