<template>
  <div>editor</div>
</template>

<style scoped>

</style>


<script>
export default {
  name: "Editor",
  mounted(params) {
    this.newsId = this.$route.params.id;
  },
  data() {
    return {
      // 对应新闻的id, 传递给mongodb插入用
      newsId: 0,
    }
  },
  methods: {

  }
}
</script>
