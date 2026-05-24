<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="资产名称" prop="assetName">
        <el-input v-model="queryParams.assetName" placeholder="请输入资产名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="报修人" prop="reporter">
        <el-input v-model="queryParams.reporter" placeholder="请输入报修人" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="报修状态" clearable>
          <el-option label="待处理" value="pending" />
          <el-option label="处理中" value="processing" />
          <el-option label="已完成" value="completed" />
          <el-option label="已驳回" value="rejected" />
          <el-option label="已取消" value="cancelled" />
        </el-select>
      </el-form-item>
      <el-form-item label="优先级" prop="priority">
        <el-select v-model="queryParams.priority" placeholder="优先级" clearable>
          <el-option label="低" value="low" />
          <el-option label="普通" value="normal" />
          <el-option label="高" value="high" />
          <el-option label="紧急" value="urgent" />
        </el-select>
      </el-form-item>
      <el-form-item label="处理人" prop="handler">
        <el-input v-model="queryParams.handler" placeholder="请输入处理人" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="报修时间">
        <el-date-picker v-model="dateRange" size="small" style="width: 240px" value-format="yyyy-MM-dd" type="daterange" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['repair:info:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['repair:info:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['repair:info:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['repair:info:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="repairList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编号" align="center" prop="repairId" width="80" />
      <el-table-column label="资产名称" align="center" prop="assetName" show-overflow-tooltip />
      <el-table-column label="资产编码" align="center" prop="assetCode" show-overflow-tooltip />
      <el-table-column label="故障描述" align="center" prop="faultDesc" show-overflow-tooltip />
      <el-table-column label="报修人" align="center" prop="reporter" width="100" />
      <el-table-column label="联系电话" align="center" prop="reporterPhone" width="120" />
      <el-table-column label="优先级" align="center" width="80">
        <template slot-scope="scope">
          <el-tag :type="scope.row.priority === 'urgent' ? 'danger' : scope.row.priority === 'high' ? 'warning' : scope.row.priority === 'low' ? 'info' : ''" size="small">
            {{ scope.row.priority === 'urgent' ? '紧急' : scope.row.priority === 'high' ? '高' : scope.row.priority === 'low' ? '低' : '普通' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="100">
        <template slot-scope="scope">
          <el-tag :type="statusType(scope.row.status)" size="small">
            {{ statusLabel(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="处理人" align="center" prop="handler" width="100" />
      <el-table-column label="报修时间" align="center" prop="faultTime" width="180" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="250">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)">详情</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleStatusChange(scope.row)" v-hasPermi="['repair:info:edit']">变更状态</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['repair:info:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 报修详情对话框 -->
    <el-dialog title="报修详情" :visible.sync="detailOpen" width="700px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="资产名称">{{ detailData.assetName }}</el-descriptions-item>
        <el-descriptions-item label="资产编码">{{ detailData.assetCode || '-' }}</el-descriptions-item>
        <el-descriptions-item label="报修人">{{ detailData.reporter }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ detailData.reporterPhone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="故障位置">{{ detailData.location || '-' }}</el-descriptions-item>
        <el-descriptions-item label="故障时间">{{ detailData.faultTime }}</el-descriptions-item>
        <el-descriptions-item label="优先级">
          <el-tag :type="detailData.priority === 'urgent' ? 'danger' : detailData.priority === 'high' ? 'warning' : detailData.priority === 'low' ? 'info' : ''" size="small">
            {{ detailData.priority === 'urgent' ? '紧急' : detailData.priority === 'high' ? '高' : detailData.priority === 'low' ? '低' : '普通' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="statusType(detailData.status)" size="small">
            {{ statusLabel(detailData.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="故障描述" :span="2">{{ detailData.faultDesc }}</el-descriptions-item>
        <el-descriptions-item label="处理人">{{ detailData.handler || '-' }}</el-descriptions-item>
        <el-descriptions-item label="处理时间">{{ detailData.handleTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="处理结果" :span="2">{{ detailData.handleResult || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailData.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
      <el-divider content-position="left">故障照片</el-divider>
      <div v-if="detailData.photos" style="display: flex; flex-wrap: wrap; gap: 8px;">
        <el-image v-for="(photo, index) in detailData.photos.split(',')" :key="index" :src="photo" :preview-src-list="detailData.photos.split(',')" style="width: 100px; height: 100px;" fit="cover" />
      </div>
      <div v-else style="color: #909399;">暂无照片</div>
      <div slot="footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 新增/修改报修对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="资产名称" prop="assetName">
          <el-input v-model="form.assetName" placeholder="请输入资产名称" />
        </el-form-item>
        <el-form-item label="资产编码" prop="assetCode">
          <el-input v-model="form.assetCode" placeholder="请输入资产编码" />
        </el-form-item>
        <el-form-item label="报修人" prop="reporter">
          <el-input v-model="form.reporter" placeholder="请输入报修人" />
        </el-form-item>
        <el-form-item label="联系电话" prop="reporterPhone">
          <el-input v-model="form.reporterPhone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="故障位置" prop="location">
          <el-input v-model="form.location" placeholder="请输入故障位置" />
        </el-form-item>
        <el-form-item label="故障时间" prop="faultTime">
          <el-date-picker v-model="form.faultTime" type="datetime" placeholder="请选择故障时间" value-format="yyyy-MM-dd HH:mm:ss" style="width: 100%" />
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-select v-model="form.priority" placeholder="请选择优先级">
            <el-option label="低" value="low" />
            <el-option label="普通" value="normal" />
            <el-option label="高" value="high" />
            <el-option label="紧急" value="urgent" />
          </el-select>
        </el-form-item>
        <el-form-item label="故障描述" prop="faultDesc">
          <el-input v-model="form.faultDesc" type="textarea" :rows="3" placeholder="请描述故障情况" />
        </el-form-item>
        <el-form-item label="故障照片">
          <image-upload v-model="form.photos" :limit="5" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 状态变更对话框 -->
    <el-dialog title="变更报修状态" :visible.sync="statusOpen" width="500px" append-to-body>
      <el-form ref="statusForm" :model="statusForm" :rules="statusRules" label-width="100px">
        <el-form-item label="当前状态">
          <el-tag :type="statusType(statusForm.currentStatus)" size="small">
            {{ statusLabel(statusForm.currentStatus) }}
          </el-tag>
        </el-form-item>
        <el-form-item label="变更状态" prop="status">
          <el-select v-model="statusForm.status" placeholder="请选择新状态">
            <el-option label="待处理" value="pending" />
            <el-option label="处理中" value="processing" />
            <el-option label="已完成" value="completed" />
            <el-option label="已驳回" value="rejected" />
            <el-option label="已取消" value="cancelled" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理人" prop="handler">
          <el-input v-model="statusForm.handler" placeholder="请输入处理人" />
        </el-form-item>
        <el-form-item label="处理结果" prop="handleResult">
          <el-input v-model="statusForm.handleResult" type="textarea" :rows="3" placeholder="请输入处理结果" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitStatusChange">确 定</el-button>
        <el-button @click="statusOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRepair, getRepair, addRepair, updateRepair, changeRepairStatus, delRepair } from "@/api/repair/repair"
import ImageUpload from "@/components/ImageUpload"

export default {
  name: "Repair",
  components: { ImageUpload },
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      repairList: [],
      dateRange: [],
      title: "",
      open: false,
      detailOpen: false,
      statusOpen: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        assetName: undefined,
        reporter: undefined,
        status: undefined,
        priority: undefined,
        handler: undefined
      },
      form: {},
      detailData: {},
      statusForm: {},
      rules: {
        assetName: [
          { required: true, message: "资产名称不能为空", trigger: "blur" }
        ],
        faultDesc: [
          { required: true, message: "故障描述不能为空", trigger: "blur" }
        ],
        faultTime: [
          { required: true, message: "故障时间不能为空", trigger: "change" }
        ],
        reporter: [
          { required: true, message: "报修人不能为空", trigger: "blur" }
        ]
      },
      statusRules: {
        status: [
          { required: true, message: "请选择新状态", trigger: "change" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      const params = { ...this.queryParams }
      if (this.dateRange && this.dateRange.length === 2) {
        params.beginTime = this.dateRange[0]
        params.endTime = this.dateRange[1]
      }
      listRepair(params).then(response => {
        this.repairList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    statusType(status) {
      const map = {
        'pending': 'info',
        'processing': 'warning',
        'completed': 'success',
        'rejected': 'danger',
        'cancelled': 'info'
      }
      return map[status] || 'info'
    },
    statusLabel(status) {
      const map = {
        'pending': '待处理',
        'processing': '处理中',
        'completed': '已完成',
        'rejected': '已驳回',
        'cancelled': '已取消'
      }
      return map[status] || status
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      const now = new Date()
      const timeStr = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')} ${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}:${String(now.getSeconds()).padStart(2, '0')}`
      this.form = {
        repairId: undefined,
        materialId: undefined,
        assetCode: undefined,
        assetName: undefined,
        faultDesc: undefined,
        faultTime: timeStr,
        reporter: this.$store.state.user.name,
        reporterPhone: undefined,
        location: undefined,
        priority: "normal",
        status: "pending",
        photos: undefined,
        remark: undefined
      }
      this.resetForm("form")
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.dateRange = []
      this.resetForm("queryForm")
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.repairId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "新增报修"
    },
    handleUpdate(row) {
      this.reset()
      const repairId = row.repairId || this.ids
      getRepair(repairId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改报修"
      })
    },
    handleDetail(row) {
      getRepair(row.repairId).then(response => {
        this.detailData = response.data
        this.detailOpen = true
      })
    },
    handleStatusChange(row) {
      this.statusForm = {
        repairId: row.repairId,
        currentStatus: row.status,
        status: row.status,
        handler: this.$store.state.user.name,
        handleResult: row.handleResult
      }
      this.statusOpen = true
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.repairId != undefined) {
            updateRepair(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addRepair(this.form).then(() => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    submitStatusChange() {
      this.$refs["statusForm"].validate(valid => {
        if (valid) {
          changeRepairStatus(this.statusForm).then(() => {
            this.$modal.msgSuccess("状态变更成功")
            this.statusOpen = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const repairIds = row.repairId || this.ids
      this.$modal.confirm('是否确认删除报修编号为"' + repairIds + '"的数据项？').then(function() {
        return delRepair(repairIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('repair/info/export', { ...this.queryParams }, `repair_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
