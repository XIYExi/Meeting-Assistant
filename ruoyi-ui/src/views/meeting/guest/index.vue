<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="嘉宾姓名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入嘉宾姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="嘉宾头衔" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入嘉宾头衔"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['meeting:guest:add']"
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
          v-hasPermi="['meeting:guest:edit']"
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
          v-hasPermi="['meeting:guest:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['meeting:guest:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="guestList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="嘉宾姓名" align="center" prop="name" />
      <el-table-column label="嘉宾头衔" align="center" prop="title" />
      <el-table-column label="嘉宾主讲内容" align="center" prop="content" />
      <el-table-column label="查看统计" align="center" prop="views" />
      <el-table-column label="点赞统计" align="center" prop="likes" />
      <el-table-column label="嘉宾头像" align="center" prop="avatar" />
      <el-table-column label="嘉宾海报" align="center" prop="url" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['meeting:guest:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['meeting:guest:remove']"
          >删除</el-button>
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

    <!-- 添加或修改会议嘉宾对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="嘉宾姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入嘉宾姓名" />
        </el-form-item>
        <el-form-item label="嘉宾头衔" prop="title">
          <el-input v-model="form.title" placeholder="请输入嘉宾头衔" />
        </el-form-item>
        <el-form-item label="嘉宾主讲内容">
          <el-input v-model="form.content" placeholder="请输入主讲内容"/>
        </el-form-item>
        <el-form-item label="嘉宾头像" prop="avatar">
          <el-input v-model="form.avatar" placeholder="请输入嘉宾头像" />
        </el-form-item>
        <el-form-item label="嘉宾海报" prop="url">
          <el-input v-model="form.url" placeholder="请输入嘉宾海报" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listGuest, getGuest, delGuest, addGuest, updateGuest } from "@/api/meeting/guest";

export default {
  name: "Guest",
  data() {
    return {
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
      // 会议嘉宾表格数据
      guestList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        title: null,
        content: null,
        views: null,
        likes: null,
        avatar: null,
        url: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "嘉宾姓名不能为空", trigger: "blur" }
        ],
        title: [
          { required: true, message: "嘉宾头衔不能为空", trigger: "blur" }
        ],
        content: [
          { required: true, message: "嘉宾主讲内容不能为空", trigger: "blur" }
        ],
        avatar: [
          { required: true, message: "嘉宾头像不能为空", trigger: "blur" }
        ],
        url: [
          { required: true, message: "嘉宾海报不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询会议嘉宾列表 */
    getList() {
      this.loading = true;
      listGuest(this.queryParams).then(response => {
        this.guestList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
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
        name: null,
        title: null,
        content: null,
        views: null,
        likes: null,
        avatar: null,
        url: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
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
      this.title = "添加会议嘉宾";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getGuest(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改会议嘉宾";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateGuest(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addGuest(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除会议嘉宾编号为"' + ids + '"的数据项？').then(function() {
        return delGuest(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('meeting/guest/export', {
        ...this.queryParams
      }, `guest_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
