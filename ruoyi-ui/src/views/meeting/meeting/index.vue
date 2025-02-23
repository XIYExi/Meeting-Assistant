<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="会议名称" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入会议名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开始时间" prop="beginTime">
        <el-date-picker clearable
          v-model="queryParams.beginTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择会议开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间" prop="endTime">
        <el-date-picker clearable
          v-model="queryParams.endTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择会议结束时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['meeting:meeting:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['meeting:meeting:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['meeting:meeting:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['meeting:meeting:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="meetingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="会议表id" align="center" prop="id" />-->
      <el-table-column label="会议名称" align="center" prop="title" />
      <el-table-column label="会议开始时间" align="center" prop="beginTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.beginTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="会议结束时间" align="center" prop="endTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="会议地点" align="center" prop="location" >
        <template slot-scope="scope">
          <span>{{ scope.row.location.formattedAddress }}</span>
        </template>
      </el-table-column>
      <el-table-column label="会议封面海报图" align="center" prop="url" >
        <template slot-scope="scope">
          <img loading="lazy" :src="scope.row.url" style="width: 80px;height: 60px;"/>
        </template>
      </el-table-column>
      <el-table-column label="查看次数" align="center" prop="views" />
      <el-table-column label="会议类型" align="center" prop="type" >
        <template slot-scope="scope">
          <p>{{ meetingType[scope.row.type] }}</p>
        </template>
      </el-table-column>
      <el-table-column label="会议状态" align="center" prop="status" >
        <template slot-scope="scope">
          <p>{{ meetingStatus[scope.row.type] }}</p>
        </template>
      </el-table-column>
      <el-table-column label="会议开展类型" align="center" prop="meetingType" >
        <template slot-scope="scope">
          <p>{{ scope.row.meetingType === 1 ? '线下' : '线上' }}</p>
        </template>
      </el-table-column>
<!--      <el-table-column label="备注" align="center" prop="remark" />-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['meeting:meeting:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            @click="handleEditAgenda(scope.row)"
            v-hasPermi="['meeting:meeting:edit']"
          >议程</el-button>

          <el-dropdown>
            <span class="el-dropdown-link">
              更多<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleDelete(scope.row)"
                  v-hasPermi="['meeting:meeting:remove']"
                >删除</el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleUploadClip(scope.row)"
                  v-hasPermi="['meeting:meeting:remove']"
                >附件</el-button>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改会议对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-row :gutter="15">
        <el-form ref="form" :model="form" :rules="rules" label-width="100px">
          <el-col :span="24">
            <el-form-item label="会议名称" prop="title">
              <el-input v-model="form.title" placeholder="请输入会议名称" clearable :style="{width: '100%'}"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="开始时间" prop="beginTime">
              <el-date-picker
                clearable
                v-model="form.beginTime"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                format="yyyy-MM-dd HH:mm:ss"
                :style="{width: '100%'}"
                placeholder="请选择会议开始时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间" prop="endTime">
              <el-date-picker
                clearable
                v-model="form.endTime"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                format="yyyy-MM-dd HH:mm:ss"
                :style="{width: '100%'}"
                placeholder="请选择会议结束时间">
              </el-date-picker>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="会议地点">
              <el-input v-model="form.path" placeholder="请输入会议地点" />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="封面海报图" prop="url">
              <el-upload
                ref="uploadMeetingRef"
                drag
                action=""
                class="upload-demo"
                :http-request="httpRequest"
                :multiple="false"
                :limit="1"
                :auto-upload="false"
                :file-list="form.file"
              >
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>
              </el-upload>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="会议类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择会议类型" clearable :style="{width: '100%'}">
                <el-option
                  v-for="(item, index) in typeOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
                  :disabled="item.disabled"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="会议类型" prop="meetingType">
              <el-select
                v-model="form.meetingType"
                placeholder="请选择会议类型"
                clearable
                :style="{width: '100%'}">
                <el-option
                  v-for="(item, index) in meetingTypeOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
                  :disabled="item.disabled"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="开启直播">
              <el-switch v-model="form.live"></el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="会议简介" prop="remark">
              <el-input
                v-model="form.remark"
                type="textarea"
                placeholder="请输入会议简介"
                :autosize="{minRows: 4, maxRows: 4}"
                :style="{width: '100%'}"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMeeting, getMeeting, delMeeting, addMeeting, updateMeeting } from "@/api/meeting/meeting";
import {parseTime} from "../../../utils/ruoyi";
import { uuid } from '@/utils/uuid'

export default {
  name: "Meeting",
  data() {
    return {
      meetingStatus: {
        1: '尚未开始',
        2: '会议进行中',
        3: '会议结束'
      },
      meetingType: {
        1: '技术会议',
        2: '学术会议',
        3: '行业峰会' ,
        4: '新品发布'
      },
      typeOptions: [{
        "label": "技术会议",
        "value": 1
      }, {
        "label": "学术会议",
        "value": 2
      }, {
        "label": "行业峰会",
        "value": 3
      }, {
        "label": "新品发布",
        "value": 4
      }],
      meetingTypeOptions: [{
        "label": "线下会议",
        "value": 1
      }, {
        "label": "线上会议",
        "value": 2
      }],
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 会议表格数据
      meetingList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        beginTime: null,
        endTime: null,
        location: null,
        url: null,
        views: null,
        type: null,
        status: null,
        meetingType: null,
      },
      // 表单参数
      form: {
        id: null,
        title: null,
        beginTime: null,
        endTime: null,
        location: null,
        url: null,
        views: null,
        type: null,
        status: null,
        meetingType: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        imageId: null,
        file: null,
        path: null,
      },
      // 表单校验
      rules: {
        title: [
          { required: true, message: "会议名称不能为空", trigger: "blur" }
        ],
        beginTime: [
          { required: true, message: "会议开始时间不能为空", trigger: "blur" }
        ],
        endTime: [
          { required: true, message: "会议结束时间不能为空", trigger: "blur" }
        ],
        path: [
          { required: true, message: "会议地点不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    handleEditAgenda(row) {
      this.$router.push({path: `/meeting/agenda/${row.id}`})
    },
    httpRequest(param) {
      this.form.file = param.file
    },

    parseTime,
    /** 查询会议列表 */
    getList() {
      this.loading = true;
      listMeeting(this.queryParams).then(response => {
        console.log(response)
        this.meetingList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    handleUploadClip(row) {
      this.$router.push({path: `/meeting/agenda/${row.id}/${1}`})
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        title: null,
        beginTime: null,
        endTime: null,
        location: null,
        url: null,
        views: null,
        type: null,
        status: null,
        meetingType: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        imageId: null,
        file: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加会议";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getMeeting(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改会议";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs.uploadMeetingRef.submit();
      this.form.imageId = uuid(8, 16);

      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateMeeting(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMeeting(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除会议编号为"' + ids + '"的数据项？').then(function() {
        return delMeeting(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('meeting/meeting/export', {
        ...this.queryParams
      }, `meeting_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>


<style scoped>
.el-dropdown-link {
  cursor: pointer;
  color: #409EFF;
}
.el-icon-arrow-down {
  font-size: 12px;
}
</style>
