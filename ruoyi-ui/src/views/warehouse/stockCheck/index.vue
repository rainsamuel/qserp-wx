<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="盘点单号" prop="checkNo">
        <el-input v-model="queryParams.checkNo" placeholder="请输入盘点单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="盘点仓库" prop="warehouseId">
        <el-select v-model="queryParams.warehouseId" placeholder="请选择仓库" clearable>
          <el-option v-for="item in warehouseOptions" :key="item.warehouseId" :label="item.warehouseName" :value="item.warehouseId" />
        </el-select>
      </el-form-item>
      <el-form-item label="盘点类型" prop="checkType">
        <el-select v-model="queryParams.checkType" placeholder="请选择类型" clearable>
          <el-option label="全盘" value="0" />
          <el-option label="抽盘" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="盘点中" value="0" />
          <el-option label="已完成" value="1" />
          <el-option label="已作废" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item label="盘点日期">
        <el-date-picker v-model="dateRange" style="width: 240px" value-format="yyyy-MM-dd" type="daterange" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['warehouse:stockCheck:add']">新增盘点</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['warehouse:stockCheck:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['warehouse:stockCheck:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="stockCheckList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="盘点单号" align="center" prop="checkNo" width="180" />
      <el-table-column label="盘点仓库" align="center" prop="warehouseName" />
      <el-table-column label="盘点类型" align="center" prop="checkType" width="80">
        <template slot-scope="scope">
          <el-tag :type="scope.row.checkType === '0' ? '' : 'warning'">{{ scope.row.checkType === '0' ? '全盘' : '抽盘' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="盘点人" align="center" prop="checker" width="100" />
      <el-table-column label="盘点日期" align="center" prop="checkDate" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.checkDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="差异金额" align="center" prop="totalDiffAmount" width="100">
        <template slot-scope="scope">
          <span :style="{ color: scope.row.totalDiffAmount < 0 ? '#F56C6C' : '#67C23A' }">
            {{ scope.row.totalDiffAmount != null ? '¥' + scope.row.totalDiffAmount : '-' }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '1' ? 'success' : scope.row.status === '2' ? 'info' : 'warning'">
            {{ scope.row.status === '1' ? '已完成' : scope.row.status === '2' ? '已作废' : '盘点中' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="250">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)">详情</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-if="scope.row.status === '0'" v-hasPermi="['warehouse:stockCheck:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-check" style="color:#67C23A" @click="handleComplete(scope.row)" v-if="scope.row.status === '0'" v-hasPermi="['warehouse:stockCheck:complete']">完成盘点</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-if="scope.row.status === '0'" v-hasPermi="['warehouse:stockCheck:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 盘点详情对话框 -->
    <el-dialog title="盘点单详情" :visible.sync="detailOpen" width="1000px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="盘点单号">{{ detailForm.checkNo }}</el-descriptions-item>
        <el-descriptions-item label="盘点仓库">{{ detailForm.warehouseName }}</el-descriptions-item>
        <el-descriptions-item label="盘点类型">{{ detailForm.checkType === '0' ? '全盘' : '抽盘' }}</el-descriptions-item>
        <el-descriptions-item label="盘点人">{{ detailForm.checker }}</el-descriptions-item>
        <el-descriptions-item label="盘点日期">{{ parseTime(detailForm.checkDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="差异金额">
          <span :style="{ color: detailForm.totalDiffAmount < 0 ? '#F56C6C' : '#67C23A' }">
            {{ detailForm.totalDiffAmount != null ? '¥' + detailForm.totalDiffAmount : '-' }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="detailForm.status === '1' ? 'success' : detailForm.status === '2' ? 'info' : 'warning'">
            {{ detailForm.status === '1' ? '已完成' : detailForm.status === '2' ? '已作废' : '盘点中' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="备注">{{ detailForm.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
      <el-divider content-position="left">盘点明细</el-divider>
      <el-table :data="detailList" border size="small">
        <el-table-column label="物资编码" align="center" prop="materialCode" width="120" />
        <el-table-column label="物资名称" align="center" prop="materialName" show-overflow-tooltip />
        <el-table-column label="规格型号" align="center" prop="spec" width="120" />
        <el-table-column label="单位" align="center" prop="unit" width="60" />
        <el-table-column label="系统数量" align="center" prop="systemQuantity" width="80" />
        <el-table-column label="实际数量" align="center" prop="actualQuantity" width="80" />
        <el-table-column label="差异数量" align="center" prop="diffQuantity" width="80">
          <template slot-scope="scope">
            <span :style="{ color: scope.row.diffQuantity < 0 ? '#F56C6C' : scope.row.diffQuantity > 0 ? '#67C23A' : '' }">
              {{ scope.row.diffQuantity }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="差异金额" align="center" prop="diffAmount" width="100" />
        <el-table-column label="差异原因" align="center" prop="diffReason" width="150" />
      </el-table>
    </el-dialog>

    <!-- 新增/编辑盘点单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1100px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="盘点单号" prop="checkNo">
              <el-input v-model="form.checkNo" placeholder="自动生成" :disabled="!!form.checkId" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="盘点仓库" prop="warehouseId">
              <el-select v-model="form.warehouseId" placeholder="请选择仓库" style="width:100%" @change="onWarehouseChange">
                <el-option v-for="item in warehouseOptions" :key="item.warehouseId" :label="item.warehouseName" :value="item.warehouseId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="盘点类型" prop="checkType">
              <el-select v-model="form.checkType" placeholder="请选择类型" style="width:100%">
                <el-option label="全盘" value="0" />
                <el-option label="抽盘" value="1" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="盘点日期" prop="checkDate">
              <el-date-picker v-model="form.checkDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="盘点人" prop="checker">
              <el-input v-model="form.checker" placeholder="请输入盘点人" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <el-divider content-position="left">盘点明细（选择仓库后自动加载物资）</el-divider>

      <el-table :data="form.detailList" border size="small" style="margin-top: 10px">
        <el-table-column label="物资编码" align="center" prop="materialCode" width="120" />
        <el-table-column label="物资名称" align="center" prop="materialName" show-overflow-tooltip min-width="150" />
        <el-table-column label="规格型号" align="center" prop="spec" width="120" />
        <el-table-column label="单位" align="center" prop="unit" width="60" />
        <el-table-column label="系统数量" align="center" prop="systemQuantity" width="80" />
        <el-table-column label="实际数量" width="120">
          <template slot-scope="scope">
            <el-input-number v-model="scope.row.actualQuantity" size="small" :min="0" controls-position="right" style="width:100px" @change="calcDiff(scope.row)" />
          </template>
        </el-table-column>
        <el-table-column label="差异数量" align="center" width="80">
          <template slot-scope="scope">
            <span :style="{ color: scope.row.diffQuantity < 0 ? '#F56C6C' : scope.row.diffQuantity > 0 ? '#67C23A' : '' }">
              {{ scope.row.diffQuantity || 0 }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="差异金额" align="center" width="100">
          <template slot-scope="scope">
            {{ scope.row.diffAmount || 0 }}
          </template>
        </el-table-column>
        <el-table-column label="差异原因" width="180">
          <template slot-scope="scope">
            <el-input v-model="scope.row.diffReason" size="small" placeholder="请输入原因" />
          </template>
        </el-table-column>
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listStockCheck, getStockCheck, addStockCheck, updateStockCheck, delStockCheck, completeStockCheck } from '@/api/warehouse/stockCheck'
import { listWarehouse } from '@/api/warehouse/warehouse'
import { listMaterial } from '@/api/warehouse/material'

export default {
  name: 'StockCheck',
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      stockCheckList: [],
      detailList: [],
      title: '',
      open: false,
      detailOpen: false,
      dateRange: [],
      warehouseOptions: [],
      materialOptions: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        checkNo: undefined,
        warehouseId: undefined,
        checkType: undefined,
        status: undefined
      },
      form: {},
      detailForm: {},
      rules: {
        warehouseId: [{ required: true, message: '请选择盘点仓库', trigger: 'change' }],
        checkType: [{ required: true, message: '请选择盘点类型', trigger: 'change' }],
        checkDate: [{ required: true, message: '请选择盘点日期', trigger: 'change' }],
        checker: [{ required: true, message: '请输入盘点人', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
    this.getWarehouseOptions()
    this.getMaterialOptions()
  },
  methods: {
    getList() {
      this.loading = true
      const params = { ...this.queryParams }
      if (this.dateRange && this.dateRange.length === 2) {
        params.params = {
          beginCheckDate: this.dateRange[0],
          endCheckDate: this.dateRange[1]
        }
      }
      listStockCheck(params).then(response => {
        this.stockCheckList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    getWarehouseOptions() {
      listWarehouse().then(response => {
        this.warehouseOptions = response.rows
      })
    },
    getMaterialOptions() {
      listMaterial({ pageSize: 1000 }).then(response => {
        this.materialOptions = response.rows
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.dateRange = []
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.checkId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    reset() {
      this.form = {
        checkId: undefined,
        checkNo: undefined,
        warehouseId: undefined,
        checkType: '0',
        checkDate: undefined,
        checker: undefined,
        status: undefined,
        totalDiffAmount: undefined,
        remark: undefined,
        detailList: []
      }
      this.resetForm('form')
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增盘点单'
    },
    handleUpdate(row) {
      this.reset()
      const checkId = row.checkId || this.ids[0]
      getStockCheck(checkId).then(response => {
        this.form = response.data
        if (!this.form.detailList) {
          this.form.detailList = []
        }
        this.open = true
        this.title = '修改盘点单'
      })
    },
    handleDetail(row) {
      getStockCheck(row.checkId).then(response => {
        this.detailForm = response.data
      })
      // 获取明细
      import('@/api/warehouse/stockCheck').then(api => {
        api.getDetailList(row.checkId).then(response => {
          this.detailList = response.data || []
          this.detailOpen = true
        })
      })
    },
    onWarehouseChange(warehouseId) {
      if (!warehouseId) {
        this.form.detailList = []
        return
      }
      // 加载该仓库的物资列表作为盘点明细
      listMaterial({ warehouseId: warehouseId, pageSize: 1000 }).then(response => {
        const materials = response.rows || []
        this.form.detailList = materials.map(m => ({
          materialId: m.materialId,
          materialCode: m.materialCode,
          materialName: m.materialName,
          categoryId: m.categoryId,
          spec: m.spec,
          unit: m.unit,
          systemQuantity: m.stockQuantity || 0,
          actualQuantity: null,
          diffQuantity: 0,
          unitPrice: m.unitPrice,
          diffAmount: 0,
          diffReason: '',
          remark: ''
        }))
      })
    },
    calcDiff(row) {
      if (row.actualQuantity != null && row.systemQuantity != null) {
        row.diffQuantity = row.actualQuantity - row.systemQuantity
        if (row.unitPrice) {
          row.diffAmount = (row.unitPrice * row.diffQuantity).toFixed(2)
        }
      }
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (!this.form.detailList || this.form.detailList.length === 0) {
            this.$modal.msgError('请先选择盘点仓库加载物资')
            return
          }
          if (this.form.checkId) {
            updateStockCheck(this.form).then(() => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addStockCheck(this.form).then(() => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleComplete(row) {
      this.$modal.confirm('确认完成盘点？完成后将自动更新库存数量。').then(() => {
        return completeStockCheck({ checkId: row.checkId })
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('盘点完成，库存已更新')
      }).catch(() => {})
    },
    handleDelete(row) {
      const checkIds = row.checkId || this.ids
      this.$modal.confirm('是否确认删除盘点单编号为"' + checkIds + '"的数据项？').then(() => {
        return delStockCheck(checkIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleExport() {
      this.download('warehouse/stockCheck/export', { ...this.queryParams }, `stockCheck_${new Date().getTime()}.xlsx`)
    },
    cancel() {
      this.open = false
      this.reset()
    }
  }
}
</script>
