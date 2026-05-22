<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="巡检物资" prop="materialId">
        <el-select v-model="queryParams.materialId" placeholder="请选择物资" filterable clearable>
          <el-option v-for="item in materialOptions" :key="item.materialId" :label="item.materialName" :value="item.materialId" />
        </el-select>
      </el-form-item>
      <el-form-item label="巡检人" prop="inspector">
        <el-input v-model="queryParams.inspector" placeholder="请输入巡检人" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="巡检结果" prop="result">
        <el-select v-model="queryParams.result" placeholder="巡检结果" clearable>
          <el-option label="正常" value="normal" />
          <el-option label="异常" value="abnormal" />
        </el-select>
      </el-form-item>
      <el-form-item label="巡检时间">
        <el-date-picker v-model="dateRange" size="small" style="width: 240px" value-format="yyyy-MM-dd" type="daterange" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['inspection:info:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['inspection:info:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['inspection:info:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['inspection:info:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="inspectionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编号" align="center" prop="inspectionId" width="80" />
      <el-table-column label="物资名称" align="center" prop="materialName" show-overflow-tooltip />
      <el-table-column label="物资编码" align="center" prop="materialCode" />
      <el-table-column label="巡检人" align="center" prop="inspector" />
      <el-table-column label="巡检时间" align="center" prop="inspectionTime" width="180" />
      <el-table-column label="巡检结果" align="center" prop="result" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.result === 'normal' ? 'success' : 'danger'" size="small">
            {{ scope.row.result === 'normal' ? '正常' : '异常' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)">详情</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['inspection:info:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['inspection:info:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改巡检记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="巡检物资" prop="materialId">
          <el-select v-model="form.materialId" placeholder="请选择物资" filterable>
            <el-option v-for="item in materialOptions" :key="item.materialId" :label="item.materialName" :value="item.materialId" />
          </el-select>
        </el-form-item>
        <el-form-item label="巡检人" prop="inspector">
          <el-input v-model="form.inspector" placeholder="请输入巡检人" />
        </el-form-item>
        <el-form-item label="巡检时间" prop="inspectionTime">
          <el-date-picker v-model="form.inspectionTime" type="datetime" placeholder="请选择巡检时间" value-format="yyyy-MM-dd HH:mm:ss" style="width: 100%" />
        </el-form-item>
        <el-form-item label="巡检结果" prop="result">
          <el-radio-group v-model="form.result">
            <el-radio label="normal">正常</el-radio>
            <el-radio label="abnormal">异常</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="检查项">
          <el-checkbox-group v-model="form.itemIds">
            <el-checkbox v-for="item in inspectionItemOptions" :key="item.itemId" :label="item.itemId">{{ item.itemName }}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 巡检详情对话框 -->
    <el-dialog title="巡检详情" :visible.sync="detailOpen" width="600px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="物资名称">{{ detailForm.materialName }}</el-descriptions-item>
        <el-descriptions-item label="物资编码">{{ detailForm.materialCode }}</el-descriptions-item>
        <el-descriptions-item label="巡检人">{{ detailForm.inspector }}</el-descriptions-item>
        <el-descriptions-item label="巡检时间">{{ detailForm.inspectionTime }}</el-descriptions-item>
        <el-descriptions-item label="巡检结果">
          <el-tag :type="detailForm.result === 'normal' ? 'success' : 'danger'" size="small">
            {{ detailForm.result === 'normal' ? '正常' : '异常' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <dict-tag :options="dict.type.sys_normal_disable" :value="detailForm.status" />
        </el-descriptions-item>
      </el-descriptions>
      <el-divider content-position="left">检查项明细</el-divider>
      <el-table :data="detailForm.details || []" border size="small">
        <el-table-column label="检查项" prop="itemName" />
        <el-table-column label="检查分组" prop="itemGroup" />
        <el-table-column label="检查结果" prop="checkResult">
          <template slot-scope="scope">
            <el-tag :type="scope.row.checkResult === 'normal' ? 'success' : 'danger'" size="small">
              {{ scope.row.checkResult === 'normal' ? '正常' : '异常' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="备注" prop="checkRemark" show-overflow-tooltip />
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import { listInspection, getInspection, addInspection, updateInspection, delInspection } from "@/api/inspection/inspection"
import { allItem } from "@/api/inspection/inspectionItem"
import { listMaterial } from "@/api/warehouse/material"

export default {
  name: "Inspection",
  dicts: ['sys_normal_disable'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      inspectionList: [],
      materialOptions: [],
      inspectionItemOptions: [],
      dateRange: [],
      title: "",
      open: false,
      detailOpen: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        materialId: undefined,
        inspector: undefined,
        result: undefined
      },
      form: {},
      detailForm: {},
      rules: {
        materialId: [
          { required: true, message: "巡检物资不能为空", trigger: "change" }
        ],
        inspector: [
          { required: true, message: "巡检人不能为空", trigger: "blur" }
        ],
        inspectionTime: [
          { required: true, message: "巡检时间不能为空", trigger: "change" }
        ],
        result: [
          { required: true, message: "巡检结果不能为空", trigger: "change" }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getMaterialOptions()
    this.getInspectionItemOptions()
  },
  methods: {
    getList() {
      this.loading = true
      const params = { ...this.queryParams }
      if (this.dateRange && this.dateRange.length === 2) {
        params.beginTime = this.dateRange[0]
        params.endTime = this.dateRange[1]
      }
      listInspection(params).then(response => {
        this.inspectionList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    getMaterialOptions() {
      listMaterial({ pageSize: 1000 }).then(response => {
        this.materialOptions = response.rows
      })
    },
    getInspectionItemOptions() {
      allItem().then(response => {
        this.inspectionItemOptions = response.data
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        inspectionId: undefined,
        materialId: undefined,
        inspector: undefined,
        inspectionTime: undefined,
        result: "normal",
        status: "0",
        itemIds: [],
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
      this.ids = selection.map(item => item.inspectionId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加巡检记录"
    },
    handleUpdate(row) {
      this.reset()
      const inspectionId = row.inspectionId || this.ids
      getInspection(inspectionId).then(response => {
        this.form = response.data
        if (!this.form.itemIds) {
          this.form.itemIds = []
        }
        this.open = true
        this.title = "修改巡检记录"
      })
    },
    handleDetail(row) {
      getInspection(row.inspectionId).then(response => {
        this.detailForm = response.data
        this.detailOpen = true
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.inspectionId != undefined) {
            updateInspection(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addInspection(this.form).then(() => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const inspectionIds = row.inspectionId || this.ids
      this.$modal.confirm('是否确认删除巡检记录编号为"' + inspectionIds + '"的数据项？').then(function() {
        return delInspection(inspectionIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('inspection/info/export', { ...this.queryParams }, `inspection_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
