<template>
  <view class="agenda_container">
    <view v-for="(item, index) in agendaList" :key="index" class="agenda-item">

      <view class="time">{{ item.time[0] }} - {{ item.time[1] }}</view>

      <view class="message">
        <view class="title">{{ item.content }}</view>
        <view v-if="item.meta" class="people">
          <view v-for="(person, pIndex) in item.meta" :key="pIndex" class="person">
            <text class="name">{{ person[0] }}</text>
            <text class="position">{{ person[1] }}</text>
          </view>
        </view>
      </view>

      <view v-if="index < agendaList.length - 1" class="divider"></view>

    </view>
  </view>
</template>

<script>
import {formatTimeRange} from '@/utils/time';

export default {
  props: {
    id: {type: Number}
  },
  mounted() {
  },
  watch: {
      //正确给 cData 赋值的 方法
      id: function(newVal,oldVal){
          this.id = newVal;  //newVal即是id
          newVal && this.loadAgendaList(); //newVal存在的话执行drawChar函数
      }
  },
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
  },
  computed: {
    agendaList() {
      const raw_agendas = this.$store.state.meeting.agendaList;
      // console.log(raw_agendas)
      const agendas = raw_agendas.map(item => {
        const content = item.content;
        const time = formatTimeRange(item.beginTime, item.endTime);
        console.log(time)
        let meta = null;
        if (item.meta!==null) {
          meta = this.formatMetaString(item.meta);
        }
        return {
          time: time,
          content: content,
          meta: meta
        }
      });
      return agendas;
    }
  },
  methods: {
    loadAgendaList() {
      this.$store.dispatch('AgendaList', this.id)
    },
    formatMetaString(input) {
      // 按逗号分割字符串，得到每个 "姓名-职务" 的组合
      const items = input.split(',');
      // 初始化结果数组
      const result = [];
      // 遍历每个 "姓名-职务" 组合
      items.forEach(item => {
          // 去除多余的空格
          const trimmedItem = item.trim();
          // 按短横线分割，得到姓名和职务
          const [name, position] = trimmedItem.split('-').map(part => part.trim());
          // 将姓名和职务作为数组添加到结果中
          result.push([name, position]);
      });

      return result;
    }
  }
};
</script>

<style scoped>
.agenda_container {
  padding: 10px;
  background-color: #f9f9f9;
  margin-bottom: 40px;
}

.agenda-item {
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.time {
  padding-top: 15px;
  font-size: 14px;
  font-weight: 300;
  color: #666;
  width: 35%;
  text-align: center;
}

.title {
  font-size: 14px;
  color: #333;
  margin-top: 5px;
  font-weight: 500;
}

.message {
  width: 65%;
  border-left: 1px solid black;
  padding-left: 10px;
  padding-bottom: 5px;
  padding-top: 5px;
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
  font-size: 12px;
  color: #999;
  font-weight: 300;
}

.position {
  font-size: 12px;
  color: #999;
  font-weight: 300;
}

.divider {
  height: 1px;
  background-color: #ddd;
  margin: 20px 0;
}
</style>