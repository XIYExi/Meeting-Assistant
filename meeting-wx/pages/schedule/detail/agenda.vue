<template>
  <view class="container">
    <view v-for="(item, index) in agendaItems" :key="index" class="agenda-item">
      <view class="time">{{ item.time }}</view>
      <view class="title">{{ item.title }}</view>
      <view v-if="item.people" class="people">
        <view v-for="(person, pIndex) in item.people" :key="pIndex" class="person">
          <text class="name">{{ person.name }}</text>
          <text class="position">{{ person.position }}</text>
        </view>
      </view>
      <view v-if="index < agendaItems.length - 1" class="divider"></view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      agendaItems: [
        {
          time: "08:30 - 09:00",
          title: "媒体签到、暖场视频",
          people: []
        },
        {
          time: "09:00 - 09:05",
          title: "主持人开场",
          people: []
        },
        {
          time: "09:05 - 09:15",
          title: "领导致辞",
          people: [
            { name: "范渊", position: "安恒信息董事长" }
          ]
        },
        {
          time: "09:15 - 09:20",
          title: "发布仪式：安恒信息2024新一代产品全景图（恒脑2.0驱动全栈产品核心能力突破）",
          people: [
            { name: "范渊", position: "安恒信息董事长" },
            { name: "刘博", position: "安恒信息CTO" },
            { name: "袁明坤", position: "安恒信息CSO" },
            { name: "王欣", position: "安恒信息研究院院长" }
          ]
        },
        {
          time: "09:20 - 09:40",
          title: "AI如何“加”网络安全",
          people: [
            { name: "谭晓生", position: "北京赛博英杰科技有限公司创始人" }
          ]
        }
      ]
    };
  }
};
</script>

<style>
.container {
  padding: 20px;
  background-color: #f9f9f9;
}

.agenda-item {
  margin-bottom: 20px;
}

.time {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.title {
  font-size: 14px;
  color: #666;
  margin-top: 5px;
}

.people {
  margin-top: 10px;
}

.person {
  display: flex;
  justify-content: space-between;
  margin-bottom: 5px;
}

.name {
  font-size: 14px;
  color: #333;
}

.position {
  font-size: 12px;
  color: #999;
}

.divider {
  height: 1px;
  background-color: #ddd;
  margin: 20px 0;
}
</style>