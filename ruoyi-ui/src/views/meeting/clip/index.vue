<template>
  <div>
    <div style="display: flex;">
      <div v-if="meeting!==null" style="margin: 20px;padding: 10px;">
        <p>会议名称：{{ meeting.title }}</p>
        <p>会议时间：{{ meeting.beginTime }}</p>
        <p>会议地点： {{ meeting.location }}</p>
        <el-alert
          title="为会议添加可下载附件，请不要上传过大的文件！"
          type="info">
        </el-alert>
      </div>
      <div v-if="agenda!==null" style="margin: 20px;padding: 10px;">
        <p>议程名称：{{ meeting.title }}</p>
        <p>议程时间：{{ meeting.beginTime }}</p>
        <el-alert
          title="为子议程添加可下载附件，请不要上传过大的文件！"
          type="info">
        </el-alert>
      </div>

      <div class="fixed-upload">
        <p>上传附件</p>
        <el-upload
          class="upload-demo"
          ref="upload"
          action=""
          :on-remove="handleRemove"
          :file-list="fileList"
          :auto-upload="false"
          :on-change="handleChange"
        >
          <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
          <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
          <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
        </el-upload>
      </div>
    </div>


    <div class="app-container">
      <el-table :data="clips" >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="id" align="center" prop="id" />
        <el-table-column label="文件名称" align="center" prop="fileName" />
        <el-table-column label="文件类型" align="center" prop="fileType" />
        <el-table-column label="文件大小" align="center" prop="fileSize" />
        <el-table-column label="存储地址" align="center" prop="url" >
          <template slot-scope="scope">
            <el-link :href="scope.row.url" type="primary" underline>查看</el-link>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import { cosUploadFile } from '@/api/meeting/cos';
import {addClip, clips, delClip, getMeeting} from '@/api/meeting/meeting';
import {getAgenda} from "@/api/meeting/agenda";

export default {
  data() {
    return{
      fileList: [],
      clips: [],
      meeting: null,
      agenda: null,
    }
  },
  mounted() {
    this.meetingId = this.$route.params.id;
    this.clipType = this.$route.params.type;
    // console.log(this.$route.params.id, this.$route.params.type)
    this.getClipsInfo(this.$route.params.id, this.$route.params.type);

    if ( this.$route.params.type == '1' ) {
      getMeeting(this.$route.params.id).then(resp => {
        this.meeting = resp.data;
      })
    }
    else {
      getAgenda(this.$route.params.id).then(resp => {
        this.agenda = resp.data;
      })
    }
  },
  methods: {
    getClipsInfo(id, type) {
      clips(id, type).then(resp => {
        console.log('clips', resp)
        this.clips = resp.data;
      })
    },

    handleChange(file, fileList) {
      this.fileList = fileList
    },
    handleRemove(file, fileList) {
      this.fileList = fileList
    },

    submitUpload() {

      //判断是否有文件再上传
      if (this.fileList.length === 0) {
        return this.$message.warning('请选取文件后再上传')
      }
      // 下面的代码将创建一个空的FormData对象:
      const formData = new FormData()
      // 你可以使用FormData.append来添加键/值对到表单里面；
      this.fileList.forEach((file) => {
        formData.append('files', file.raw)
      });

      //自定义的接口也可以用ajax或者自己封装的接口
      cosUploadFile(formData).then(resp => {
       if (resp.code === 200) {
         const data = resp.data;
         const params = data.map(elem => {
           const json = {};
           json['fileName'] = elem['fileName'];
           json['fileType'] = elem['fileType'];
           json['fileSize'] = elem['fileSize'];
           json['url'] = elem['url'];
           json['clipType'] = this.clipType;
           if (this.clipType == 1){
             json['meetingId'] = parseInt(this.meetingId);
             json['agendaId'] = 0;
           }
           else if (this.clipType == 2) {
             json['meetingId'] =0;
             json['agendaId'] = parseInt(this.meetingId);
           }
           else {
             json['meetingId'] = 0;
             json['agendaId'] = 0;
           }
           return json;
         });
         addClip(params).then(resp => {
           if (resp.code === 200) {
             this.$message.success("附件上传成功");
             this.getClipsInfo(this.meetingId, this.clipType);
           }
           else {
             this.$message.error("附件上传失败");
           }
         })
       }
       else {
         this.$message.error("上传失败");
       }
        //清空fileList
        this.fileList = []
      })

    },

    handleDelete(row) {
      const ids = row.id;
      this.$modal.confirm('是否确认删除会议编号为"' + ids + '"的数据项？').then(function() {
        return delClip(ids);
      }).then(() => {
        this.getClipsInfo(this.meetingId, this.clipType);
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
  }
}
</script>


<style scoped>
.fixed-upload{
  margin: 20px;
  padding: 20px;
  width: 500px;
  background-color: #efecec;
  border-radius: 12px;
  min-height: 200px;
}
</style>
