<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="物资名称" prop="materialName">
        <el-input v-model="queryParams.materialName" placeholder="请输入物资名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="流转类型" prop="recordType">
        <el-select v-model="queryParams.recordType" placeholder="请选择类型" clearable>
          <el-option label="入库" value="IN" />
          <el-option label="出库" value="OUT" />
          <el-option label="报损" value="DAMAGE" />
          <el-option label="报废" value="SCRAP" />
        </el-select>
      </el-form-item>
      <el-form-item label="操作人" prop="operator">
        <el-input v-model="queryParams.operator" placeholder="请输入操作人" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="操作时间">
        <el-date-picker v-model="dateRange" size="small" style="width: 240px" value-format="yyyy-MM-dd" type="daterange" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-bottom" size="mini" @click="handleStockIn" v-hasPermi="['asset:record:in']">入库</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-top" size="mini" @click="handleStockOut" v-hasPermi="['asset:record:out']">出库</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-warning-outline" size="mini" @click="handleDamage" v-hasPermi="['asset:record:damage']">报损</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleScrap" v-hasPermi="['asset:record:scrap']">报废</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['asset:record:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 流转类型统计卡片 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card stat-in">
          <div class="stat-title">入库次数</div>
          <div class="stat-value">{{ statsData.IN || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card stat-out">
          <div class="stat-title">出库次数</div>
          <div class="stat-value">{{ statsData.OUT || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card stat-damage">
          <div class="stat-title">报损次数</div>
          <div class="stat-value">{{ statsData.DAMAGE || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card stat-scrap">
          <div class="stat-title">报废次数</div>
          <div class="stat-value">{{ statsData.SCRAP || 0 }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="recordList">
      <el-table-column label="编号" align="center" prop="recordId" width="80" />
      <el-table-column label="物资名称" align="center" prop="materialName" show-overflow-tooltip />
      <el-table-column label="物资编码" align="center" prop="materialCode" />
      <el-table-column label="流转类型" align="center" prop="recordType" width="100">
        <template slot-scope="scope">
          <el-tag :type="getTypeTag(scope.row.recordType)" size="small">
            {{ getTypeLabel(scope.row.recordType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="数量" align="center" prop="quantity" width="80" />
      <el-table-column label="操作人" align="center" prop="operator" />
      <el-table-column label="领用人" align="center" prop="targetPerson" />
      <el-table-column label="来源仓库" align="center" prop="fromWarehouseName" show-overflow-tooltip />
      <el-table-column label="目标仓库" align="center" prop="toWarehouseName" show-overflow-tooltip />
      <el-table-column label="操作时间" align="center" prop="operateTime" width="180" />
      <el-table-column label="原因/用途" align="center" prop="reason" show-overflow-tooltip />
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 入库对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogOpen" width="500px" append-to-body>
      <el-form ref="opForm" :model="opForm" :rules="opRules" label-width="100px">
        <el-form-item label="选择物资" prop="materialId">
          <el-select v-model="opForm.materialId" placeholder="请选择物资" filterable @change="handleMaterialChange">
            <el-option v-for="item in materialOptions" :key="item.materialId" :label="item.materialName + '(' + item.materialCode + ')'" :value="item.materialId" />
          </el-select>
        </el-form-item>
        <el-form-item label="数量" prop="quantity">
          <el-input-number v-model="opForm.quantity" :min="1" controls-position="right" />
        </el-form-item>
        <el-form-item v-if="opType === 'IN'" label="目标仓库" prop="toWarehouseId">
          <el-select v-model="opForm.toWarehouseId" placeholder="请选择目标仓库">
            <el-option v-for="item in warehouseOptions" :key="item.warehouseId" :label="item.warehouseName" :value="item.warehouseId" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="opType === 'OUT'" label="领用人" prop="targetPerson">
          <el-input v-model="opForm.targetPerson" placeholder="请输入领用人" />
        </el-form-item>
        <el-form-item label="操作时间" prop="operateTime">
          <el-date-picker v-model="opForm.operateTime" type="datetime" placeholder="请选择操作时间" value-format="yyyy-MM-dd HH:mm:ss" style="width: 100%" />
        </el-form-item>
        <el-form-item label="原因/用途" prop="reason">
          <el-input v-model="opForm.reason" type="textarea" placeholder="请输入原因或用途" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="opForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitOpForm">确 定</el-button>
        <el-button @click="cancelDialog">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRecord, stockIn, stockOut, doDamage, doScrap, getRecordStats } from "@/api/asset/assetRecord"
import { listMaterial } from "@/api/warehouse/material"
import { optionselectWarehouse } from "@/api/warehouse/warehouse"

export default {
  name: "AssetRecord",
  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      recordList: [],
      materialOptions: [],
      warehouseOptions: [],
      dateRange: [],
      statsData: {},
      dialogTitle: "",
      dialogOpen: false,
      opType: "",
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        materialName: undefined,
        recordType: undefined,
        operator: undefined
      },
      opForm: {},
      opRules: {
        materialId: [
          { required: true, message: "请选择物资", trigger: "change" }
        ],
        quantity: [
          { required: true, message: "请输入数量", trigger: "blur" }
        ],
        operateTime: [
          { required: true, message: "请选择操作时间", trigger: "change" }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getStats()
    this.getMaterialOptions()
    this.getWarehouseOptions()
  },
  methods: {
    getList() {
      this.loading = true
      const params = { ...this.queryParams }
      if (this.dateRange && this.dateRange.length === 2) {
        params.beginTime = this.dateRange[0]
        params.endTime = this.dateRange[1]
      }
      listRecord(params).then(response => {
        this.recordList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    getStats() {
      getRecordStats(null).then(response => {
        const data = response.data || []
        this.statsData = {}
        data.forEach(item => {
          this.statsData[item.recordType] = item.quantity
        })
      })
    },
    getMaterialOptions() {
      listMaterial({ pageSize: 1000 }).then(response => {
        this.materialOptions = response.rows
      })
    },
    getWarehouseOptions() {
      optionselectWarehouse().then(response => {
        this.warehouseOptions = response.data
      })
    },
    getTypeTag(type) {
      const map = { 'IN': 'success', 'OUT': 'primary', 'DAMAGE': 'warning', 'SCRAP': 'danger' }
      return map[type] || 'info'
    },
    getTypeLabel(type) {
      const map = { 'IN': '入库', 'OUT': '出库', 'DAMAGE': '报损', 'SCRAP': '报废' }
      return map[type] || type
    },
    handleMaterialChange(val) {
      const material = this.materialOptions.find(item => item.materialId === val)
      if (material) {
        this.opForm.assetCode = material.materialCode
      }
    },
    resetOpForm() {
      this.opForm = {
        materialId: undefined,
        quantity: 1,
        toWarehouseId: undefined,
        fromWarehouseId: undefined,
        targetPerson: undefined,
        operateTime: new Date(),
        reason: undefined,
        remark: undefined,
        assetCode: undefined
      }
      this.resetForm("opForm")
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
    handleStockIn() {
      this.resetOpForm()
      this.opType = "IN"
      this.dialogTitle = "资产入库"
      this.dialogOpen = true
    },
    handleStockOut() {
      this.resetOpForm()
      this.opType = "OUT"
      this.dialogTitle = "资产出库"
      this.dialogOpen = true
    },
    handleDamage() {
      this.resetOpForm()
      this.opType = "DAMAGE"
      this.dialogTitle = "资产报损"
      this.dialogOpen = true
    },
    handleScrap() {
      this.resetOpForm()
      this.opType = "SCRAP"
      this.dialogTitle = "资产报废"
      this.dialogOpen = true
    },
    cancelDialog() {
      this.dialogOpen = false
      this.resetOpForm()
    },
    submitOpForm() {
      this.$refs["opForm"].validate(valid => {
        if (valid) {
          const apiMap = {
            'IN': stockIn,
            'OUT': stockOut,
            'DAMAGE': doDamage,
            'SCRAP': doScrap
          }
          const api = apiMap[this.opType]
          api(this.opForm).then(() => {
            this.$modal.msgSuccess("操作成功")
            this.dialogOpen = false
            this.getList()
            this.getStats()
          })
        }
      })
    },
    handleExport() {
      this.download('asset/record/export', { ...this.queryParams }, `asset_record_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style scoped>
.stat-card {
  text-align: center;
  padding: 10px 0;
}
.stat-title {
  font-size: 14px;
  color: #909399;
}
.stat-value {
  font-size: 28px;
  font-weight: bold;
  margin-top: 5px;
}
.stat-in .stat-value { color: #67c23a; }
.stat-out .stat-value { color: #409eff; }
.stat-damage .stat-value { color: #e6a23c; }
.stat-scrap .stat-value { color: #f56c6c; }
</style>
