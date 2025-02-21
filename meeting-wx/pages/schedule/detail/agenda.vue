<template>
  <view class="agenda_container">
    <view class="agenda_title">会议议程</view>

    <view v-for="(item, index) in agendaList" :key="index" class="agenda-item">

      <view class="time">
        <view class="agenda-idx">{{ index+1 }}</view>
        <view class="agenda-time">{{ item.time[0] }} - {{ item.time[1] }}</view>
      </view>

      <view class="message">
        <view class="title">{{ item.content }}</view>
        <view v-if="item.meta" class="people">
          <view v-for="(person, pIndex) in item.meta" :key="pIndex" class="person">
            <text class="name">{{ person[0] }}</text>
            <text class="position">{{ person[1] }}</text>
          </view>
        </view>
      </view>
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
      agendaItems: []
    };
  },
  computed: {
    agendaList() {
      const raw_agendas = this.$store.state.meeting.agendaList;
      // console.log(raw_agendas)
      const agendas = raw_agendas.map(item => {
        const content = item.content;
        const time = formatTimeRange(item.beginTime, item.endTime);
        //console.log(time)
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
  padding: 5px 0px;
}

.agenda_title {
  font-weight: 600;
  font-size: 24px;
  margin-top: 10px;
  margin-bottom: 15px;
  padding-left: 5px;
}

.agenda-item {
  margin-bottom: 10px;
  display: flex;
  justify-content: flex-start;
  align-items: flex-start;
}

.time {
  padding-top: 15px;
  font-size: 14px;
  font-weight: 300;
  color: #666;
  width: 30%;
  text-align: center;
  justify-content: center;
  align-items: center;
  display: flex;
  flex-direction: column;
}
.agenda-idx {
  border-radius: 12px;
  text-align: center;
  justify-content: center;
  align-items: center;
  display: flex;
  width: 35px;
  height: 35px;
  color: #FAFAFA;
  background-color: #006FEE;
  box-shadow: rgba(0, 0, 0, 0.3) 0px 19px 38px, rgba(0, 0, 0, 0.22) 0px 15px 12px;
}
.agenda-time {
  padding-top: 8px;
  font-size: 13px;
  font-weight: 300;
  color: #52525B;
  font-style: italic;
}

.title {
  font-size: 14px;
  color: #27272A;
  font-weight: 600;
}

.message {
  width: 60%;
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
</style>